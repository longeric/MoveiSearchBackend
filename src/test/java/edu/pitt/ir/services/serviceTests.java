package edu.pitt.ir.services;

import edu.pitt.ir.repositories.TestRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class serviceTests {
    private TestService testService;

    @Autowired
    private TestRepository testRepository;

    @Before
    public void setup() {
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

    @Test
    public void test_getQueryResultList() {
        String content = "i%20am%20batman";
        int topK = 5;
        List<String> actualResult = this.testService.getQueryResultList(content, topK);

        System.out.println(actualResult.toString());

        Assert.assertEquals(actualResult.size(), topK);

    }
}
