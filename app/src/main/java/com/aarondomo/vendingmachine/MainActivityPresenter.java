package com.aarondomo.vendingmachine;


import android.util.Log;

import com.aarondomo.vendingmachine.utils.Coins;

public class MainActivityPresenter {

    public interface View {

        void displayMessage(String message);

        void returnCoin(String coinValue);

    }

    private View view;

    private int insertedAmount = 0;

    private static final String TAG = MainActivityPresenter.class.getName();

    public void attachView(MainActivityPresenter.View view){
        this.view = view;
    }

    public void dettachView(){
        this.view = null;
    }

    public void receiveCoin(String coinValue){
        int coin = getCoinValue(coinValue);
        if(coin != -1){
            insertedAmount += coin;
            view.displayMessage(Integer.toString(insertedAmount));
        } else{
            view.returnCoin(coinValue);
        }
    }

    private int getCoinValue(String coinValue){
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

    private boolean isValidCoin(int coin){
        for(int validCoin : Coins.validCoins){
            if(coin == validCoin){
                return true;
            }
        }
        return false;
    }



}
