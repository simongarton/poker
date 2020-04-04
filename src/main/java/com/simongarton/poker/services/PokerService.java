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

    public Result calculate(Set<Card> cards, int playerCount, Set<Card> communityCards) {
        Result result = new Result();
        result.setValue(0);
        result.setExplanation("Uncalculated");

        setupPlayers(playerCount);

        deck = getFullDeck();
        setupPlayer1(cards);
        setupCommunityCards(communityCards);
        setupOtherPlayers();

        debugHands();
        return result;
    }

    private void debugHands() {
        System.out.println(getHand(this.communityCards));
        for (Player player : players) {
            System.out.println(player.getId() + " : " + getHand(player.getCards()));
        }
    }

    private String getHand(Set<Card> cards) {
        String hand = "";
        for (Card card : cards) {
            hand = hand + card.getRank().getName() + " " + card.getSuit().getName() + " ";
        }
        return hand.trim();
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

    public List<Player> getPlayers() {
        return players;
    }
}
