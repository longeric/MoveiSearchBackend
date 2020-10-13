package edu.pitt.ir.services;

import edu.pitt.ir.repositories.TestRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final TestRepositories testRepositories;

    @Autowired
    public TestService(final TestRepositories testRepositories) {
        this.testRepositories = testRepositories;
    }

    public String getTest() {
        return this.testRepositories.getString();
    }
}
