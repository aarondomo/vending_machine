package com.aarondomo.vendingmachine.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aarondomo.vendingmachine.di.DaggerVendingMachineComponent;
import com.aarondomo.vendingmachine.di.VendingMachineModule;
import com.aarondomo.vendingmachine.model.Coins;
import com.aarondomo.vendingmachine.presenters.VendingMachineActivityPresenter;
import com.aarondomo.vendingmachine.R;
import com.aarondomo.vendingmachine.model.Product;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class VendingMachineActivity extends AppCompatActivity implements VendingMachineActivityPresenter.View{

    private TableLayout tableLayoutProductDisplay;
    private TextView textViewDisplay;
    private EditText editTextCoinSlot;
    private Button buttonInsertCoin;
    private Button buttonMoneyBack;
    private TextView textViewCoinReturn;
    private LinearLayout linearLayoutReturnCoinTray;

    private DisplayMetrics displayMetrics;

    @Inject
    VendingMachineActivityPresenter presenter;

    private static final String COIN_RETURN = "Coin Return: ";
    private static final String DISPATCHED_PRODUCT = "Dispatched product: ";
    private static final String TAKE_PRODUCT = "Take Product";
    private static final String ENJOY = "Enjoy!";
    private static final String MONEY_COLLECTED = "Money Collected";
    private static final String EMPTY_STRING = "";
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
        linearLayoutReturnCoinTray = (LinearLayout) findViewById(R.id.linearlayout_main_coin_return_tray);
    }

    private void setUpButtonInsertCoinOnClickListener(){
        buttonInsertCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoin();
                editTextCoinSlot.setText(EMPTY_STRING);
                hideSoftKeyboard();
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
    public void returnInvalidCoin(String coinValue){
        returnCoin(coinValue);
        showInvalidCoin();
    }

    private void getCoin(){
        String coinValue = editTextCoinSlot.getText().toString();
        if(!coinValue.equals(EMPTY_STRING)){
            presenter.receiveCoin(coinValue);
        }
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
        productButton.setImageBitmap(resize(product.getPicture(), getImageWidth()));

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

    private Bitmap resize(int pictureResource, int size) {
        Drawable image = getResources().getDrawable(pictureResource);
        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, size, size, false);
        return bitmapResized;
    }

    private int getImageWidth(){
        return (displayMetrics.widthPixels / (COLUMN_NUMBER * 2));
    }

    @Override
    public void showDispatchedProductAlert(Product product) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(product.getPicture());

        builder.setTitle(DISPATCHED_PRODUCT + product.getName());
        builder.setMessage(ENJOY);
        builder.setView(imageView);

        builder.setPositiveButton(TAKE_PRODUCT, null);

        final AlertDialog dialog = builder.create();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.show();
            }
        }, DELAY_TIME);
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void showChangeCoins(Map<Integer, Integer> coinsMap){
        linearLayoutReturnCoinTray.removeAllViews();
        for(Integer coinKey : coinsMap.keySet()){
            int imageRes = 0;

            switch(coinKey){
                case Coins.TWENTY_FIVE_CENTS:
                    imageRes = R.drawable.twenty_five_cents_128;
                    break;
                case Coins.TEN_CENTS:
                    imageRes = R.drawable.ten_cents_128;
                    break;
                case Coins.FIVE_CENTS:
                    imageRes = R.drawable.five_cents_128;
                    break;
            }
            for(int i = 0; i < coinsMap.get(coinKey); i++){
                linearLayoutReturnCoinTray.addView(getCoinImageView(imageRes));
            }
        }
    }

    private void showInvalidCoin(){
        linearLayoutReturnCoinTray.removeAllViews();
        int imageRes = R.drawable.unknown_coin_128;
        linearLayoutReturnCoinTray.addView(getCoinImageView(imageRes));
    }


    private ImageView getCoinImageView(int imageRes){
        ImageView coinImage = new ImageView(this);
        coinImage.setImageBitmap(resize(imageRes, getImageWidth() / 3));
        coinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanCoinReturnTray();
            }
        });
        return coinImage;
    }

    private void cleanCoinReturnTray(){
        textViewCoinReturn.setText(MONEY_COLLECTED);
        textViewCoinReturn.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCoinReturn.setText(EMPTY_STRING);
            }
        }, DELAY_TIME / 2);
        linearLayoutReturnCoinTray.removeAllViews();
    }

}
