package com.simongarton.poker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HandResponse {

    private double percentage;
    private List<Card> community;
    private List<PlayerOutcome> players = new ArrayList<>();
    private PlayerOutcome winner;
    private int communityCardCount;
    private boolean shouldFold;
    private boolean didWin;
}
