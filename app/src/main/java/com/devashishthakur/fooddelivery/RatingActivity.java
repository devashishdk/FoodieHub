package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity {

    Button rateButton;
    AppCompatRatingBar ratingBar;
    EditText review;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = (AppCompatRatingBar) findViewById(R.id.ratingBar);
        review = (EditText) findViewById(R.id.review);

        rateButton = (Button) findViewById(R.id.rateButton);
        String shopid = getIntent().getStringExtra("shopID");
        final String userid = getIntent().getStringExtra("userID");

        final DatabaseReference dbShop = FirebaseDatabase.getInstance().getReference("Shops").child(shopid);

        //final HashMap<String,HashMap<String,String>> hm = new HashMap<String, HashMap<String,String>>();
        final HashMap<String,String> hmChild = new HashMap<String, String>();

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(RatingActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(true);
                pd.setTitle("Loading....");
                pd.setMessage("Please Wait");
                pd.show();

                String rating = String.valueOf(ratingBar.getRating());
                String rev = review.getText().toString();

                hmChild.put("rating",rating);
                hmChild.put("review",rev);

                dbShop.child("ratinglist").child(userid).setValue(hmChild).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(RatingActivity.this,"Rated Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RatingActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }
}
