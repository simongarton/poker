package com.simongarton.poker.services;

import com.simongarton.poker.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.simongarton.poker.model.Suit.*;

@Service
public class PokerService {

    public Suit[] SUITS = new Suit[]{CLUBS, DIAMONDS, HEARTS, SPADES};

    private List<Card> deck;
    private Set<Card> communityCards = new HashSet<>();
    private List<Player> players = new ArrayList<>();
    private Player winner;
    private boolean debug = false;

    public double getWinningPercentage(Set<Card> cards, int playerCount, int communityCardCount, int iterations) {

        double wins = 0;
        List<String> tracking = new ArrayList<>();
        for (int i = 1; i <= iterations; i++) {
            Player p = getWinner(cards, playerCount, communityCardCount);
            if (p == null) {
                wins += 0.5;
            } else {
                if (p.getId() == 1) {
                    wins++;
                }
            }
            String line = i + "," + String.format("%.0f", wins) + "," + String.format("%.3f", wins / i);
            if (debug) tracking.add(line);
        }
        if (debug) {
            try {
                Files.write(Paths.get("output.csv"),tracking, Charset.defaultCharset()); //tracking.forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1.0 * wins / iterations;
    }

    public Player getWinner(Set<Card> cards, int playerCount, Set<Card> communityCards) {
        setupPlayers(playerCount);
        setupDeck();

        setupPlayer1(cards);
        setupCommunityCards(communityCards);
        setupOtherPlayers();

        scoreHands();
        winner = getWinningPlayer();

        if (debug) debugHands();
        return winner;
    }

    private void setupDeck() {
        deck = new ArrayList<>(getFullDeck());
        Collections.shuffle(deck, new Random());
    }

    public Player getWinner(Set<Card> cards, int playerCount, int communityCardCount) {
        setupPlayers(playerCount);
        setupDeck();

        setupPlayer1(cards);
        setupCommunityCards(communityCardCount);
        setupOtherPlayers();

        scoreHands();
        winner = getWinningPlayer();

        if (debug) debugHands();
        return winner;
    }

    private Player getWinningPlayer() {
        List<Player> somePlayers = new ArrayList<>(players);
        somePlayers.sort(Comparator.comparing(p -> ((Player) p).getBestHand().getScoringCombination().getValue()).reversed());
        int topScore = somePlayers.get(0).getBestHand().getScoringCombination().getValue();
        Iterator<Player> iterator = somePlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getBestHand().getScoringCombination().getValue() != topScore) {
                iterator.remove();
                ;
            }
        }
        if (somePlayers.size() == 1) {
            return somePlayers.get(0);
        }
        return tieBreak(somePlayers);
    }

    private Player tieBreak(List<Player> somePlayers) {
        ScoringCombination sc = somePlayers.get(0).getBestHand().getScoringCombination();
        switch (sc) {
            case NO_PAIR:
                return tieBreakNoPair(somePlayers);
            case ONE_PAIR:
                return tieBreakOnePair(somePlayers);
            case TWO_PAIR:
                return tieBreakTwoPair(somePlayers);
            case THREE_OF_A_KIND:
                return tieBreakThreeOfAKind(somePlayers);
            case STRAIGHT:
                return tieBreakStraight(somePlayers);
            case FLUSH:
                return tieBreakFlush(somePlayers);
            case FULL_HOUSE:
                return tieBreakFullHouse(somePlayers);
            case FOUR_OF_A_KIND:
                return tieBreakFourOfAKind(somePlayers);
            case STRAIGHT_FLUSH:
                return tieBreakStraightFlush(somePlayers);
            default:
                throw new RuntimeException("No tiebreak");
        }
    }

    private Player tieBreakFullHouse(List<Player> somePlayers) {
        return tieBreakTwoLayers(somePlayers);
    }

    private static class Pair {
        private int high;
        private int low;
    }

    private Player tieBreakTwoPair(List<Player> somePlayers) {
        return tieBreakTwoLayers(somePlayers);
    }

