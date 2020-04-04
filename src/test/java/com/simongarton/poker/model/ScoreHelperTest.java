package com.simongarton.poker.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.simongarton.poker.model.ScoringCombination.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreHelperTest {

    @Test
    void testNoPair() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(NO_PAIR, result);
    }

    @Test
    void testOnePair() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.SPADES, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
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
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(TWO_PAIR, result);
    }

    @Test
    void testThreeOfAKind() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.HEARTS, Rank.FOUR));
        cards.add(new Card(Suit.SPADES, Rank.FOUR));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(THREE_OF_A_KIND, result);
    }

    @Test
    void testStraight() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.HEARTS, Rank.FIVE));
        cards.add(new Card(Suit.SPADES, Rank.SIX));
        cards.add(new Card(Suit.SPADES, Rank.SEVEN));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(STRAIGHT, result);
    }

    @Test
    void testFlush() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.CLUBS, Rank.EIGHT));
        cards.add(new Card(Suit.CLUBS, Rank.SIX));
        cards.add(new Card(Suit.CLUBS, Rank.SEVEN));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(FLUSH, result);
    }

    @Test
    void testFullHouse() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.SPADES, Rank.EIGHT));
        cards.add(new Card(Suit.SPADES, Rank.SIX));
        cards.add(new Card(Suit.SPADES, Rank.SEVEN));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(FULL_HOUSE, result);
    }

    @Test
    void testFourOfAKind() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.DIAMONDS, Rank.THREE));
        cards.add(new Card(Suit.HEARTS, Rank.THREE));
        cards.add(new Card(Suit.SPADES, Rank.THREE));
        cards.add(new Card(Suit.SPADES, Rank.SEVEN));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(FOUR_OF_A_KIND, result);
    }

    @Test
    void testStraightFlush() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(Suit.CLUBS, Rank.THREE));
        cards.add(new Card(Suit.CLUBS, Rank.FOUR));
        cards.add(new Card(Suit.CLUBS, Rank.FIVE));
        cards.add(new Card(Suit.CLUBS, Rank.SIX));
        cards.add(new Card(Suit.CLUBS, Rank.SEVEN));
        ScoreHelper scoreHelper = new ScoreHelper(cards);
        ScoringCombination result = scoreHelper.getResult().getScoringCombination();;
        assertEquals(STRAIGHT_FLUSH, result);
    }
}