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

    public ScoreHelper(Set<Card> cards) {
        suitMap = new HashMap<>();
        rankMap = new HashMap<>();

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

    public Result getResult() {
        if (hasFiveOfAKind()) return new Result(ScoringCombination.FIVE_OF_A_KIND);
        if (hasStraightFlush()) return new Result(ScoringCombination.STRAIGHT_FLUSH);
        if (hasFourOfAKind()) return new Result(ScoringCombination.FOUR_OF_A_KIND);
        if (hasFullHouse()) return new Result(ScoringCombination.FULL_HOUSE);
        if (hasFlush()) return new Result(ScoringCombination.FLUSH);
        if (hasStraight()) return new Result(ScoringCombination.STRAIGHT);
        if (hasThreeOfAKind()) return new Result(ScoringCombination.THREE_OF_A_KIND);
        if (hasTwoPair()) return new Result(ScoringCombination.TWO_PAIR);
        if (hasOnePair()) return new Result(ScoringCombination.ONE_PAIR);
        return new Result(ScoringCombination.NO_PAIR);
    }

    private boolean hasStraight() {
        return false;
    }

    private boolean hasStraightFlush() {
        return false;
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
        int groups = 0;
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() > 2) {
                groups++;
            }
        }
        return groups >= 2;
    }

    private boolean hasFiveOfAKind() {
        return hasNOfAKind(5);
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
