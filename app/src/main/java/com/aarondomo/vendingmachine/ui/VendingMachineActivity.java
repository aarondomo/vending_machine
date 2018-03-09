package com.aarondomo.vendingmachine.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aarondomo.vendingmachine.di.DaggerVendingMachineComponent;
import com.aarondomo.vendingmachine.di.VendingMachineModule;
import com.aarondomo.vendingmachine.presenters.VendingMachineActivityPresenter;
import com.aarondomo.vendingmachine.R;
import com.aarondomo.vendingmachine.model.Product;

import java.util.List;

import javax.inject.Inject;

public class VendingMachineActivity extends AppCompatActivity implements VendingMachineActivityPresenter.View{

    private TableLayout tableLayoutProductDisplay;
    private TextView textViewDisplay;
    private EditText editTextCoinSlot;
    private Button buttonInsertCoin;
    private Button buttonMoneyBack;
    private TextView textViewCoinReturn;
    private TextView textViewProductDispatch;
    private DisplayMetrics displayMetrics;

    @Inject
    VendingMachineActivityPresenter presenter;

    private static final String COIN_RETURN = "Coin Return: ";
    private static final String DISPATCHED_PRODUCT = "Dispatched product: ";
    private static final int DELAY_TIME = 1000;
    private static final int COLUMN_NUMBER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpDisplayMetrics();

        bindViews();
        setUpButtonInsertCoinOnClickListener();
        setUpButtonMoneyBackOnClickListener();

        setUpDaggerComponent();

        presenter.attachView(this);

    }

    private void setUpDaggerComponent() {
        DaggerVendingMachineComponent.builder()
                .vendingMachineModule(new VendingMachineModule(this))
                .build()
                .inject(this);
    }


    private void bindViews(){
        tableLayoutProductDisplay = (TableLayout) findViewById(R.id.tableLayout_main_productDisplay);
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
        }, DELAY_TIME);
    }

    @Override
    public void returnCoin(String coinValue){
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
        TableRow tableRow = null;

        int rowItems = 0;

        for(final Product product : products) {
            if(rowItems % COLUMN_NUMBER == 0){
                tableRow = new TableRow(this);
                tableRow.addView(setUpProductButton(product));
                tableLayoutProductDisplay.addView(tableRow);
            } else {
                tableRow.addView(setUpProductButton(product));
            }
            rowItems++;
        }
        tableLayoutProductDisplay.setStretchAllColumns(true);
    }

    private ImageButton setUpProductButton(final Product product){

        ImageButton productButton = new ImageButton(this);
        productButton.setImageBitmap(resize(product.getPicture()));

        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.dispatchProduct(product);
            }
        });
        return productButton;
    }

    private void setUpDisplayMetrics(){
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
    }

    private Bitmap resize(int pictureResource) {
        Drawable image = getResources().getDrawable(pictureResource);
        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        int size = getImageWidth();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, size, size, false);
        return bitmapResized;
    }

    private int getImageWidth(){
        return (displayMetrics.widthPixels / (COLUMN_NUMBER * 2));
    }

}
