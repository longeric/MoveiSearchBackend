package edu.pitt.ir.controllers;

import edu.pitt.ir.repositories.TestRepository;
import edu.pitt.ir.services.TestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class controllerTests {
    public TestController testController;

    @Before
    public void setup() {
        TestRepository testRepository = new TestRepository();
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


}
