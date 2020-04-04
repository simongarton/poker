package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Player {

    private int id;
    private Set<Card> cards = new HashSet<>();
    @Setter
    private BestHand bestHand;

    public Player(int id) {
        this.id = id;
    }
}
