package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class PlayerOutcome {

    private int id;
    private Set<Card> cards = new HashSet<>();
    private String scoringCombination;
    private Set<Card> scoringCards = new HashSet<>();

    public PlayerOutcome(Player player) {
        this.id = player.getId();
        this.cards.addAll(player.getCards());
        this.scoringCombination = player.getBestHand().getScoringCombination().getName();
        this.scoringCards.addAll(player.getBestHand().getCards());
    }
}
