package com.aarondomo.vendingmachine.utils;

import com.aarondomo.vendingmachine.model.Coins;

import org.junit.Test;

import java.util.List;

import edu.emory.mathcs.backport.java.util.LinkedList;

import static org.junit.Assert.*;


public class CoinsUtilTest {

    @Test
    public void getValidCoinValue_should_return_valid_coin_value_if_coin_is_in_valid_list() throws Exception {

        assertEquals(Coins.FIVE_CENTS, CoinsUtil.getValidCoinValue("5"));
        assertEquals(Coins.TEN_CENTS, CoinsUtil.getValidCoinValue("10"));
        assertEquals(Coins.TWENTY_FIVE_CENTS, CoinsUtil.getValidCoinValue("25"));

    }

    @Test
    public void getValidCoinValue_should_return_invalid_coin_value_if_coin_is_in_not_in_valid_list() throws Exception {

        assertNotEquals(Coins.ONE_CENT, CoinsUtil.getValidCoinValue("1") );

        assertEquals(-1, CoinsUtil.getValidCoinValue("50") );
    }

    @Test
    public void getValidCoinValue_should_return_invalid_coin_value_if_washer_inserted() throws Exception {
        assertEquals(-1, CoinsUtil.getValidCoinValue("washer"));
    }

    @Test
    public void getValidCoinValue_should_return_invalid_coin_value_if_coin_is_null() throws Exception {
        assertEquals(-1, CoinsUtil.getValidCoinValue(null));
    }

    @Test
    public void isValidCoin_should_return_true_if_coin_is_valid() throws Exception {
        assertTrue(CoinsUtil.isValidCoin(Coins.FIVE_CENTS));
    }

    @Test
    public void isValidCoin_should_return_false_if_coin_is_not_valid() throws Exception {
        assertFalse(CoinsUtil.isValidCoin(Coins.ONE_CENT));
    }

    @Test
    public void getCoinsValue_should_return_sum_of_the_coins() throws Exception {
        List<Integer> coins = new LinkedList();

        coins.add(10);
        coins.add(10);

        assertEquals(20, CoinsUtil.getCoinsValue(coins));

    }

}