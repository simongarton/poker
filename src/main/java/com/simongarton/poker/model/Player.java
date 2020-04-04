package com.simongarton.poker.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Player {

    private int id;
    private Set<Card> cards = new HashSet<>();

    public Player(int id) {
        this.id = id;
    }
}
