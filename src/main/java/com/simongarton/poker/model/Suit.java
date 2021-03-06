package com.simongarton.poker.model;

import com.simongarton.poker.exceptions.CardFormatException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public static Suit findSuit(String s) {
        for (Suit suit : values()) {
            if (suit.getName().substring(0,1).equalsIgnoreCase(s)) {
                return suit;
            }
        }
        throw new CardFormatException("Could not find suit " + s);
    }

    public static List<Suit> getSuits() {
        return Arrays.asList(Suit.values());
    }

    public static Suit getRandomSuit() {
        Random r = new Random();
        return getSuits().get(r.nextInt(Suit.getSuits().size()));
    }

}
