package edu.pitt.ir.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryResult {

    private String title;
    private String content;
    private float score;

    public String toString() {
        return String.format("the title is %s, the score is %s, the content is %s \n",
                this.getTitle(), this.getScore(), this.getContent());
    }

}
