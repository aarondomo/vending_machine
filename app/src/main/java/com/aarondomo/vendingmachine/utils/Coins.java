package com.aarondomo.vendingmachine.utils;

import java.util.HashSet;
import java.util.Set;

public abstract class Coins {
    public static final int ONE_CENT = 1;
    public static final int FIVE_CENTS = 5;
    public static final int TEN_CENTS = 10;
    public static final int TWENTY_FIVE_CENTS = 25;

    public static final int[] validCoins = {
            FIVE_CENTS,
            TEN_CENTS,
            TWENTY_FIVE_CENTS
    };

    public static final int[] invalidCoins = {
            ONE_CENT
    };

}
