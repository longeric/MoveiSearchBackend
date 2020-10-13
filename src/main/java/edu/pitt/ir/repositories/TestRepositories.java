package edu.pitt.ir.repositories;

import edu.pitt.ir.models.TestDAO;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositories {

    public String getString() {
        TestDAO test = new TestDAO();
        test.setTestMessage("test api works");
        return test.getTestMessage();
    }
}
