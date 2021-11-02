package com.cs201.g1t1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlgoResult {
    private String categoryName;
    private long rangeSearchTime;
    private long categorySearchTime;
}
