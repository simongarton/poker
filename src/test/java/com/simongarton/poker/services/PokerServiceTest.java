package com.simongarton.poker.services;

import com.simongarton.poker.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PokerServiceTest {

    @Autowired
    private PokerService pokerService;

    @Test
    void calculate() {
        Result result = pokerService.calculate(Collections.emptySet(), 2, Collections.emptySet());
        assertEquals(0, result.getValue());
    }

    @Test
    void getFullDeck() {
        Set<Card> deck = pokerService.getFullDeck();
        assertEquals(52, deck.size());
    }

    @Test
    void getDeck() {
        Set<Card> deck = pokerService.getDeck();
        assertEquals(52, deck.size());
    }

    @Test
    void testPlayerCardsOnly() {
        Set<Card> playerCards = new HashSet<>();
        Card card = new Card(Suit.CLUBS, Rank.THREE);
        playerCards.add(card);
        Result result = pokerService.calculate(playerCards, 2, Collections.emptySet());
        Set<Card> deck = pokerService.getDeck();
        assertEquals(51, deck.size());
        Player player1 = pokerService.getPlayers().get(0);
        assertNotNull(player1);
        assertEquals(1, player1.getCards().size());
        List<Card> hand = new ArrayList<>(player1.getCards());
        assertEquals(card, hand.get(0));
    }
}