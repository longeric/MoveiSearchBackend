package edu.pitt.ir.repositories;

import edu.pitt.ir.models.TestDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
                .parallel()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getRelevantResult(final String searchName) {

        // return a list which contains searchName to implement autocomplete
        return Arrays.stream(this.results)
                .parallel()
                .filter( result -> result.toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    public String getTxtFile() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("files//test.txt"));
        } catch (FileNotFoundException e) {
            log.error("cannot find file");
        }

        if (bufferedReader == null) return "file cannot find";

        String res = null;

        try {
            res = bufferedReader.readLine();
        } catch (IOException e) {
            log.error("cannot read file");
        }

        return res;
    }
}
