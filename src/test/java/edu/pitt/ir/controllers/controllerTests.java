package edu.pitt.ir.controllers;

import edu.pitt.ir.repositories.TestRepository;
import edu.pitt.ir.services.TestService;
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
public class controllerTests {
    public TestController testController;

    @Autowired
    public TestRepository testRepository;

    @Before
    public void setup() {
        TestService testService = new TestService(testRepository);
        this.testController = new TestController(testService);
    }

    @Test
    public void test_Test () {
        String expectedResult = "test api works";
        String actualResult = this.testController.test();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_GetSearchResult () {
        int expectedLength = 12;

        List<String> actualResult = this.testController.getSearchResult("the");

        Assert.assertEquals(expectedLength, actualResult.size());
    }

    @Test
    public void test_GetRelevantResult () {
        List<String> actualResult = this.testController.getRelevantResult("the%20Far%20lion");

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("The Lion King");
        expectedResult.add("Star Wars: The Rise of Skywalker");
        expectedResult.add("Spider-Man: Far From Home");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_GetQueryResult () {
        String content = "the%20Far%20lion";
        List<String> actualResult = this.testController.getFileNameList(content);

        Assert.assertEquals(actualResult.size(), 50);
    }


}
