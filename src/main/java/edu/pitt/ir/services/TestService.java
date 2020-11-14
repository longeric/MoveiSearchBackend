package edu.pitt.ir.services;

import edu.pitt.ir.helpers.LuenceHelper.LuenceIndexReader;
import edu.pitt.ir.repositories.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestService {
    private final TestRepository testRepositories;

    @Autowired
    public TestService(final TestRepository testRepositories) {
        this.testRepositories = testRepositories;
    }

    public String getTest() {
        return this.testRepositories.getString();
    }

    public List<String> getSearchResult(final String searchName) {
        return this.testRepositories.getSearchResult(searchName);
    }

    public List<String> getRelevantResult(final String searchName) {
        String[] searchTerms = searchName.split("%20");

        return Arrays.stream(searchTerms)
                .parallel()
                .map(String::toLowerCase)
                .map(this.testRepositories::getRelevantResult)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getQueryResultList(String content, int topK) {
        LuenceIndexReader luenceIndexReader = LuenceIndexReader.getInstance(null, null);
        content = content.replaceAll("%20", " ");
        return this.testRepositories.getQueryResultList(content, topK)
                .stream().parallel()
                .map(scoreDoc -> {
                    try {
                        Document document = luenceIndexReader.searcher.doc(scoreDoc.doc);
                        return document.get("name");
                    } catch (IOException e) {
                        throw new NullPointerException("unable to find document");
                    }
                }).collect(Collectors.toList());
    }
}
