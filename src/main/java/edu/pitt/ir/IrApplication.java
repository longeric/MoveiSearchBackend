package edu.pitt.ir;

import edu.pitt.ir.helpers.NormalizeText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class IrApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrApplication.class, args);
    }

    /**
     * run once before start server
     */
//    @Autowired
//    NormalizeText normalizeText;
//
//    @PostConstruct
//    public void normalize() {
//        this.normalizeText.normalize();
//    }
}
