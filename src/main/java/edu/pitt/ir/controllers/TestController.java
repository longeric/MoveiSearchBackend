package edu.pitt.ir.controllers;

import edu.pitt.ir.models.QueryResult;
import edu.pitt.ir.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class TestController {
    private final TestService testService;

    private static final int TOPK = 50;

    @Autowired
    public TestController(final TestService testService) {
        this.testService = testService;
    }

    @GetMapping()
    public String test() {
        return this.testService.getTest();
    }

    @GetMapping(value = "searchResult")
    public List<String> getSearchResult(@RequestParam("searchName") final String searchName) {
        return this.testService.getSearchResult(searchName);
    }

    @GetMapping(value = "relevant")
    public List<String> getRelevantResult(@RequestParam("searchName") final String searchName) {

        return this.testService.getRelevantResult(searchName);
    }

    @GetMapping(value = "testFileName")
    public List<String> getFileNameList(@RequestParam("searchName") final String content) {
        return this.testService.getFileNameList(content, TOPK);
    }

    @GetMapping(value = "testQueryResult")
    public List<QueryResult> getQueryResultList(@RequestParam("searchName") final String content) {
        return this.testService.getQueryResultList(content, TOPK);
    }

}