    private Player tieBreakTwoLayers(List<Player> somePlayers) {
        Map<Integer, Pair> pairMap = new HashMap<>();
        for (Player p : somePlayers) {
            Pair pair = new Pair();
            List<Card> sortedCards = new ArrayList<>(p.getCards());
            sortedCards.sort(Comparator.comparing(c -> ((Card) c).getRank().getValue()));
            pair.low = sortedCards.get(0).getRank().getValue();
            pair.high = sortedCards.get(sortedCards.size() - 1).getRank().getValue();
            pairMap.put(p.getId(), pair);
        }
        Pair bestPair = null;
        for (Map.Entry<Integer, Pair> entry : pairMap.entrySet()) {
            if (bestPair == null) {
                bestPair = entry.getValue();
                continue;
            }
            if (bestPair.high > entry.getValue().high) {
                continue;
            }
            if (bestPair.low > entry.getValue().low) {
                continue;
            }
            bestPair = entry.getValue();
        }
        List<Player> tieBreakPlayers = new ArrayList<>(somePlayers);
        Iterator<Player> iterator = tieBreakPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            Pair pair = pairMap.get(p.getId());
            if (pair.high < bestPair.high) {
                iterator.remove();
                continue;
            }
            if (pair.low < bestPair.low) {
                iterator.remove();
                continue;
            }
        }
        if (tieBreakPlayers.size() == 1) {
            return tieBreakPlayers.get(0);
        }
        if (tieBreakPlayers.size() == 0) {
            throw new RuntimeException("Ran out of players");
        }
        return tieBreakHighestKicker(tieBreakPlayers, 0);
    }

    private Player tieBreakOnePair(List<Player> somePlayers) {
        return tieBreakPlayedCards(somePlayers);
    }

    private Player tieBreakThreeOfAKind(List<Player> somePlayers) {
        return tieBreakPlayedCards(somePlayers);
    }

    private Player tieBreakFourOfAKind(List<Player> somePlayers) {
        return tieBreakPlayedCards(somePlayers);
    }

    private Player tieBreakStraight(List<Player> somePlayers) {
        return tieBreakPlayedCards(somePlayers);
    }

    private Player tieBreakStraightFlush(List<Player> somePlayers) {
        return tieBreakPlayedCards(somePlayers);
    }

    private Player tieBreakFlush(List<Player> somePlayers) {
        return tieBreakPlayedCards(somePlayers);
    }

    private Player tieBreakPlayedCards(List<Player> somePlayers) {
        List<Player> tieBreakPlayers = new ArrayList<>(somePlayers);
        int highCard = 0;
        for (Player p : tieBreakPlayers) {
            for (Card c : p.getCards()) {
                int high = c.getRank().getValue();
                if (high > highCard) {
                    highCard = high;
                }
            }
        }
        Iterator<Player> iterator = tieBreakPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            boolean hasHighCard = false;
            for (Card c : p.getCards()) {
                if (c.getRank().getValue() == highCard) {
                    hasHighCard = true;
                }
            }
            if (!hasHighCard) {
                iterator.remove();
            }
        }
        if (tieBreakPlayers.size() == 1) {
            return tieBreakPlayers.get(0);
        }
        if (tieBreakPlayers.size() == 0) {
            throw new RuntimeException("Ran out of players");
        }
        Player p = tieBreakPlayers.get(0);
        if (0 >= p.getSortedRemainingCards().size() - 1) {
            // TODO split pot
            return null;
        }
        return tieBreakHighestKicker(tieBreakPlayers, 0);
    }

    private Player tieBreakNoPair(List<Player> somePlayers) {
        return tieBreakHighestKicker(somePlayers, 0);
    }

    private Player tieBreakHighestKicker(List<Player> somePlayers, int n) {
        List<Player> tieBreakPlayers = new ArrayList<>(somePlayers);
        int highKicker = 0;
        for (Player p : tieBreakPlayers) {
            int high = p.getSortedRemainingCards().get(n).getRank().getValue();
            if (high > highKicker) {
                highKicker = high;
            }
        }
        Iterator<Player> iterator = tieBreakPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getSortedRemainingCards().get(n).getRank().getValue() != highKicker) {
                iterator.remove();
            }
        }
        if (tieBreakPlayers.size() == 1) {
            return tieBreakPlayers.get(0);
        }
        if (tieBreakPlayers.size() == 0) {
            throw new RuntimeException("Ran out of tiebreaks");
        }
        Player p = tieBreakPlayers.get(0);
        if (n == p.getSortedRemainingCards().size() - 1) {
            // TODO split pot
            return null;
        }
        return tieBreakHighestKicker(tieBreakPlayers, n + 1);
    }

    private void debugHands() {
        System.out.println("Community : " + getHand(this.communityCards));
        for (Player player : players) {
            System.out.println(player.getId() + " : " + getHand(player.getBestHand().getCards())
                    + " (" + player.getBestHand().getScoringCombination().getValue() + " : " + player.getBestHand().getScoringCombination().getName() + ") "
                    + getHand(player.getBestHand().getRemainingCards()));
        }
        if (winner == null) {
            System.out.println("Split pot");
            return;
        }
        System.out.println("Winner : " + winner.getId() + " with " + winner.getBestHand().getScoringCombination().getName());
    }

    private String getHand(Set<Card> cards) {
        if (cards.isEmpty()) {
            return "nothing";
        }
        String hand = "";
        List<Card> sortedCards = sortCards(cards);
        for (Card card : sortedCards) {
            hand = hand + card.getRank().getName() + " " + card.getSuit().getName() + " ";
        }
        return hand.trim();
    }

    private List<Card> sortCards(Set<Card> cards) {
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(Comparator.comparing(c -> ((Card) c).getRank()).reversed().thenComparing(c -> ((Card) c).getSuit().getCode()));
        return sortedCards;
    }

    public void scoreHands() {
        for (Player player : players) {
            BestHand result = scoreHand(player.getCards(), communityCards);
            player.setBestHand(result);
        }
    }

    private BestHand scoreHand(Set<Card> playerCards, Set<Card> communityCards) {
        Set<Card> cards = new HashSet<>();
        cards.addAll(playerCards);
        cards.addAll(communityCards);

        ScoreHelper scoreHelper = new ScoreHelper(cards);
        return scoreHelper.getResult();
    }

    private void setupOtherPlayers() {
        List<Card> cards = new ArrayList<>(deck);
        int index = 0;
        for (Player player : players) {
            if (player.getId() == 1) {
                continue;
            }
            player.getCards().add(cards.get(index++));
            player.getCards().add(cards.get(index++));
        }
    }

    private void setupPlayers(int playerCount) {
        players.clear();
        for (int i = 0; i < playerCount; i++) {
            players.add(new Player(i + 1));
        }
    }

    private void setupPlayer1(Set<Card> cards) {
        Player player1 = players.get(0);
        for (Card playerCard : cards) {
            Iterator<Card> iterator = deck.iterator();
            while (iterator.hasNext()) {
                Card card = iterator.next();
                if (card.equals(playerCard)) {
                    player1.getCards().add(card);
                    iterator.remove();
                }
            }
        }
    }

    private void setupCommunityCards(int communityCardCount) {
        int cardsDealt = 0;
        this.communityCards.clear();
        Iterator<Card> iterator = deck.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            this.communityCards.add(card);
            iterator.remove();
            cardsDealt++;
            if (cardsDealt == communityCardCount) {
                break;
            }
        }
    }

    private void setupCommunityCards(Set<Card> communityCards) {
        this.communityCards.clear();
        for (Card playerCard : communityCards) {
            Iterator<Card> iterator = deck.iterator();
            while (iterator.hasNext()) {
                Card card = iterator.next();
                if (card.equals(playerCard)) {
                    this.communityCards.add(card);
                    iterator.remove();
                }
            }
        }
    }

    public Set<Card> getFullDeck() {
        Set<Card> deck = new HashSet<>();
        for (Suit suit : SUITS) {
            for (Rank rank : Rank.getRanks()) {
                Card card = new Card(suit, rank);
                deck.add(card);
            }
        }
        return deck;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public Set<Card> getCommunityCards() {
        return communityCards;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
