package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BestHand {

    private ScoringCombination scoringCombination;
    private Set<Card> cards = new HashSet<>();
}
