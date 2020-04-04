package com.simongarton.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

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

    public BestHand getResult() {
        // TODO need wild card support
        // if (hasFiveOfAKind()) return ScoringCombination.FIVE_OF_A_KIND;
        Set<Card> cards = getStraightFlush();
        if (cards != null) return new BestHand(ScoringCombination.STRAIGHT_FLUSH, cards, getRemainingCards(cards));
        cards = getFourOfAKind();
        if (cards != null) return new BestHand(ScoringCombination.FOUR_OF_A_KIND, cards, getRemainingCards(cards));
        cards = getFullHouse();
        if (cards != null) return new BestHand(ScoringCombination.FULL_HOUSE, cards, getRemainingCards(cards));
        cards = getFlush();
        if (cards != null) return new BestHand(ScoringCombination.FLUSH, cards, getRemainingCards(cards));
        cards = getStraight();
        if (cards != null) return new BestHand(ScoringCombination.STRAIGHT, cards, getRemainingCards(cards));
        cards = getThreeOfAKind();
        if (cards != null) return new BestHand(ScoringCombination.THREE_OF_A_KIND, cards, getRemainingCards(cards));
        cards = getTwoPair();
        if (cards != null) return new BestHand(ScoringCombination.TWO_PAIR, cards, getRemainingCards(cards));
        cards = getOnePair();
        if (cards != null) return new BestHand(ScoringCombination.ONE_PAIR, cards, getRemainingCards(cards));
        return new BestHand(ScoringCombination.NO_PAIR, Collections.emptySet(), this.cards);
    }

    private Set<Card> getStraightFlush() {
        if (!hasStraightFlush()) {
            return null;
        }
        return getStraight();
    }

    private Set<Card> getOnePair() {
        if (!hasOnePair()) {
            return null;
        }
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                return new HashSet<>(entry.getValue());
            }
        }
        throw new RuntimeException("Nope");
    }

    private Set<Card> getTwoPair() {
        if (!hasTwoPair()) {
            return null;
        }
        Set<Card> cards = new HashSet<>();
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                cards.addAll(entry.getValue());
                if (cards.size() == 4) {
                    return cards;
                }
            }
        }
        throw new RuntimeException("Nope");
    }

    private Set<Card> getThreeOfAKind() {
        if (!hasThreeOfAKind()) {
            return null;
        }
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() == 3) {
                return new HashSet<>(entry.getValue());
            }
        }
        throw new RuntimeException("Nope");
    }

    private Set<Card> getFourOfAKind() {
        if (!hasFourOfAKind()) {
            return null;
        }
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() == 4) {
                return new HashSet<>(entry.getValue());
            }
        }
        throw new RuntimeException("Nope");
    }

    private Set<Card> getStraight() {
        if (!hasStraight()) {
            return null;
        }
        int[] counts = new int[14];
        Map<Integer, List<Card>> cardMap = new HashMap<>();
        for (Card card : cards) {
            int value = card.getRank().getValue() - 1;
            List<Card> cards = cardMap.get(value);
            if (cards == null) cards = new ArrayList<>();
            cards.add(card);
            cardMap.put(value, cards);
            counts[value] = counts[value] + 1;
        }
        Set<Card> cards = new HashSet<>();
        int inStraight = 0;
        for (int i = 0; i < 14; i++) {
            if (counts[i] > 0) {
                inStraight++;
                cards.add(cardMap.get(i).get(0));
                if (inStraight == 5) {
                    return cards;
                }
            } else {
                inStraight = 0;
                cards.clear();
            }
        }
        throw new RuntimeException("Nope");
    }

    private Set<Card> getFlush() {
        if (!hasFlush()) {
            return null;
        }
        for (Map.Entry<Suit, Set<Card>> entry : suitMap.entrySet()) {
            if (entry.getValue().size() >= 5) {
                List<Card> cards = new ArrayList<>(entry.getValue());
                cards.sort(Comparator.comparing(c -> ((Card) c).getRank().getValue()).reversed());
                Set<Card> bestCards = new HashSet<>();
                for (int i = 0; i < 5; i++) {
                    bestCards.add(cards.get(i));
                }
                return bestCards;
            }
        }
        throw new RuntimeException("Nope");
    }

    private Set<Card> getFullHouse() {
        if (!hasFullHouse()) {
            return null;
        }
        // TODO I need to pick the higher ones
        int rank2 = 0;
        int rank3 = 0;
        Set<Card> cards2 = new HashSet<>();
        Set<Card> cards3 = new HashSet<>();
        for (Map.Entry<Rank, Set<Card>> entry : rankMap.entrySet()) {
            if (entry.getValue().size() == 2) {
                if (entry.getKey().getValue() > rank2) {
                    rank2 = entry.getKey().getValue();
                    cards2 = new HashSet<>(entry.getValue());
                }
            }
            if (entry.getValue().size() == 3) {
                if (entry.getKey().getValue() > rank3) {
                    rank3 = entry.getKey().getValue();
                    cards3 = new HashSet<>(entry.getValue());
                }
            }
        }
        if (cards2.isEmpty()) throw new RuntimeException("Nope");
        if (cards3.isEmpty()) throw new RuntimeException("Nope");
        Set<Card> cards = new HashSet<>(cards2);
        cards.addAll(cards3);
        return cards;
    }

    public Set<Card> getRemainingCards(Set<Card> winningCards) {
        Set<Card> remainingCards = new HashSet<>(cards);
        winningCards.forEach(remainingCards::remove);
        return remainingCards;
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
                inStraight++;
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
