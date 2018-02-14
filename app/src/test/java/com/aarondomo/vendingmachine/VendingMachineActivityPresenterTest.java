package com.aarondomo.vendingmachine;

import com.aarondomo.vendingmachine.model.Inventory;
import com.aarondomo.vendingmachine.model.PettyCash;
import com.aarondomo.vendingmachine.model.Product;
import com.aarondomo.vendingmachine.presenters.VendingMachineActivityPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class VendingMachineActivityPresenterTest {

    private static final String EMPTY_STRING = "";
    private static final String THANK_YOU_MSG = "THANK YOU";
    private static final String INSERT_COIN_MSG = "INSERT_COIN";
    private static final String PRICE_MSG = "PRICE: ";
    private static final String SOLD_OUT = "SOLD OUT";

    @Mock
    VendingMachineActivityPresenter.View mockView;

    @Mock
    Inventory mockInventory;

    @Mock
    PettyCash mockPettyCash;

    VendingMachineActivityPresenter subject;


    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        subject = new VendingMachineActivityPresenter(mockInventory, mockPettyCash);

        subject.attachView(mockView);

    }


    @Test
    public void receiveCoin_should_display_inserted_amount_when_coin_is_valid() throws Exception {

        String coinValue = "25";

        subject.receiveCoin(coinValue);

        verify(mockView).displayMessage(coinValue);

    }

    @Test
    public void receiveCoin_should_return_coin_and_not_update_inserted_amount_when_coin_is__NOT_valid() throws Exception {

        String coinValue = "1";

        subject.receiveCoin(coinValue);

        verify(mockView, never()).displayMessage(coinValue);
        verify(mockView).returnCoin(coinValue);

    }

    @Test
    public void receiveCoin_should_return_value_inserted_and_not_update_inserted_amount_when_inserted_value_is_NOT_a_coin() throws Exception {

        String coinValue = "abc";

        subject.receiveCoin(coinValue);

        verify(mockView, never()).displayMessage(coinValue);
        verify(mockView).returnCoin(coinValue);

    }

    @Test
    public void receiveCoin_should_return_empty_string_and_not_update_inserted_amount_when_inserted_value_is_null() throws Exception {

        String coinValue = null;

        subject.receiveCoin(coinValue);

        verify(mockView, never()).displayMessage(coinValue);
        verify(mockView).returnCoin(EMPTY_STRING);

    }

    @Test
    public void dispatchProduct_should_display_thank_you_and_product_dispatched_and_delayed_insert_coin_when_inserted_amoun_greater_than_price() throws Exception {

        String coinValue = "25";
        Product product = new Product("Coke", 100);
        when(mockInventory.getProductQuantity(product)).thenReturn(1);
        when(mockPettyCash.isChangeAvailable(0)).thenReturn(true);


        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(THANK_YOU_MSG);
        verify(mockView).setProductDispatched(product.getName());
        verify(mockView).displayDelayedMessage(INSERT_COIN_MSG);

    }

    @Test
    public void dispatchProduct_should_display_thank_you_and_product_dispatched_and_return_change_and_delayed_insert_coin_when_inserted_amoun_greater_than_price() throws Exception {

        String coinValue = "25";
        Product product = new Product("Coke", 100);
        when(mockInventory.getProductQuantity(product)).thenReturn(1);
        when(mockPettyCash.isChangeAvailable(25)).thenReturn(true);

        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(THANK_YOU_MSG);
        verify(mockView).setProductDispatched(product.getName());
        verify(mockView).displayDelayedMessage(INSERT_COIN_MSG);
        verify(mockView).returnCoin("25");

    }

    @Test
    public void dispatchProduct_should_display_price_and_delayed_inserted_amount_when_inserted_amount_less_than_price() throws Exception {

        String coinValue = "25";
        Product product = new Product("Coke", 100);
        when(mockInventory.getProductQuantity(product)).thenReturn(1);

        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(PRICE_MSG + product.getPrice());
        verify(mockView).displayDelayedMessage("50");

    }

    @Test
    public void dispatchProduct_should_display_price_and_delayed_insert_coin_when_inserted_amount_is_zero() throws Exception {

        Product product = new Product("Coke", 100);
        when(mockInventory.getProductQuantity(product)).thenReturn(1);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(PRICE_MSG + product.getPrice());
        verify(mockView).displayDelayedMessage(INSERT_COIN_MSG);

    }

    @Test
    public void dispatchProduct_should_display_sold_out_and_delayed_insert_coin_when_product_quantity_is_zero_and_inserted_amount_is_zero() throws Exception {

        Product product = new Product("Coke", 100);
        when(mockInventory.getProductQuantity(product)).thenReturn(0);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(SOLD_OUT);
        verify(mockView).displayDelayedMessage(INSERT_COIN_MSG);

    }

    @Test
    public void dispatchProduct_should_display_sold_out_and_delayed_insert_amount_when_product_quantity_is_zero_and_inserted_amount_is_not_zero() throws Exception {

        String coinValue = "25";
        Product product = new Product("Coke", 100);
        when(mockInventory.getProductQuantity(product)).thenReturn(0);
        subject.receiveCoin(coinValue);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(SOLD_OUT);
        verify(mockView).displayDelayedMessage(coinValue);

    }

    @Test
    public void getMoneyBack_should_return_coin_and_display_insert_coin() throws Exception {

        String coinValue = "25";

        subject.receiveCoin(coinValue);

        subject.getMoneyBack();

        verify(mockView).returnCoin(coinValue);
        verify(mockView).displayMessage(INSERT_COIN_MSG);

    }





}