package com.aarondomo.vendingmachine;

import com.aarondomo.vendingmachine.utils.Product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MainActivityPresenterTest {

    private static final String EMPTY_STRING = "";
    private static final String THANK_YOU_MSG = "THANK YOU";
    private static final String INSERT_COIN_MSG = "INSERT_COIN";
    private static final String PRICE_MSG = "PRICE: ";

    @Mock
    MainActivityPresenter.View mockView;

    MainActivityPresenter subject;


    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        subject = new MainActivityPresenter();

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
    public void dispatchProduct_should_display_price_and_delayed_inserted_amount_when_inserted_amount_less_than_price() throws Exception {

        String coinValue = "25";
        Product product = new Product("Coke", 100);

        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(PRICE_MSG + PRICE_MSG + product.getPrice());
        verify(mockView).displayDelayedMessage("50");

    }

    @Test
    public void dispatchProduct_should_display_price_and_delayed_insert_coin_when_inserted_amount_is_zero() throws Exception {

        String coinValue = "25";
        Product product = new Product("Coke", 100);

        subject.receiveCoin(coinValue);
        subject.receiveCoin(coinValue);

        subject.dispatchProduct(product);

        verify(mockView).displayMessage(PRICE_MSG + PRICE_MSG + product.getPrice());
        verify(mockView).displayDelayedMessage(INSERT_COIN_MSG);

    }


}