package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout LoginText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText userName, userID, userPass, userRoom, userHostel, userPhone;
    Button signupbutton;
    ProgressDialog pd;
    //String mVerificationId;
    //PhoneAuthProvider.ForceResendingToken mResendToken;
    //PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Auth Instance
        mAuth = FirebaseAuth.getInstance();

        //initFireBaseCallbacks();

        LoginText = (LinearLayout) findViewById(R.id.LoginText);
        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userID = (EditText) findViewById(R.id.UserId);
        userName = (EditText) findViewById(R.id.UserName);
        userPass = (EditText) findViewById(R.id.UserPass);
        userHostel = (EditText) findViewById(R.id.hostel);
        userRoom = (EditText) findViewById(R.id.room);
        userPhone = (EditText) findViewById(R.id.Phone);

        signupbutton = (Button) findViewById(R.id.SignUpButton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String name = userName.getText().toString();
                String userId = userID.getText().toString();
                String pass = userPass.getText().toString();

                //register user
                registerUser(userId,pass);

                /*
                //PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhone.getText().toString(),1000,TimeUnit.MILLISECONDS,SignUpActivity.this,mCallbacks);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        userPhone.getText().toString(),        // Phone number to verify
                        1,                 // Timeout duration
                        TimeUnit.MINUTES,   // Unit of timeout
                        SignUpActivity.this,               // Activity (for callback binding)
                        mCallbacks);
                // OnVerificationStateChangedCallbacks


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("PASSWORD");
                alertDialog.setCancelable(false);

                // Setting Dialog Message
                alertDialog.setMessage("Enter Password");
                final EditText input = new EditText(SignUpActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verifyOTP(input.getText().toString());
                    }
                });

                alertDialog.setNegativeButton("RESEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                userPhone.getText().toString(),        // Phone number to verify
                                1  ,               // Timeout duration
                                TimeUnit.MINUTES,   // Unit of timeout
                                SignUpActivity.this,               // Activity (for callback binding)
                                mCallbacks,         // OnVerificationStateChangedCallbacks
                                mResendToken);             // Force Resending Token from callbacks
                    }
                });
                alertDialog.show();
            */
            }

        });

    }

    void registerUser(String email, String password) {

        //Register user with this email and pass
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Updating data in firebase
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            //Getting UID of user
                            String uid = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            String device_token = FirebaseInstanceId.getInstance().getToken();

                            //Making a hashmap

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", userName.getText().toString());
                            userMap.put("phone", userPhone.getText().toString());
                            userMap.put("hostel", userHostel.getText().toString());
                            userMap.put("room", userRoom.getText().toString());
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");
                            userMap.put("device_token", device_token);

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        pd.dismiss();

                                        //On successfull move to main

                                        Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();

                                    }

                                }
                            });

                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


/*
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(SignUpActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignUpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(SignUpActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                mResendToken = token;
                mVerificationId = verificationId; //Add this line to save //verification Id
            }
        };
    }

    void verifyOTP(String otp) {

        pd = new ProgressDialog(SignUpActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (task.isSuccessful()) {

                                //Updating data in firebase
                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                //Getting UID of user
                                String uid = current_user.getUid();

                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                String device_token = FirebaseInstanceId.getInstance().getToken();

                                //Making a hashmap

                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("name", userName.getText().toString());
                                userMap.put("phone", userPhone.getText().toString());
                                userMap.put("hostel", userHostel.getText().toString());
                                userMap.put("room", userRoom.getText().toString());
                                userMap.put("image", "default");
                                userMap.put("thumb_image", "default");
                                userMap.put("device_token", device_token);

                                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            pd.dismiss();

                                            //On successfull move to main

                                            Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();

                                        }

                                    }
                                });

                            } else {
                                pd.dismiss();
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                            Toast.makeText(SignUpActivity.this, "Verification Success", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUpActivity.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {


            }
        });
        {
        }
    }
    */
}
