package com.simongarton.poker.model;

import com.simongarton.poker.exceptions.CardFormatException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Card {

    public Suit suit;
    public Rank rank;

    public Card(String card) {
        if (card.length() == 1) {
            throw new CardFormatException("Card length " + card.length() + " is not valid.");
        }
        if (card.length() > 3) {
            throw new CardFormatException("Card length " + card.length() + " is not valid.");
        }
        int n = card.length() == 2 ? 1 : 2;
        String r = card.substring(0, n);
        String s = card.substring(n, n + 1);
        rank = Rank.findRank(r);
        suit = Suit.findSuit(s);
    }

    public String getCaption() {
        return rank.getName() + " " + suit.getName();
    }

    public static Card getRandomCard() {
        Suit suit = Suit.getRandomSuit();
        Rank rank = Rank.getRandomRank();
        return new Card(suit, rank);
    }

    public String getAbbreviation() {
        return rank.getAbbreviation() + suit.getCode();
    }
}
