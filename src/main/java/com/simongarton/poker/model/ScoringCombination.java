package com.simongarton.poker.model;

import lombok.Getter;

@Getter
public enum ScoringCombination {

    NO_PAIR("No pair", 0),
    ONE_PAIR("One pair", 1),
    TWO_PAIR("Two pairs", 2),
    THREE_OF_A_KIND("Three of a kind", 3),
    STRAIGHT("Straight", 4),
    FLUSH("Flush", 5),
    FULL_HOUSE("Full House", 6),
    FOUR_OF_A_KIND("Four of a kind", 7),
    STRAIGHT_FLUSH("Straight flush", 8),
    FIVE_OF_A_KIND("Five of a kind", 9);

    private String name;
    private int value;

    ScoringCombination(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
