package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Helper enumeration class to process Cards
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Getter
@RequiredArgsConstructor
public class Card {

    public enum Suit {CLUB, DIAMOND, HEART, SPADE}

    public enum Rank {
        ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING
    }

    private final Suit suit;
    private final Rank rank;
}
