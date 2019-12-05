package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillDisplayActivity extends AppCompatActivity {

    RecyclerView mBillItems;
    List<BillItem> BillItemList;
    BillItemAdapter billItemAdapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_display);

        pd = new ProgressDialog(BillDisplayActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        String order_id = getIntent().getStringExtra("orderid");

        mBillItems = (RecyclerView) findViewById(R.id.billitemlist);
        mBillItems.setHasFixedSize(true);
        mBillItems.setLayoutManager(new LinearLayoutManager(this));
        BillItemList = new ArrayList<>();
        //Using ShopID to display menu of that shop
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("orders").child(order_id);
        dbProducts.child("bill").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        BillItem p = productSnapshot.getValue(BillItem.class);
                        BillItemList.add(p);
                    }

                    billItemAdapter = new BillItemAdapter(BillDisplayActivity.this, BillItemList);
                    mBillItems.setAdapter(billItemAdapter);
                    pd.dismiss();
                }
                else
                {
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }
}
