package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Player {

    private int id;
    private Set<Card> cards = new HashSet<>();
    @Setter
    private ScoringCombination score;

    public Player(int id) {
        this.id = id;
    }
}
