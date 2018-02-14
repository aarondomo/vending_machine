package com.aarondomo.vendingmachine;


import android.util.Log;

import com.aarondomo.vendingmachine.model.PettyCash;
import com.aarondomo.vendingmachine.utils.Coins;
import com.aarondomo.vendingmachine.model.Inventory;
import com.aarondomo.vendingmachine.model.Product;
import com.aarondomo.vendingmachine.utils.CoinsUtil;

import java.util.LinkedList;
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
    private Inventory inventory;
    private PettyCash pettyCash;
    private List<Integer> insertedCoins;

    private static final String EMPTY_STRING = "";

    private static final String THANK_YOU_MSG = "THANK YOU";
    private static final String INSERT_COIN_MSG = "INSERT_COIN";
    private static final String PRICE_MSG = "PRICE: ";
    private static final String SOLD_OUT = "SOLD OUT";
    private static final String EXACT_CHANGE_MSG = "EXACT CHANGE ONLY" ;

    public MainActivityPresenter(){
        //TODO: inject inventory and pettyCash
        inventory = new Inventory();
        pettyCash = new PettyCash();
        insertedCoins = new LinkedList<Integer>();
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

        int coin = CoinsUtil.getCoinValue(coinValue);
        if(coin != -1){
            insertedCoins.add(coin);
            view.displayMessage(Integer.toString(CoinsUtil.getInsertedAmount(insertedCoins)));
        } else{
            view.returnCoin(coinValue);
        }
    }

    public void dispatchProduct(Product product) {
        if(inventory.getProductQuantity(product) == 0) {
            view.displayMessage(SOLD_OUT);
            displayInsertedAmount();
            return;
        }

        int price = product.getPrice();

        if(CoinsUtil.getInsertedAmount(insertedCoins) >= price
                && inventory.getProductQuantity(product) > 0){

            int changeAmount = CoinsUtil.getInsertedAmount(insertedCoins) - price;

            pettyCash.addCoins(insertedCoins);

            if(pettyCash.isChangeAvailable(changeAmount)){
                pettyCash.getChange(changeAmount);
                insertedCoins.clear();
                inventory.obtainProduct(product);

                view.displayMessage(THANK_YOU_MSG);
                view.setProductDispatched(product.getName());
                view.displayDelayedMessage(INSERT_COIN_MSG);
                view.returnCoin(Integer.toString(changeAmount));
            } else {
                displayMessageAndDelayedAmount(EXACT_CHANGE_MSG);
            }
        } else {
            displayMessageAndDelayedAmount(PRICE_MSG + product.getPrice());
        }
    }


    private void displayMessageAndDelayedAmount(String message){
        view.displayMessage(message);
        displayInsertedAmount();
    }

    private void displayInsertedAmount(){
        if(CoinsUtil.getInsertedAmount(insertedCoins) == 0){
            view.displayDelayedMessage(INSERT_COIN_MSG);
        } else {
            view.displayDelayedMessage(Integer.toString(CoinsUtil.getInsertedAmount(insertedCoins)));
        }
    }

    public void getMoneyBack() {
        view.returnCoin(Integer.toString(CoinsUtil.getInsertedAmount(insertedCoins)));
        insertedCoins.clear();
        view.displayMessage(INSERT_COIN_MSG);
    }


}
