package com.aarondomo.vendingmachine.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aarondomo.vendingmachine.MainActivityPresenter;
import com.aarondomo.vendingmachine.R;
import com.aarondomo.vendingmachine.model.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View{

    private LinearLayout linearLayoutProductDisplay;
    private TextView textViewDisplay;
    private EditText editTextCoinSlot;
    private Button buttonInsertCoin;
    private Button buttonMoneyBack;
    private TextView textViewCoinReturn;
    private TextView textViewProductDispatch;

    private MainActivityPresenter presenter;

    private static final String COIN_RETURN = "Coin Return: ";
    private static final String DISPATCHED_PRODUCT = "Dispatched product: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setUpButtonInsertCoinOnClickListener();
        setUpButtonMoneyBackOnClickListener();

        //TODO: inject the presenter with Dagger2
        presenter = new MainActivityPresenter();

        presenter.attachView(this);

    }

    private void bindViews(){
        //TODO: bind using Butterknife
        linearLayoutProductDisplay = (LinearLayout)findViewById(R.id.linearLayout_main_productDisplay);
        textViewDisplay = (TextView) findViewById(R.id.textView_main_display);
        editTextCoinSlot = (EditText) findViewById(R.id.editText_main_coinSlot);
        buttonInsertCoin = (Button) findViewById(R.id.button_main_insertCoin);
        buttonMoneyBack = (Button) findViewById(R.id.button_main_moneyBack);
        textViewCoinReturn = (TextView) findViewById(R.id.textView_main_coinReturn);
        textViewProductDispatch = (TextView) findViewById(R.id.textView_main_productDispatch);
    }

    private void setUpButtonInsertCoinOnClickListener(){
        buttonInsertCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoin();
                editTextCoinSlot.setText("");
            }
        });
    }

    private void setUpButtonMoneyBackOnClickListener(){
        buttonMoneyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getMoneyBack();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dettachView();
    }

    @Override
    public void displayMessage(String message){
        textViewDisplay.setText(message);
    }

    @Override
    public void displayDelayedMessage(final String message){
        textViewDisplay.postDelayed(new Runnable() {
            public void run() {
                textViewDisplay.setText(message);
            }
        }, 1000);
    }

    @Override
    public void returnCoin(String coinValue){
        //TODO: Extract string values
        textViewCoinReturn.setText(COIN_RETURN + coinValue);
    }

    @Override
    public void setProductDispatched(String productName){
        textViewProductDispatch.setText(DISPATCHED_PRODUCT + productName);
    }

    private void getCoin(){
        String coinValue = editTextCoinSlot.getText().toString();
        presenter.receiveCoin(coinValue);
    }

    @Override
    public void displayProducts(List<Product> products){
        for(final Product product : products) {
            linearLayoutProductDisplay.addView(setUpProductButton(product));
        }
    }

    private Button setUpProductButton(final Product product){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        Button productButton = new Button(this);
        productButton.setText(product.getName());
        productButton.setLayoutParams(params);

        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.dispatchProduct(product);
            }
        });
        return productButton;
    }

}
