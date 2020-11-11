package edu.pitt.ir.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class repoTests {

    @Autowired
    private TestRepository testRepository;

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
}
