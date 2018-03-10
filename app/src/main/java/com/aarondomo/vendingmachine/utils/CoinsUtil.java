package com.aarondomo.vendingmachine.utils;

import com.aarondomo.vendingmachine.model.Coins;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public static Map<Integer, Integer> getCoinsMap(List<Integer> coins){
        Map<Integer, Integer> coinsMap = new TreeMap<>();
        for(Integer coin : coins){
            int numberOfCoins = 1;
            if(coinsMap.containsKey(coin)){
                numberOfCoins = coinsMap.get(coin) + 1;
            }
            coinsMap.put(coin, numberOfCoins);
        }
        return coinsMap;
    }

    public static String getCoinMapString(List<Integer> coins){
        Map<Integer, Integer> coinsMap = getCoinsMap(coins);
        StringBuffer stringBuffer = new StringBuffer();
        for(Integer coinKey : coinsMap.keySet()){
            stringBuffer.append(coinsMap.get(coinKey));
            stringBuffer.append("-");
            stringBuffer.append(coinKey);
            stringBuffer.append(" ");
        }
        return new String(stringBuffer);
    }
}
