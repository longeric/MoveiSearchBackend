package edu.pitt.ir.repositories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class repoTests {

    private TestRepository testRepository;

    @Before
    public void setup() {
        this.testRepository = new TestRepository();
    }

    @Test
    public void test_GetString () {
        String actualResult = this.testRepository.getString();
        String expectedResult = "test api works";

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_GetRelevantResult_the () {
        List<String> actualResult = this.testRepository.getRelevantResult("the");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("The Lion King");
        expectedResult.add("Star Wars: The Rise of Skywalker");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_GetRelevantResult_far () {
        List<String> actualResult = this.testRepository.getRelevantResult("far");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Spider-Man: Far From Home");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_GetTxtFile () {
        String actualResult = this.testRepository.getTxtFile();
        String expectedResult = "this is a test file";

        Assert.assertEquals(expectedResult, actualResult);
    }
}
