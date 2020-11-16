package edu.pitt.ir.helpers.LuceneHelper;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import edu.pitt.ir.models.DocumentDAO;
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

@Slf4j
public class LuceneIndexReader {

    private IndexReader indexReader;
    private final IndexSearcher searcher;
    private final Analyzer analyzer;
    private Query query;
    private final AzureBlob azureBlob;

    private static LuceneIndexReader luceneIndexReader = null;
    private static final int TITLE_START = 16;
    private static final int FRAGMENT_SIZE = 50;

    private LuceneIndexReader(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
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

    public List<DocumentDAO> searchDocument(String content, int topK) {
        QueryParser queryParser = new QueryParser("content", analyzer);

        List<DocumentDAO> documentDAOList = new ArrayList<>();
        try {
            this.query = queryParser.parse(content);
            TopDocs topDocs = this.searcher.search(this.query, topK);
            Arrays.stream(topDocs.scoreDocs).parallel().forEach(ScoreDoc -> {
                String title = this.getDocName(ScoreDoc);
                DocumentDAO documentDAO = new DocumentDAO(title, ScoreDoc.score);
                documentDAOList.add(documentDAO);
            });

        } catch (ParseException e) {
            log.error("unable to parse content");
        } catch (IOException e) {
            log.error("unable to search query");
        }
        return documentDAOList;
    }

    public List<QueryResult> searchSummary(List<DocumentDAO> documentDAOList) {

        List<QueryResult> queryResultList = new ArrayList<>();
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<strong>", "</strong>");
        SimpleFragmenter simpleFragmenter = new SimpleFragmenter(FRAGMENT_SIZE);

        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(this.query));

        highlighter.setTextFragmenter(simpleFragmenter);

        documentDAOList.parallelStream().forEach( documentDAO-> {
            this.getQueryResultList(highlighter, documentDAO, queryResultList);
        });

        queryResultList.sort(((o1, o2) -> Float.compare(o2.getScore(), o1.getScore())));
        return queryResultList;
    }

    private void getQueryResultList(Highlighter highlighter, DocumentDAO documentDAO, List<QueryResult> queryResultList) {
        try {
            String name = documentDAO.getTitle();

            String[] titleAndContent = this.azureBlob.readFiles(name).split("\n");

            String title = UCharacter.toTitleCase(titleAndContent[0], BreakIterator.getTitleInstance());

            TokenStream tokenStream = this.analyzer.tokenStream("content", new StringReader(titleAndContent[1].toLowerCase()));
            String matchContent = highlighter.getBestFragment(tokenStream, titleAndContent[1].toLowerCase());

            QueryResult queryResult = new QueryResult(title, matchContent, documentDAO.getScore());

            queryResultList.add(queryResult);

        } catch (IOException e) {
            log.error("unable to find any summary");
        } catch (InvalidTokenOffsetsException e) {
            log.error("unable to highlight content");
        }
    }

    private String getDocName(ScoreDoc scoreDoc) {
        String docName = "unable to find";
        try {
            Document document = this.searcher.doc(scoreDoc.doc);
            docName = document.get("name").substring(TITLE_START);
        } catch (IOException e) {
            log.error("unable to search document title");
        }

        return docName;
    }

    public static void getInstance(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
        if (luceneIndexReader == null)
            luceneIndexReader = new LuceneIndexReader(azureBlob, ramDirectory, analyzer);
    }

    public static LuceneIndexReader getInstance() {
        if (luceneIndexReader == null) {
            throw new NullPointerException("unable to initialize index reader");
        }

        return luceneIndexReader;
    }
}
