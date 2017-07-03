package com.inducesmile.androidpayexample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.africastalking.ATClient;
import com.africastalking.Callback;
import com.africastalking.Currency;
import com.africastalking.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inducesmile.androidpayexample.adapters.CheckRecyclerViewAdapter;
import com.inducesmile.androidpayexample.entities.ProductObject;
import com.inducesmile.androidpayexample.helpers.MySharedPreference;
import com.inducesmile.androidpayexample.helpers.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = CheckoutActivity.class.getSimpleName();

    private RecyclerView checkRecyclerView;
    public String product = "clothes";
    public String Phone;
    private TextView subTotal;
    public double Total;

    public double mSubTotal = 0;

    private void initSDK(String host, int port, String token)
    {
        ATClient.initialize(host, port, token);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initSDK("192.168.0.141", 3001, "Bz4KD3xZ9");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Over Cart");

        subTotal = (TextView )findViewById(R.id.sub_total);

        checkRecyclerView = (RecyclerView)findViewById(R.id.checkout_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckoutActivity.this);
        checkRecyclerView.setLayoutManager(linearLayoutManager);
        checkRecyclerView.setHasFixedSize(true);
        checkRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(CheckoutActivity.this));

        // get content of cart
        MySharedPreference mShared = new MySharedPreference(CheckoutActivity.this);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        ProductObject[] addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(), ProductObject[].class);
        List<ProductObject> productList = convertObjectArrayToListObject(addCartProducts);

        CheckRecyclerViewAdapter mAdapter = new CheckRecyclerViewAdapter(CheckoutActivity.this, productList);
        checkRecyclerView.setAdapter(mAdapter);

        mSubTotal = getTotalPrice(productList);
        subTotal.setText("Subtotal excluding tax and shipping:\n " + String.valueOf(mSubTotal) + "\n Ksh ");


        Button shoppingButton = (Button)findViewById(R.id.shopping);
        assert shoppingButton != null;
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shoppingIntent = new Intent(CheckoutActivity.this, ShoppingActivity.class);
                startActivity(shoppingIntent);
            }
        });

        Button checkButton = (Button)findViewById(R.id.checkout);
        assert checkButton != null;
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "checkout", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.popup_checkout);
                setTitle("Pay");

                final EditText phone = (EditText)findViewById(R.id.phoneNumber);
                final float amount = (float)mSubTotal;


                final Button btnCheckout = (Button)findViewById(R.id.pay_button);
                btnCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        Phone = phone.getText().toString();
                        Toast toast = Toast.makeText(getApplicationContext(), Phone, Toast.LENGTH_SHORT);
                        toast.show();
                        checkout(Phone,amount);
                    }

                    private void checkout(String Phone,float amount)
                    {
                        Payment payments = ATClient.getPaymentService();
                        payments.checkout(product, Phone, amount, Currency.KES, null, new Callback<String>() {
                            @Override
                            public void onSuccess(String data)
                            {
                                Toast.makeText(getApplicationContext(), "hh", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable throwable)
                            {
                                //Log.e(throwable.getMessage());
                            }
                        });
                    }
                });



            }

        });

    }

    private List<ProductObject> convertObjectArrayToListObject(ProductObject[] allProducts){
        List<ProductObject> mProduct = new ArrayList<ProductObject>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    private int returnQuantityByProductName(String productName, List<ProductObject> mProducts){
        int quantityCount = 0;
        for(int i = 0; i < mProducts.size(); i++){
            ProductObject pObject = mProducts.get(i);
            if(pObject.getProductName().trim().equals(productName.trim())){
                quantityCount++;
            }
        }
        return quantityCount;
    }

    private double getTotalPrice(List<ProductObject> mProducts){
        double totalCost = 0;
        for(int i = 0; i < mProducts.size(); i++){
            ProductObject pObject = mProducts.get(i);
            totalCost = totalCost + pObject.getProductPrice();
        }
        return totalCost;
    }


}
