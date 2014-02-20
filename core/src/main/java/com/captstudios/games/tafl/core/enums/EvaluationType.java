package com.captstudios.games.tafl.core.enums;


/**
 * Alphabeta may return an actual move potency evaluation, or an upper or
 * lower bound only (in case a cutoff happens). We need to store this
 * information in the transposition table to make sure that a given
 * value is actually useful in given circumstances.
 * 
 * @author apotapov
 *
 */
public enum EvaluationType {
    ACCURATE,
    UPPERBOUND,
    LOWERBOUND;
}
