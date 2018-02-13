package com.aarondomo.vendingmachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MainActivityPresenterTest {

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
        verify(mockView).returnCoin("");

    }



}