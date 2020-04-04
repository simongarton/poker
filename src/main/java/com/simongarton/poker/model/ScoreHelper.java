package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ScoreHelper {

    private Map<Suit, Set<Card>> suitMap;
    private Map<Rank, Set<Card>> rankMap;
    private Set<Card> cards;

    public ScoreHelper(Set<Card> cards) {
        suitMap = new HashMap<>();
        rankMap = new HashMap<>();
        this.cards = new HashSet<>();
        this.cards.addAll(cards);

        for (Card card : cards) {
            if (!suitMap.containsKey(card.getSuit())) {
                suitMap.put(card.getSuit(), new HashSet<>());
            }
            Set<Card> suitCards = suitMap.get(card.getSuit());
            suitCards.add(card);
            suitMap.put(card.getSuit(), suitCards);

            if (!rankMap.containsKey(card.getRank())) {
                rankMap.put(card.getRank(), new HashSet<>());
            }
            Set<Card> rankCards = rankMap.get(card.getRank());
            rankCards.add(card);
            rankMap.put(card.getRank(), rankCards);
        }
    }

    public ScoringCombination getResult() {
        // TODO need wild card support
        // if (hasFiveOfAKind()) return ScoringCombination.FIVE_OF_A_KIND;
        if (hasStraightFlush()) return ScoringCombination.STRAIGHT_FLUSH;
        if (hasFourOfAKind()) return ScoringCombination.FOUR_OF_A_KIND;
        if (hasFullHouse()) return ScoringCombination.FULL_HOUSE;
        if (hasFlush()) return ScoringCombination.FLUSH;
        if (hasStraight()) return ScoringCombination.STRAIGHT;
        if (hasThreeOfAKind()) return ScoringCombination.THREE_OF_A_KIND;
        if (hasTwoPair()) return ScoringCombination.TWO_PAIR;
        if (hasOnePair()) return ScoringCombination.ONE_PAIR;
        return ScoringCombination.NO_PAIR;
    }

    private boolean hasStraight() {
        int[] counts = new int[14];
        for (Card card : cards) {
            int value = card.getRank().getValue() - 1;
            counts[value] = counts[value] + 1;
        }
        int inStraight = 0;
        for (int i = 0; i < 14; i++) {
            if (counts[i] > 0) {
                inStraight ++;
                if (inStraight == 5) {
                    return true;
                }
            } else {
                inStraight = 0;
            }
        }
        return false;
    }

    private boolean hasStraightFlush() {
        return hasStraight() && hasFlush();
    }

    private boolean hasFlush() {
        for (Map.Entry<Suit, Set<Card>> entry : suitMap.entrySet()) {
            if (entry.getValue().size() >= 5) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFullHouse() {
        int twos = 0;
        int threes = 0;
        for (Map.Entry<Suit, Set<Card>> entry : suitMap.entrySet()) {
            if (entry.getValue().size() == 2) {
                twos++;
            }
            if (entry.getValue().size() == 3) {
                threes++;
            }
        }
        return (twos >= 1) && (threes >= 1);
    }

    private boolean hasNOfAKind(int n) {
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() == n) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFourOfAKind() {
        return hasNOfAKind(4);
    }

    private boolean hasThreeOfAKind() {
        return hasNOfAKind(3);
    }

    private boolean hasTwoPair() {
        return countPairs() >= 2;
    }

    private boolean hasOnePair() {
        return countPairs() >= 1;
    }

    private int countPairs() {
        int pairs = 0;
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                pairs++;
            }
        }
        return pairs;
    }
}
