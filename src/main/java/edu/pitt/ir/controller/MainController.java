package edu.pitt.ir.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MainController {

    @GetMapping()
    public String test() {
        return "This api works";
    }
}
