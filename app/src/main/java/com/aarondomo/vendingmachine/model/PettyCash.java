package com.aarondomo.vendingmachine.model;

import com.aarondomo.vendingmachine.utils.Coins;
import com.aarondomo.vendingmachine.utils.CoinsUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PettyCash {

    //The Integer value reflects the quantity available of coins in the vending machine
    private Map<Integer, Integer> cashMap;

    public PettyCash() {
        cashMap = new HashMap<>();
        fillPettyCash();
    }

    private void fillPettyCash(){
        cashMap.put(Coins.FIVE_CENTS, 0);
        cashMap.put(Coins.TEN_CENTS, 0);
        cashMap.put(Coins.TWENTY_FIVE_CENTS, 0);
    }

    public int getCoinQuantity(int coin){
        return cashMap.get(coin);
    }

    public void addCoin(int coin){
        cashMap.put(coin, cashMap.get(coin) + 1);
    }

    public void addCoins(List<Integer> coins){
        for(int coin : coins){
            addCoin(coin);
        }
    }

    public void substractCoin(int coin){
        int amount = cashMap.get(coin);
        if(amount > 0){
            cashMap.put(coin, amount - 1);
        } else {
            cashMap.put(coin, 0);
        }
    }

    public void substractCoins(List<Integer> coins){
        for(int coin : coins){
            substractCoin(coin);
        }
    }

    public List<Integer> getChange(int amount){
        List<Integer> change = getChangeCoins(amount);

        substractCoins(change);

        return change;
    }

    public boolean isChangeAvailable(int amount){
        List<Integer> change = getChangeCoins(amount);

        return CoinsUtil.getCoinsValue(change) == amount;
    }

    private List<Integer> getChangeCoins(int amount){
        List<Integer> change = new LinkedList<Integer>();

        for(int i = 0; i < getCoinQuantity(Coins.TWENTY_FIVE_CENTS) && amount >= Coins.TWENTY_FIVE_CENTS; i++){
            change.add(Coins.TWENTY_FIVE_CENTS);
            amount -= Coins.TWENTY_FIVE_CENTS;
        }
        for(int i = 0; i < getCoinQuantity(Coins.TEN_CENTS) && amount >= Coins.TEN_CENTS; i++){
            change.add(Coins.TEN_CENTS);
            amount -= Coins.TEN_CENTS;
        }
        for(int i = 0; i < getCoinQuantity(Coins.FIVE_CENTS) && amount >= Coins.FIVE_CENTS; i++){
            change.add(Coins.FIVE_CENTS);
            amount -= Coins.FIVE_CENTS;
        }
        return change;
    }
}
