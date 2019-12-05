package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuActivity extends AppCompatActivity {

    RecyclerView mMenuItems;
    List<MenuItem> MenuItemList;
    MenuItemAdapter menuItemAdapter;
    Button makeAnOrder;
    ArrayList<String> productName = new ArrayList<String>();
    ArrayList<String> productPrice = new ArrayList<String>();
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_menu);

        pd = new ProgressDialog(ShopMenuActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        //Getting Shop ID (PUSH ID OF SHOP)

        final String shopId = getIntent().getStringExtra("shopid");

        //On click next pass both arrays to next activity
        //It will be be used for populating spinner
        makeAnOrder = (Button) findViewById(R.id.make_an_order);
        makeAnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShopMenuActivity.this,BillGenerateActivity.class);
                intent.putExtra("productPrice",productPrice);
                intent.putExtra("productName",productName);
                intent.putExtra("shopid",shopId);
                startActivity(intent);
                finish();
            }
        });

        mMenuItems = (RecyclerView) findViewById(R.id.menu_item_list);
        mMenuItems.setHasFixedSize(true);
        mMenuItems.setLayoutManager(new LinearLayoutManager(this));
        MenuItemList = new ArrayList<>();
        //Using ShopID to display menu of that shop
        DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("Shops").child(shopId).child("menu");
        dbProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        MenuItem p = productSnapshot.getValue(MenuItem.class);
                        //Store all Product's name and it's price in an array
                        productName.add(p.getItem());
                        productPrice.add(p.getPrice());
                        MenuItemList.add(p);
                    }

                    menuItemAdapter = new MenuItemAdapter(ShopMenuActivity.this, MenuItemList);
                    mMenuItems.setAdapter(menuItemAdapter);
                    pd.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
