package com.simongarton.poker.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Card {

    public Suit suit;
    public Rank rank;

    public String getCaption() {
        return rank.getName() + " " + suit.getName();
    }
}
