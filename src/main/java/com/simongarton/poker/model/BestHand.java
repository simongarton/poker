package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BestHand {

    private ScoringCombination scoringCombination;
    private Set<Card> cards;
    private Set<Card> remainingCards;

    public BestHand(ScoringCombination scoringCombination, Set<Card> cards, Set<Card> remainingCards) {
        this.scoringCombination = scoringCombination;
        this.cards = new HashSet<>(cards);
        this.remainingCards = new HashSet<>(remainingCards);
    }
}
