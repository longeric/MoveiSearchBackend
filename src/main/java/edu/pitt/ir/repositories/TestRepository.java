package edu.pitt.ir.repositories;

import edu.pitt.ir.models.TestDAO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TestRepository {

    private final String[] results = {
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
            "What Men Want"
    };

    public String getString() {
        TestDAO test = new TestDAO();
        test.setTestMessage("test api works");
        return test.getTestMessage();
    }

    public List<String> getSearchResult(String searchName) {

        // should return a list based on score

        return Arrays.stream(this.results)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getRelevantResult(final String searchName) {

        String name = searchName.replaceAll("%20", " ").toLowerCase();
        // return a list which contains searchName to implement autocomplete
        return Arrays.stream(this.results)
                .filter( result -> result.toLowerCase().contains(name))
                .collect(Collectors.toList());
    }
}
