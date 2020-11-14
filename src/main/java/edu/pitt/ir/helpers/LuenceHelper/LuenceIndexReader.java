package edu.pitt.ir.helpers.LuenceHelper;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class LuenceIndexReader {

    private IndexReader indexReader;
    private final IndexSearcher searcher;
    private final Analyzer analyzer;

    public LuenceIndexReader(RAMDirectory ramDirectory, Analyzer analyzer) {
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

    public void searchContent(String content, int topK) {
        QueryParser queryParser = new QueryParser("content", analyzer);

        try {
            Query query = queryParser.parse(content);
            TopDocs topDocs = this.searcher.search(query, topK);

            System.out.println(String.format("the total document numebr is %s", topDocs.totalHits));

            Arrays.stream(topDocs.scoreDocs).forEach(scoreDoc -> {
                try {
                    Document document = this.searcher.doc(scoreDoc.doc);
                    System.out.println(String.format("document name is %s, score is %s", document.get("name"), scoreDoc.score));
                } catch (IOException e) {
                    log.error("unable to find document");
                }
            });

        } catch (ParseException e) {
            log.error("unable to parse content");
        } catch (IOException e) {
            log.error("unable to search query");
        }

        this.close();

    }

    private void close() {
        try {
            this.indexReader.close();
        } catch (IOException e) {
            log.error("unable to close writer");
        } finally {
            System.gc();
        }
    }
}
