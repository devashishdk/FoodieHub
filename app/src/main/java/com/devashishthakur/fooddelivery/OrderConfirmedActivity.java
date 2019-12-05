package com.devashishthakur.fooddelivery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderConfirmedActivity extends AppCompatActivity {

    Button callButton;
    MyTextView shopPhone;
    FirebaseUser firebaseUser;
    HashMap<String,Integer> hashMap = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Getting User UID

        final String user_id = firebaseUser.getUid().toString();


        //Getting info

        shopPhone = (MyTextView) findViewById(R.id.shopPhone);

        final String shopId = getIntent().getStringExtra("shopid");
        final String orderNo = getIntent().getStringExtra("orderid");
        final DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("Shops").child(shopId);
        final DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

        dbProducts.child("phoneno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                shopPhone.setText("+91"+snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        hashMap = (HashMap<String, Integer>) getIntent().getSerializableExtra("map");
        // Updating in database

        dbProducts.child("orders").child(orderNo).child("bill").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dbUser.child("orders").child(orderNo).child("bill").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });

        callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = shopPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

    }
}
