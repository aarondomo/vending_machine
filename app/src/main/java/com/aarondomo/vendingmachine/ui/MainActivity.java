package com.aarondomo.vendingmachine.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aarondomo.vendingmachine.MainActivityPresenter;
import com.aarondomo.vendingmachine.R;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View{

    private TextView textViewDisplay;
    private EditText editTextCoinSlot;
    private Button buttonInsertCoin;
    private TextView textViewCoinReturn;

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setUpButtonInsertCoinOnClickListener();

        //TODO: inject the presenter with Dagger2
        presenter = new MainActivityPresenter();

        presenter.attachView(this);

    }

    private void bindViews(){
        //TODO: bind using Butterknife
        textViewDisplay = (TextView) findViewById(R.id.textView_main_display);
        editTextCoinSlot = (EditText) findViewById(R.id.editText_main_coinSlot);
        buttonInsertCoin = (Button) findViewById(R.id.button_main_insertCoin);
        textViewCoinReturn = (TextView) findViewById(R.id.textView_main_coinReturn);
    }

    private void setUpButtonInsertCoinOnClickListener(){
        buttonInsertCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoin();
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
    public void returnCoin(String coinValue){
        //TODO: Extract string values
        textViewCoinReturn.setText("Coin Return: " + coinValue);
    }

    private void getCoin(){
        String coinValue = editTextCoinSlot.getText().toString();
        presenter.receiveCoin(coinValue);
    }

}
