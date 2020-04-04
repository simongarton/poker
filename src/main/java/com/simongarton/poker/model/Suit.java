package com.simongarton.poker.model;

public enum Suit {
    CLUBS("Clubs", "C"),
    DIAMONDS("Diamonds", "D"),
    HEARTS("Hearts", "H"),
    Spades("Spades", "S");

    private String name;
    private String code;

    Suit(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
