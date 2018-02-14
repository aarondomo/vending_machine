package com.aarondomo.vendingmachine.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {

    private Map<Product, Integer> warehouseMap;

    public Inventory() {
        warehouseMap = new HashMap<>();
        supplyInventory();
    }

    private void supplyInventory(){
        warehouseMap.put(new Product("Cola", 100), 2);
        warehouseMap.put(new Product("Chip", 50), 2);
        warehouseMap.put(new Product("Candy", 65), 2);
    }

    //TODO: Other scenarios as substract product will be handled in further stories
    public int getProductQuantity(Product product){
        return warehouseMap.get(product);
    }

    public List<Product> getProducts(){
        return new ArrayList<Product>(warehouseMap.keySet());
    }

}
