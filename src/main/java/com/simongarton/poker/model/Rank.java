package com.simongarton.poker.model;

import com.simongarton.poker.exceptions.CardFormatException;
import lombok.Getter;

import java.util.*;

@Getter
public enum Rank {

    TWO("2", 2, "2"),
    THREE("3", 3, "3"),
    FOUR("4", 4, "4"),
    FIVE("5", 5, "5"),
    SIX("6", 6, "6"),
    SEVEN("7", 7, "7"),
    EIGHT("8", 8, "8"),
    NINE("9", 9, "9"),
    TEN("10", 10, "10"),
    JACK("Jack", 11, "J"),
    QUEEN("Queen", 12, "Q"),
    KING("King", 13, "K"),
    ACE("Ace", 14, "A");

    private String name;
    private int value;
    private String abbreviation;

    Rank(String name, int value, String abbreviation) {
        this.name = name;
        this.value = value;
        this.abbreviation = abbreviation;
    }

    public static List<Rank> getRanks() {
        List<Rank> ranks = new ArrayList<>(Arrays.asList(Rank.values()));
        ranks.sort(Comparator.comparing(Rank::getValue));
        return ranks;
    }

    public static Rank findRank(String r) {
        for (Rank rank : values()) {
            if (rank.getAbbreviation().equalsIgnoreCase(r)) {
                return rank;
            }
        }
        throw new CardFormatException("Could not find rank " + r);
    }

    public static Rank getRandomRank() {
        Random r = new Random();
        return getRanks().get(r.nextInt(Rank.getRanks().size()));
    }
}
