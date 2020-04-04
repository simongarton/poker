package com.simongarton.poker.model;

import lombok.Getter;

@Getter
public enum Suit {
    CLUBS("Clubs", "C"),
    DIAMONDS("Diamonds", "D"),
    HEARTS("Hearts", "H"),
    SPADES("Spades", "S");

    private String name;
    private String code;

    Suit(String name, String code) {
        this.name = name;
        this.code = code;
    }

}
