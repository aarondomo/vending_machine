package com.aarondomo.vendingmachine.utils;

import android.util.Log;

import java.util.List;

public class CoinsUtil {

    private static final String TAG = CoinsUtil.class.getName();

    public static int getCoinValue(String coinValue){
        try {
            Integer coin = Integer.valueOf(coinValue);
            if(isValidCoin(coin)){
                return coin;
            }
        } catch (NumberFormatException e){
            Log.d(TAG, e.getMessage());
        }
        return -1;
    }

    public static boolean isValidCoin(int coin){
        for(int validCoin : Coins.validCoins){
            if(coin == validCoin){
                return true;
            }
        }
        return false;
    }

    public static int getCoinsValue(List<Integer> coins){
        int amount = 0;
        for(int coin : coins){
            amount += coin;
        }
        return amount;
    }
}
