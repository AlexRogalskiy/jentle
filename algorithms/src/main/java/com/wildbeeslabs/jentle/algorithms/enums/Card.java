package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Helper enumeration class to process Cards
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
public class Card {

    public enum Suit {CLUB, DIAMOND, HEART, SPADE}

    public enum Rank {
        ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING
    }

    final Suit suit;
    final Rank rank;

    public Card(final Suit suit, final Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
}
