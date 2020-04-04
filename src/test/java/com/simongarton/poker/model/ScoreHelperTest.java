package com.simongarton.poker.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static com.simongarton.poker.model.ScoringCombination.ONE_PAIR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreHelperTest {

    @Test
    void testNoPair() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        Result result = scoreHelper.getResult();
        assertEquals(0, result.getValue());
        assertEquals("No pair", result.getExplanation());
    }

    @Test
    void testOnePair() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.SPADES, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        Result result = scoreHelper.getResult();
        assertEquals(ONE_PAIR, result);
    }

    @Test
    void testTwoPair() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.SPADES, Rank.THREE));
        cards.add(new Card(Suit.SPADES, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        Result result = scoreHelper.getResult();
        assertEquals(2, result.getValue());
        assertEquals("Two pairs", result.getExplanation());
    }

    @Test
    void testThreeOfAKind() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.HEARTS, Rank.FOUR));
        cards.add(new Card(Suit.SPADES, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        Result result = scoreHelper.getResult();
        assertEquals(2, result.getValue());
        assertEquals("Two pairs", result.getExplanation());
    }
}