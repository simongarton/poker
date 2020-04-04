package com.simongarton.poker.services;

import com.simongarton.poker.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.simongarton.poker.model.Suit.*;

@Service
public class PokerService {

    public Suit[] SUITS = new Suit[]{CLUBS, DIAMONDS, HEARTS, SPADES};

    private Set<Card> deck;
    private Set<Card> communityCards = new HashSet<>();
    private List<Player> players = new ArrayList<>();
    private Player winner;

    public BestHand calculate(Set<Card> cards, int playerCount, Set<Card> communityCards) {
        setupPlayers(playerCount);

        deck = getFullDeck();
        setupPlayer1(cards);
        setupCommunityCards(communityCards);
        setupOtherPlayers();

        scoreHands();
        winner = getWinningPlayer();

        debugHands();
        return winner.getBestHand();
    }

    private Player getWinningPlayer() {
        // TODO this is NOT working
        players.sort(Comparator.comparing(p -> ((Player) p).getBestHand().getScoringCombination().getValue()).reversed().thenComparing(p -> this.getOtherCardScoreWithCommunity(((Player) p).getCards())).reversed());
        return players.get(0);
    }

    private double getOtherCardScoreWithCommunity(Set<Card> playerCards) {
        Set<Card> cards = new HashSet<>(playerCards);
        cards.addAll(communityCards);
        // TODO should remove scoring cards ...
        return getOtherCardScore(cards);
    }

    public long getOtherCardScore(Set<Card> cards) {
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(Comparator.comparing(c -> ((Card) c).getRank().getValue()).reversed());
        double score = 0;
        for (int n = 0; n < sortedCards.size(); n++) {
            Card card = sortedCards.get(n);
            score = score + Math.pow(16, 7-n) * card.getRank().getValue();
        }
        return Math.round(score);
    }

    private void debugHands() {
        System.out.println("Community : " + getHand(this.communityCards));
        for (Player player : players) {
            System.out.println(player.getId() + " : " + getHand(player.getCards())
                    + " (" + player.getBestHand().getScoringCombination().getValue() + " : " + player.getBestHand().getScoringCombination().getName() + ")");
        }
        System.out.println("Winner : " + winner.getId() + " with " + winner.getBestHand().getScoringCombination().getName());
    }

    private String getHand(Set<Card> cards) {
        String hand = "";
        List<Card> sortedCards = sortCards(cards);
        for (Card card : sortedCards) {
            hand = hand + card.getRank().getName() + " " + card.getSuit().getName() + " ";
        }
        return hand.trim();
    }

    private List<Card> sortCards(Set<Card> cards) {
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(Comparator.comparing(c -> ((Card) c).getSuit().getCode()).thenComparing(c -> ((Card) c).getRank().getValue()));
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
        BestHand bestHand = scoreHelper.getResult();
        return bestHand;
    }

    private void setupOtherPlayers() {
        List<Card> cards = new ArrayList<>(deck);
        Collections.shuffle(cards);
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

    private void setupCommunityCards(Set<Card> communityCards) {
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

    public Set<Card> getDeck() {
        return deck;
    }

    public Set<Card> getCommunityCards() {
        return communityCards;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
