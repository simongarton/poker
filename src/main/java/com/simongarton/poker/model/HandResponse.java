package com.simongarton.poker.model;

import lombok.Data;

import java.util.List;

@Data
public class HandResponse {

    private double percentage;
    private List<Card> hand;
    private List<Card> community;
    private int playerCount;
    private int communityCardCount;
    private int iterations;
    private String scoringCombination;
    private boolean shouldFold;
}
