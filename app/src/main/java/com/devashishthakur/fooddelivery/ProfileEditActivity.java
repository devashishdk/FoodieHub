package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileEditActivity extends AppCompatActivity {

    MyEditText mMobile,mName,mHostel,mRoom;
    private FirebaseUser mCurrentUser;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgress;
    Button mSaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        String name = getIntent().getStringExtra("name");
        String mob = getIntent().getStringExtra("mob");
        final String hostel = getIntent().getStringExtra("hostel");
        String room = getIntent().getStringExtra("room");

        mSaveButton = (Button) findViewById(R.id.mSaveButton);

        mName = (MyEditText) findViewById(R.id.mName);
        mMobile = (MyEditText) findViewById(R.id.mMobile);
        mRoom = (MyEditText) findViewById(R.id.mRoom);
        mHostel = (MyEditText) findViewById(R.id.mHostel);

        mName.setText(name);
        mMobile.setText(mob);
        mRoom.setText(room);
        mHostel.setText(hostel);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Progress
                mProgress = new ProgressDialog(ProfileEditActivity.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();

                String mob = mMobile.getText().toString();
                String name = mName.getText().toString();
                String room = mRoom.getText().toString();
                String hostel = mHostel.getText().toString();

                mDatabaseReference.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            if(mProgress.isShowing())
                                mProgress.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

                mDatabaseReference.child("hostel").setValue(hostel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            if(mProgress.isShowing())
                                mProgress.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });
                mDatabaseReference.child("phone").setValue(mob).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            if(mProgress.isShowing())
                                mProgress.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });
                mDatabaseReference.child("room").setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            if(mProgress.isShowing())
                                mProgress.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        });
    }
}
