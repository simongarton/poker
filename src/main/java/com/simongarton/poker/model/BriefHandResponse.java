package com.simongarton.poker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BriefHandResponse {

    private double percentage;
    private String community;
    private List<BriefPlayerOutcome> players = new ArrayList<>();
    private BriefPlayerOutcome winner;
    private int communityCardCount;
    private boolean shouldFold;
    private boolean didWin;
}
