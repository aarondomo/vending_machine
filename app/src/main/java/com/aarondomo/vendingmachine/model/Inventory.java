package com.aarondomo.vendingmachine.model;

import com.aarondomo.vendingmachine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {

    //The Integer value reflects the quantity available of a products available in the vending machine
    private Map<Product, Integer> warehouseMap;

    public Inventory() {
        warehouseMap = new HashMap<>();
        supplyInventory();
    }

    private void supplyInventory(){
        warehouseMap.put(new Product("Cola", 100, R.drawable.cola_256), 2);
        warehouseMap.put(new Product("Chip", 50, R.drawable.chips_256), 2);
        warehouseMap.put(new Product("Candy", 65, R.drawable.candy_256), 2);
        warehouseMap.put(new Product("Cookies", 80, R.drawable.cookie_256), 2);
    }

    //TODO: Add test cases
    public int getProductQuantity(Product product){
        return warehouseMap.get(product);
    }

    public List<Product> getProducts(){
        return new ArrayList<Product>(warehouseMap.keySet());
    }

    public int obtainProduct(Product product){
        int quantity = warehouseMap.get(product);
        if(quantity > 0){
            quantity--;
            warehouseMap.put(product, quantity);
            return quantity;
        } else {
            warehouseMap.put(product, 0);
            return 0;
        }
    }
}
