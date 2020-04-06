package com.simongarton.poker.services;

import com.simongarton.poker.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PokerServiceTest {

    @Autowired
    private PokerService pokerService;

//    @Test
//    void mapOutTwoCards() {
//        Set<Card> cards = new HashSet<>();
//        Card card1;
//        Card card2;
//        List<String> lines = new ArrayList<>();
//        String line = ",";
//        for (Rank r1 : Rank.values()) {
//            line = line + r1.getName() + ",";
//        }
//        lines.add(line);
//
//        for (Rank r1 : Rank.values()) {
//            line = r1.getName() + ",";
//            for (Rank r2 : Rank.values()) {
//                card1 = new Card(Suit.CLUBS, r1);
//                card2 = new Card(Suit.SPADES, r2);
//                cards.clear();
//                cards.add(card1);
//                cards.add(card2);
//                double percentage = pokerService.getWinningPercentage(cards, 5, 3, 10000);
//                System.out.println(card1.getCaption() + " + " + card2.getCaption() + " = " + percentage);
//                line = line + percentage + ",";
//            }
//            lines.add(line);
//        }
//        try {
//            Files.write(Paths.get("two_cards.csv"), lines, Charset.defaultCharset());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

    @Test
    void testTieBreakerOnePair() {
        Set<Card> player1Cards = new HashSet<>();
        Set<Card> player2Cards = new HashSet<>();
        Set<Card> communityCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        Card card3 = new Card(Suit.SPADES, Rank.QUEEN);
        Card card4 = new Card(Suit.HEARTS, Rank.THREE);
        Card card5 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card card6 = new Card(Suit.HEARTS, Rank.JACK);
        Card card7 = new Card(Suit.HEARTS, Rank.KING);
        player1Cards.add(card1);
        player1Cards.add(card2);
        player1Cards.add(card3);
        player2Cards.add(card4);
        player2Cards.add(card5);
        player2Cards.add(card6);
        List<Set<Card>> players = new ArrayList<>();
        players.add(player1Cards);
        players.add(player2Cards);
        Player winner = pokerService.getWinner(players, communityCards);
        assertNotNull(winner);
        assertEquals(1, winner.getId());
        player2Cards.clear();
        player2Cards.add(card4);
        player2Cards.add(card5);
        player2Cards.add(card7);
        players = new ArrayList<>();
        players.add(player1Cards);
        players.add(player2Cards);
        winner = pokerService.getWinner(players, communityCards);

        assertNotNull(winner);
        assertEquals(2, winner.getId());
    }

    @Test
    void testTieBreakerOnePairDifferent() {
        Set<Card> player1Cards = new HashSet<>();
        Set<Card> player2Cards = new HashSet<>();
        Set<Card> communityCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        Card card3 = new Card(Suit.SPADES, Rank.QUEEN);
        Card card4 = new Card(Suit.HEARTS, Rank.FOUR);
        Card card5 = new Card(Suit.DIAMONDS, Rank.FOUR);
        Card card6 = new Card(Suit.HEARTS, Rank.KING);
        player1Cards.add(card1);
        player1Cards.add(card2);
        player1Cards.add(card3);
        player2Cards.add(card4);
        player2Cards.add(card5);
        player2Cards.add(card6);
        List<Set<Card>> players = new ArrayList<>();
        players.add(player1Cards);
        players.add(player2Cards);
        Player winner = pokerService.getWinner(players, communityCards);
        assertNotNull(winner);
        assertEquals(2, winner.getId());
    }

    @Test
    void testTieBreakerTwoPairDifferent() {
        Set<Card> player1Cards = new HashSet<>();
        Set<Card> player2Cards = new HashSet<>();
        Set<Card> communityCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        Card card3 = new Card(Suit.CLUBS, Rank.FOUR);
        Card card4 = new Card(Suit.SPADES, Rank.FOUR);
        Card card5 = new Card(Suit.SPADES, Rank.QUEEN);
        Card card6 = new Card(Suit.HEARTS, Rank.FOUR);
        Card card7 = new Card(Suit.DIAMONDS, Rank.FOUR);
        Card card8 = new Card(Suit.HEARTS, Rank.FIVE);
        Card card9 = new Card(Suit.DIAMONDS, Rank.FIVE);
        Card card10 = new Card(Suit.HEARTS, Rank.THREE);
        player1Cards.add(card1);
        player1Cards.add(card2);
        player1Cards.add(card3);
        player1Cards.add(card4);
        player1Cards.add(card5);
        player2Cards.add(card6);
        player2Cards.add(card7);
        player2Cards.add(card8);
        player2Cards.add(card9);
        player2Cards.add(card10);
        List<Set<Card>> players = new ArrayList<>();
        players.add(player1Cards);
        players.add(player2Cards);
        Player winner = pokerService.getWinner(players, communityCards);
        assertNotNull(winner);
        assertEquals(1, winner.getId());
    }

    @Test
    void testTieBreakerOnePairTwoLevel() {
        Set<Card> player1Cards = new HashSet<>();
        Set<Card> player2Cards = new HashSet<>();
        Set<Card> communityCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        Card card3 = new Card(Suit.SPADES, Rank.QUEEN);
        Card card4 = new Card(Suit.HEARTS, Rank.THREE);
        Card card5 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card card6 = new Card(Suit.HEARTS, Rank.QUEEN);
        Card card7 = new Card(Suit.HEARTS, Rank.KING);
        Card card8 = new Card(Suit.HEARTS, Rank.JACK);
        player1Cards.add(card1);
        player1Cards.add(card2);
        player1Cards.add(card3);
        player1Cards.add(card8);
        player2Cards.add(card4);
        player2Cards.add(card5);
        player2Cards.add(card6);
        player2Cards.add(card7);
        List<Set<Card>> players = new ArrayList<>();
        players.add(player1Cards);
        players.add(player2Cards);
        Player winner = pokerService.getWinner(players, communityCards);
        assertNotNull(winner);
        assertEquals(2, winner.getId());
    }

    @Test
    void testTieBreakerOnePairSplit() {
        Set<Card> player1Cards = new HashSet<>();
        Set<Card> player2Cards = new HashSet<>();
        Set<Card> communityCards = new HashSet<>();
        Card card1 = new Card(Suit.CLUBS, Rank.THREE);
        Card card2 = new Card(Suit.SPADES, Rank.THREE);
        Card card3 = new Card(Suit.SPADES, Rank.QUEEN);
        Card card4 = new Card(Suit.HEARTS, Rank.THREE);
        Card card5 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card card6 = new Card(Suit.HEARTS, Rank.QUEEN);
        Card card7 = new Card(Suit.HEARTS, Rank.KING);
        Card card8 = new Card(Suit.DIAMONDS, Rank.KING);
        player1Cards.add(card1);
        player1Cards.add(card2);
        player1Cards.add(card3);
        player1Cards.add(card8);
        player2Cards.add(card4);
        player2Cards.add(card5);
        player2Cards.add(card6);
        player2Cards.add(card7);
        List<Set<Card>> players = new ArrayList<>();
        players.add(player1Cards);
        players.add(player2Cards);
        Player winner = pokerService.getWinner(players, communityCards);
        assertNull(winner);
    }

    @Test
    void getHandResponse() {
        int playerCount = 2;
        int iterations = 100;
        String cards = "JS,JH";
        String communityCards = "KH,KS,KD";
        HandResponse handResponse = pokerService.getHandResponse(cards, communityCards, playerCount, iterations );
        assertNotNull(handResponse);
        assertEquals("Full House", handResponse.getScoringCombination());
    }
}