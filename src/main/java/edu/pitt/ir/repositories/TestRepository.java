package edu.pitt.ir.repositories;

import edu.pitt.ir.models.TestDAO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TestRepository {

    public String getString() {
        TestDAO test = new TestDAO();
        test.setTestMessage("test api works");
        return test.getTestMessage();
    }

    public List<String> getSearchResult() {
        String[] results = {
                "Avengers: Endgame",
                "The Lion King",
                "Star Wars: The Rise of Skywalker",
                "Frozen II",
                "Toy Story 4",
                "Captain Marvel",
                "Spider-Man: Far From Home",
                "Aladdin",
                "Joker",
                "John Wick: Chapter 3 – Parabellum",
                "A Dog's Way Home",
                "What Men Want"};

        return Arrays.stream(results).collect(Collectors.toList());
    }
}
