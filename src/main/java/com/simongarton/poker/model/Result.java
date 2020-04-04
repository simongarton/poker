package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    private int value;
    private String explanation;

    public Result(ScoringCombination combination) {
        value = combination.getValue();
        explanation = combination.getName();
    }
}
