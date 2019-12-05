package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView mOrderList;
    List<Order> OrderList;
    OrderAdapter OrderAdapter;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    Switch switchOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        switchOrder = (Switch) findViewById(R.id.switchOrder);

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(OrdersActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();



        mOrderList = (RecyclerView) findViewById(R.id.order_list);
        mOrderList.setHasFixedSize(false);
        mOrderList.setLayoutManager(new LinearLayoutManager(this));

        OrderList = new ArrayList<>();

        firebaseLoad();

        switchOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                clear();

                if(isChecked == false)
                    firebaseLoad();
                else
                {
                    Intent intent = new Intent(OrdersActivity.this,AllOrdersActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });

    }

    public void firebaseLoad()
    {
        try {
            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = current_user.getUid();

            DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("orders");

            Query searchQuerry = dbProducts.orderByChild("status").equalTo("pending");

            searchQuerry.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        int pos = 0;
                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            Order p = productSnapshot.getValue(Order.class);
                            OrderList.add(p);
                        }

                        OrderAdapter = new OrderAdapter(OrdersActivity.this, OrderList);
                        mOrderList.setAdapter(OrderAdapter);
                        pd.dismiss();

                    }
                    else
                    {
                        pd.dismiss();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e)
        {
            Intent startIntent = new Intent(OrdersActivity.this,MainActivity.class);
            startActivity(startIntent);
            finish();
        }
    }

    public void clear() {

        if (OrderList.isEmpty() == false) {
            final int size = OrderList.size();
            OrderList.clear();
            OrderAdapter.notifyItemRangeRemoved(0, size);
        }

    }

}
