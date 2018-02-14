package com.aarondomo.vendingmachine.di;

import android.content.Context;

import com.aarondomo.vendingmachine.model.Inventory;
import com.aarondomo.vendingmachine.model.PettyCash;
import com.aarondomo.vendingmachine.presenters.VendingMachineActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class VendingMachineModule {
    private Context context;

    public VendingMachineModule(Context context){
        this.context = context;
    }

    @Provides
    public VendingMachineActivityPresenter provideVendingMachineActivityPresenter(){
        return new VendingMachineActivityPresenter(new Inventory(), new PettyCash());
    }

}