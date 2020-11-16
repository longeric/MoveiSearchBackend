package edu.pitt.ir.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentDAO {
    private String title;
    private float score;
}
