package com.simongarton.poker.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BriefPlayerOutcome {

    private int id;
    private String cards;
    private String scoringCombination;
    private String scoringCards;

    public BriefPlayerOutcome(Player player) {
        this.id = player.getId();
        this.cards = player.getBriefCards();
        this.scoringCombination = player.getBestHand().getScoringCombination().getName();
        this.scoringCards = player.getBestHand().getBriefCards();
    }
}
