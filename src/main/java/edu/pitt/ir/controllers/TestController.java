package edu.pitt.ir.controllers;

import edu.pitt.ir.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class TestController {
    private final TestService testService;

    @Autowired
    public TestController(final TestService testService) {
        this.testService = testService;
    }

    @GetMapping()
    public String test() {
        return this.testService.getTest();
    }

    @GetMapping(value = "searchResult")
    public List<String> getSearchResult() {
        return this.testService.getSearchResult();
    }

}
