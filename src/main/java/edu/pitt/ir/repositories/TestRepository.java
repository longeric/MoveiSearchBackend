package edu.pitt.ir.repositories;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import edu.pitt.ir.models.TestDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TestRepository {

    @Value("${spring.azure.connectionKey}")
    private String connectionString;

    @Value("${spring.azure.containerName}")
    private String containerName;

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

        AzureBlob azureBlob = new AzureBlob(this.connectionString, this.containerName);

        return azureBlob.readFiles("15minutes_dialog.txt");

    }

    public void getList() {
        AzureBlob azureBlob = new AzureBlob(this.connectionString, this.containerName);

        System.out.println(azureBlob.getAllFileNames().size());
    }
}
