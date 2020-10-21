package edu.pitt.ir.services;

import edu.pitt.ir.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return this.testRepositories.getRelevantResult(searchName);
    }
}
