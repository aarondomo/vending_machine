package com.aarondomo.vendingmachine;


import android.util.Log;

import com.aarondomo.vendingmachine.utils.Coins;
import com.aarondomo.vendingmachine.utils.Inventory;
import com.aarondomo.vendingmachine.utils.Product;

import java.util.List;

public class MainActivityPresenter {

    public interface View {

        void displayMessage(String message);

        void returnCoin(String coinValue);

        void setProductDispatched(String productName);

        void displayDelayedMessage(String message);

        void displayProducts(List<Product> products);

    }

    private View view;
    private int insertedAmount = 0;
    private Inventory inventory;

    private static final String TAG = MainActivityPresenter.class.getName();

    private static final String EMPTY_STRING = "";

    private static final String THANK_YOU_MSG = "THANK YOU";
    private static final String INSERT_COIN_MSG = "INSERT_COIN";
    private static final String PRICE_MSG = "PRICE: ";

    public MainActivityPresenter(){
        //TODO: inject inventory
        inventory = new Inventory();
    }

    public void attachView(MainActivityPresenter.View view){
        this.view = view;
        view.displayProducts(inventory.getProducts());
    }

    public void dettachView(){
        this.view = null;
    }

    public void receiveCoin(String coinValue){
        if(coinValue == null){
            coinValue = EMPTY_STRING;
        }
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

    public void dispatchProduct(Product product) {
        int price = product.getPrice();
        if(insertedAmount >= price){
            int change = insertedAmount - price;
            insertedAmount = 0;
            view.displayMessage(THANK_YOU_MSG);
            view.setProductDispatched(product.getName());
            view.displayDelayedMessage(INSERT_COIN_MSG);
            view.returnCoin(Integer.toString(change));
        } else {
            view.displayMessage(PRICE_MSG + product.getPrice());
            if(insertedAmount == 0){
                view.displayDelayedMessage(INSERT_COIN_MSG);
            } else {
                view.displayDelayedMessage(Integer.toString(insertedAmount));
            }
        }

    }


    public void getMoneyBack() {
        view.returnCoin(Integer.toString(insertedAmount));
        insertedAmount = 0;
        view.displayMessage(INSERT_COIN_MSG);
    }

}
