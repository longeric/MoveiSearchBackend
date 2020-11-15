package edu.pitt.ir.helpers.LuenceHelper;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import edu.pitt.ir.models.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LuenceIndexReader {

    private IndexReader indexReader;
    public final IndexSearcher searcher;
    private final Analyzer analyzer;
    private Query query;
    private final AzureBlob azureBlob;

    private static LuenceIndexReader luenceIndexReader = null;

    private LuenceIndexReader(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
        this.azureBlob = azureBlob;
        this.analyzer = analyzer;
        try {
            this.indexReader = DirectoryReader.open(ramDirectory);
        } catch (IOException e) {
            log.error("unable to create index reader because of" + e.getMessage());
        }

        if (this.indexReader == null) {
            throw new NullPointerException("unable to find indexReader");
        }

        this.searcher = new IndexSearcher(this.indexReader);
    }

    public List<ScoreDoc> searchContent(String content, int topK) {
        QueryParser queryParser = new QueryParser("content", analyzer);

        try {
            this.query = queryParser.parse(content);
            TopDocs topDocs = this.searcher.search(this.query, topK);
            return Arrays.stream(topDocs.scoreDocs).collect(Collectors.toList());

        } catch (ParseException e) {
            log.error("unable to parse content");
        } catch (IOException e) {
            log.error("unable to search query");
        }
        return null;
    }

    public List<QueryResult> searchSummary(List<ScoreDoc> scoreDocList) {

        List<QueryResult> queryResultList = new ArrayList<>();
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<strong>", "</strong>");
        SimpleFragmenter simpleFragmenter = new SimpleFragmenter(50);

        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(this.query));

        highlighter.setTextFragmenter(simpleFragmenter);

        scoreDocList.parallelStream().forEach(scoreDoc -> {
            try {
                Document document = this.searcher.doc(scoreDoc.doc);
                String name = document.get("name").substring(16);
                String content = this.azureBlob.readFiles(name);
                String firstTitle = content.split("\n")[0];
                String title = UCharacter.toTitleCase(firstTitle, BreakIterator.getTitleInstance());
                TokenStream tokenStream = this.analyzer.tokenStream("content",new StringReader(content));
                String matchContent = highlighter.getBestFragment(tokenStream,content);

                QueryResult queryResult = new QueryResult();
                queryResult.setTitle(title);
                queryResult.setContent(matchContent);
                queryResult.setScore(scoreDoc.score);
                queryResultList.add(queryResult);
            } catch (IOException e) {
                log.error("unable to find any summary");
            } catch (InvalidTokenOffsetsException e) {
                log.error("unable to highlight content");
            }
        });

        queryResultList.sort(((o1, o2) -> Float.compare(o2.getScore(), o1.getScore())));
        return queryResultList;
    }

    public static void getInstance(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
        if (luenceIndexReader == null)
            luenceIndexReader = new LuenceIndexReader(azureBlob, ramDirectory, analyzer);
    }

    public static LuenceIndexReader getInstance() {
        if (luenceIndexReader == null) {
            throw new NullPointerException("unable to initialize index reader");
        }

        return luenceIndexReader;
    }
}
