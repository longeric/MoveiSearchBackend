package edu.pitt.ir.services;

import edu.pitt.ir.repositories.TestRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class serviceTests {
    private TestService testService;

    @Before
    public void setup() {
        TestRepository testRepository = new TestRepository();
        this.testService = new TestService(testRepository);
    }


    @Test
    public void test_getSearchResult () {
        int expectedLength = 12;

        List<String> actualResult = this.testService.getSearchResult("the");

        Assert.assertEquals(expectedLength, actualResult.size());
    }

    @Test
    public void test_getRelevantResult () {
        List<String> actualResult = this.testService.getRelevantResult("the%20Far%20lion");

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("The Lion King");
        expectedResult.add("Star Wars: The Rise of Skywalker");
        expectedResult.add("Spider-Man: Far From Home");
        Assert.assertEquals(expectedResult, actualResult);

    }
}
