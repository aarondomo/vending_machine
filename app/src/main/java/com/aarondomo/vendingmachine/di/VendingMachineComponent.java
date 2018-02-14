package com.aarondomo.vendingmachine.di;

import com.aarondomo.vendingmachine.ui.VendingMachineActivity;

import dagger.Component;

@Component(modules = VendingMachineModule.class)
public interface VendingMachineComponent {
    void inject(VendingMachineActivity vendingMachineActivity);
}