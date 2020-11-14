package edu.pitt.ir.repositories;

import edu.pitt.ir.helpers.LuenceHelper.LuenceIndexReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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

    @Test
    public void test_getList() {
        String content = "I am batman";
        int topK = 10;
        List<ScoreDoc> actualResult = this.testRepository.getQueryResultList(content, topK);

        LuenceIndexReader luenceIndexReader = LuenceIndexReader.getInstance(null, null);

        System.out.println(String.format("query is %s", content));

        actualResult.forEach(scoreDoc -> {
            try {
                Document document = luenceIndexReader.searcher.doc(scoreDoc.doc);
                System.out.println(String.format("the document name is %s, the score is %s",
                        document.get("name"), scoreDoc.score));
            } catch (IOException e) {
                System.out.println("unable to search");
            }
        });

        Assert.assertEquals(actualResult.size(), topK);
    }
}
