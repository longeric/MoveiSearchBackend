package edu.pitt.ir.services;

import edu.pitt.ir.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public List<String> getSearchResult (final String searchName) {
        return this.testRepositories.getSearchResult(searchName);
    }

    public List<String> getRelevantResult(final String searchName) {
        String[] searchTerms = searchName.split("%20");

        HashSet<String> relevantResults = new HashSet<>();
        for (String term: searchTerms) {
            relevantResults.addAll(this.testRepositories.getRelevantResult(term.toLowerCase()));
        }

        return new ArrayList<>(relevantResults);
    }
}
