package com.simongarton.poker.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
public enum Rank {

    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("Jack", 11),
    QUEEN("Queen", 12),
    KING("King", 13),
    ACE("Ace", 14);

    private String name;
    private int value;

    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static List<Rank> getRanks() {
        List<Rank> ranks = new ArrayList<>(Arrays.asList(Rank.values()));
        ranks.sort(Comparator.comparing(Rank::getValue));
        return ranks;
    }
}
