package com.aarondomo.vendingmachine.utils;

import com.aarondomo.vendingmachine.model.Coins;

import java.util.List;

public class CoinsUtil {

    public static int getValidCoinValue(String coinValue){
        try {
            Integer coin = Integer.valueOf(coinValue);
            if(isValidCoin(coin)){
                return coin;
            }
        } catch (NumberFormatException e){

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
            if(isValidCoin(coin)){
                amount += coin;
            }
        }
        return amount;
    }
}
