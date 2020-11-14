package edu.pitt.ir.helpers.LuenceHelper;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LuenceIndexReader {

    private IndexReader indexReader;
    public final IndexSearcher searcher;
    private final Analyzer analyzer;

    private static LuenceIndexReader luenceIndexReader = null;

    private LuenceIndexReader(RAMDirectory ramDirectory, Analyzer analyzer) {
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
            Query query = queryParser.parse(content);
            TopDocs topDocs = this.searcher.search(query, topK);
            return Arrays.stream(topDocs.scoreDocs).collect(Collectors.toList());

        } catch (ParseException e) {
            log.error("unable to parse content");
        } catch (IOException e) {
            log.error("unable to search query");
        }
        return null;
    }

    public static LuenceIndexReader getInstance(RAMDirectory ramDirectory, Analyzer analyzer) {
        if (luenceIndexReader == null)
            luenceIndexReader = new LuenceIndexReader(ramDirectory, analyzer);
        return luenceIndexReader;
    }
}
