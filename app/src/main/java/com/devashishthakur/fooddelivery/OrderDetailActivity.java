package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailActivity extends AppCompatActivity {

    Button confirmOrder;
    AppCompatSpinner spinnerItems;
    String orderNo = "100";
    TextInputLayout hostelName,phoneNumber,roomNo,personName;
    Boolean isLoaded = true;
    ProgressDialog pd;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        final String shopId = getIntent().getStringExtra("shopid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Getting User UID

        final String user_id = firebaseUser.getUid().toString();

        hostelName = (TextInputLayout) findViewById(R.id.order_hostel_name);
        phoneNumber = (TextInputLayout) findViewById(R.id.order_phone_no);
        roomNo = (TextInputLayout) findViewById(R.id.order_room_no);
        personName = (TextInputLayout) findViewById(R.id.order_person_name);

        Intent intent = getIntent();
        final HashMap<String, Integer> hashMapObject = (HashMap<String, Integer>) intent.getSerializableExtra("map");

        final DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("Shops").child(shopId).child("orders");

        final DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        //Maintaining a hashmap of all details of user

        dbUser.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                personName.getEditText().setText(snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        dbUser.child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                phoneNumber.getEditText().setText(snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dbUser.child("room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                roomNo.getEditText().setText(snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dbUser.child("hostel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                hostelName.getEditText().setText(snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        final HashMap<String,String> orderDetails = new HashMap<String, String>();

        //getting order no

        //on click confirm

        confirmOrder = (Button) findViewById(R.id.confirm_order);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLoaded) {

                    pd = new ProgressDialog(OrderDetailActivity.this);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setCancelable(true);
                    pd.setTitle("Loading....");
                    pd.setMessage("Please Wait");
                    pd.show();

                    //Get data

                    String name = personName.getEditText().getText().toString();
                    String hostel = hostelName.getEditText().getText().toString();
                    String phoneNo = phoneNumber.getEditText().getText().toString();
                    String room = roomNo.getEditText().getText().toString();

                    //isNumeric is a user defined function

                    Boolean flagPhone = isNumeric(phoneNo);
                    Boolean flagRoom = isNumeric(room);

                    //Validate the data

                    if(name!=null && hostel!=null && phoneNo!=null &&
                            room!=null && phoneNo.length() == 10 && flagPhone && flagRoom
                            )
                    {

                        orderDetails.put("userid",user_id);
                        orderDetails.put("name",name);
                        //orderDetails.put("item",spinnerItems.getSelectedItem().toString());
                        orderDetails.put("hostel",hostel);
                        orderDetails.put("phone", phoneNumber.getEditText().getText().toString());
                        orderDetails.put("room", roomNo.getEditText().getText().toString());
                        orderDetails.put("status","pending");
                        //Pushing data into the database

                        final String key = dbProducts.child("allorders").push().getKey();
                        orderDetails.put("orderid",key);
                        orderDetails.put("shopid",shopId);

                        dbProducts.child(key).setValue(orderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                dbUser.child("orders").child(key).setValue(orderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(pd.isShowing())
                                            pd.dismiss();
                                        //Sending some information to next activity
                                        Intent intent = new Intent(OrderDetailActivity.this, OrderConfirmedActivity.class);
                                        intent.putExtra("map", hashMapObject);
                                        intent.putExtra("shopid", shopId);
                                        intent.putExtra("orderno", orderNo);
                                        intent.putExtra("orderid",key);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        });
                    }
                    else {

                        //Validation Error Details

                        pd.dismiss();

                        if (!(name != null && hostel != null && phoneNo != null && room != null)) {
                            Toast.makeText(OrderDetailActivity.this, "Enter All the Information", Toast.LENGTH_LONG).show();
                        }
                        else if(!(phoneNo.length() == 10))
                        {
                            Toast.makeText(OrderDetailActivity.this, "Enter Correct Phone No", Toast.LENGTH_LONG).show();
                        }
                        else if(!(flagPhone && flagRoom))
                        {
                            Toast.makeText(OrderDetailActivity.this, "Number and Room should be Numeric", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
