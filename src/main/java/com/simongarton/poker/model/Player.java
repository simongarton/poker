package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Player {

    private int id;
    private Set<Card> cards = new HashSet<>();
    @Setter
    private BestHand bestHand;

    public Player(int id) {
        this.id = id;
    }

    public List<Card> getSortedRemainingCards() {
        List<Card> cards = new ArrayList<>(bestHand.getRemainingCards());
        cards.sort(Comparator.comparing(c -> ((Card)c).getRank().getValue()).reversed());
        return cards;
    }

    public String getBriefCards() {
        String briefCards = "";
        for (Card card : cards) {
            briefCards = briefCards + card.getCaption() + " ";
        }
        return briefCards.trim();
    }
}
