package com.simongarton.poker.services;

import com.simongarton.poker.model.Card;
import com.simongarton.poker.model.Player;
import com.simongarton.poker.model.Rank;
import com.simongarton.poker.model.Suit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PokerServiceTest {

    @Autowired
    private PokerService pokerService;

    @Test
    void empty() {
        Player winner = pokerService.getWinner(Collections.emptySet(), 2, Collections.emptySet());
        assertNotNull(winner);
    }

    @Test
    void playGameNoCards() {
        Set<Card> cards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        cards.add(card1);
        cards.add(card2);
        Player winner = pokerService.getWinner(cards, 2, Collections.emptySet());
        assertNotNull(winner);
    }

    @Test
    void playGameSomeCards() {
        Set<Card> cards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        cards.add(card1);
        cards.add(card2);
        Player winner = pokerService.getWinner(cards, 2, 3);
        assertNotNull(winner);
    }

    @Test
    void playSimulation() {
        Set<Card> cards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.JACK);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        cards.add(card1);
        cards.add(card2);
        double percentage = pokerService.getWinningPercentage(cards, 2, 3, 10000);
        System.out.println(percentage);
    }

    @Test
    void getFullDeck() {
        Set<Card> deck = pokerService.getFullDeck();
        assertEquals(52, deck.size());
    }

    @Test
    void testPlayerCardsOnly() {
        Set<Card> playerCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        playerCards.add(card1);
        playerCards.add(card2);
        pokerService.getWinner(playerCards, 2, Collections.emptySet());
        List<Card> deck = pokerService.getDeck();
        assertEquals(50, deck.size());
        Player player1 = pokerService.getPlayers().get(0);
        assertNotNull(player1);
        assertEquals(2, player1.getCards().size());
        assertTrue(player1.getCards().contains(card1));
        assertTrue(player1.getCards().contains(card2));
    }

    @Test
    void testCommunity() {
        Set<Card> playerCards = new HashSet<>();
        Set<Card> communityCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        Card card3 = new Card(Suit.HEARTS, Rank.JACK);
        Card card4 = new Card(Suit.HEARTS, Rank.QUEEN);
        Card card5 = new Card(Suit.HEARTS, Rank.KING);
        playerCards.add(card1);
        playerCards.add(card2);
        communityCards.add(card3);
        communityCards.add(card4);
        communityCards.add(card5);
        pokerService.getWinner(playerCards, 2, communityCards);
        List<Card> deck = pokerService.getDeck();
        assertEquals(47, deck.size());
        Player player1 = pokerService.getPlayers().get(0);
        assertNotNull(player1);
        assertEquals(2, player1.getCards().size());
        assertTrue(player1.getCards().contains(card1));
        assertTrue(player1.getCards().contains(card2));
        assertEquals(2, player1.getCards().size());
        assertEquals(3, pokerService.getCommunityCards().size());
    }
}