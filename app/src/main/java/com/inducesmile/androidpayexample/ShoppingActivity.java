package com.inducesmile.androidpayexample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.inducesmile.androidpayexample.adapters.ShopRecyclerViewAdapter;
import com.inducesmile.androidpayexample.entities.ProductObject;
import com.inducesmile.androidpayexample.helpers.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {

    private static final String TAG = ShoppingActivity.class.getSimpleName();

    private RecyclerView shoppingRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shoppingRecyclerView = (RecyclerView)findViewById(R.id.product_list);
        GridLayoutManager mGrid = new GridLayoutManager(ShoppingActivity.this, 2);
        shoppingRecyclerView.setLayoutManager(mGrid);
        shoppingRecyclerView.setHasFixedSize(true);
        shoppingRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 12, false));

        ShopRecyclerViewAdapter shopAdapter = new ShopRecyclerViewAdapter(ShoppingActivity.this, getAllProductsOnSale());
        shoppingRecyclerView.setAdapter(shopAdapter);
    }


    private List<ProductObject> getAllProductsOnSale(){
        List<ProductObject> products = new ArrayList<ProductObject>();
        products.add(new ProductObject(1, "Sleek Yellow Dress", R.drawable.product1, "Beautiful sleek black top for casual outfit and evening walk", 800, 38, "Black"));
        products.add(new ProductObject(1, "Flare White Denim", R.drawable.product2, "Beautiful sleek black top for casual outfit and evening walk", 800, 38, "Black"));
        products.add(new ProductObject(1, "Flare White Blouse", R.drawable.product3, "Beautiful sleek black top for casual outfit and evening walk", 700, 38, "White"));
        products.add(new ProductObject(1, "Blue Swed Suit", R.drawable.product4, "Beautiful sleek black top for casual outfit and evening walk", 500, 38, "Dark Blue"));
        products.add(new ProductObject(1, "Spotted Grey African", R.drawable.product5, "Beautiful sleek black top for casual outfit and evening walk", 600, 38, "Spotted Green"));
        products.add(new ProductObject(1, "Satin Blue Top", R.drawable.product6, "Beautiful sleek black top for casual outfit and evening walk", 500, 38, "Multi-color"));
        return products;
    }
}
