package edu.pitt.ir.services;

import edu.pitt.ir.repositories.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String getTxtFile() {
        return this.testRepositories.getTxtFile();
    }
}
