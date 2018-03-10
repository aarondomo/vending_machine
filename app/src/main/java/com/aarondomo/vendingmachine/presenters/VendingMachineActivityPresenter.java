package com.aarondomo.vendingmachine.presenters;


import com.aarondomo.vendingmachine.model.PettyCash;
import com.aarondomo.vendingmachine.model.Inventory;
import com.aarondomo.vendingmachine.model.Product;
import com.aarondomo.vendingmachine.utils.CoinsUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VendingMachineActivityPresenter {

    public interface View {

        void displayMessage(String message);

        void returnCoin(String coinValue);

        void returnInvalidCoin(String coinValue);

        void displayDelayedMessage(String message);

        void displayProducts(List<Product> products);

        void showDispatchedProductAlert(Product product);

        void showChangeCoins(Map<Integer, Integer> coinsMap);

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

    public VendingMachineActivityPresenter(Inventory inventory,
                                           PettyCash pettyCash){
        this.inventory = inventory;
        this.pettyCash = pettyCash;
        insertedCoins = new LinkedList<Integer>();
    }

    public void attachView(VendingMachineActivityPresenter.View view){
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

        int coin = CoinsUtil.getValidCoinValue(coinValue);
        if(coin != -1){
            insertedCoins.add(coin);
            view.displayMessage(Integer.toString(CoinsUtil.getCoinsValue(insertedCoins)));
        } else{
            view.returnInvalidCoin(coinValue);
        }
    }

    public void dispatchProduct(Product product) {
        if(inventory.getProductQuantity(product) == 0) {
            view.displayMessage(SOLD_OUT);
            displayInsertedAmount();
            return;
        }

        int price = product.getPrice();

        if(CoinsUtil.getCoinsValue(insertedCoins) >= price
                && inventory.getProductQuantity(product) > 0){

            int changeAmount = CoinsUtil.getCoinsValue(insertedCoins) - price;

            pettyCash.addCoins(insertedCoins);

            if(pettyCash.isChangeAvailable(changeAmount)){
                List<Integer> changeCoins = pettyCash.getChange(changeAmount);
                insertedCoins.clear();
                inventory.obtainProduct(product);

                view.displayMessage(THANK_YOU_MSG);
                view.showDispatchedProductAlert(product);
                view.displayDelayedMessage(INSERT_COIN_MSG);
                displayReturnedCoins(changeCoins);
            } else {
                displayMessageAndDelayedAmount(EXACT_CHANGE_MSG);
            }
        } else {
            displayMessageAndDelayedAmount(PRICE_MSG + product.getPrice());
        }
    }


    public void getMoneyBack() {
        displayReturnedCoins(insertedCoins);
        insertedCoins.clear();
        view.displayMessage(INSERT_COIN_MSG);
    }

    private void displayMessageAndDelayedAmount(String message){
        view.displayMessage(message);
        displayInsertedAmount();
    }

    private void displayInsertedAmount(){
        if(CoinsUtil.getCoinsValue(insertedCoins) == 0){
            view.displayDelayedMessage(INSERT_COIN_MSG);
        } else {
            view.displayDelayedMessage(Integer.toString(CoinsUtil.getCoinsValue(insertedCoins)));
        }
    }

    private String getReturnCoinMessage(List<Integer> coinList){
        view.showChangeCoins(CoinsUtil.getCoinsMap(coinList));
        return CoinsUtil.getCoinsValue(coinList) + " cents";
    }

    private void displayReturnedCoins(List<Integer> coinList){
        if(coinList != null && !coinList.isEmpty()){
            view.returnCoin(getReturnCoinMessage(coinList));
            view.showChangeCoins(CoinsUtil.getCoinsMap(coinList));
        }
    }


}
