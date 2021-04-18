package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    public static final String Email = "email";
    public static final String Password = "password";
    public static final String fileName = "login";

    @SuppressWarnings("TooBroadScope")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }


        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if(sharedPreferences.contains(Email)){
            Intent i = new Intent(Login.this,MainPage.class);
            startActivity(i);
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }


    private Boolean emailValidate(){
        String email = mEmail.getText().toString().replaceAll("@gmail.com","");
        if(email.isEmpty()){
            mEmail.setError("Field cannot be empty");
            return false;
        }else{
            mEmail.setError(null);
            return true;
        }
    }
    private  Boolean passwordValidate(){
        String password = mPassword.getText().toString();
        if(password.isEmpty()){
            mPassword.setError("Field cannot be empty");
            return false;
        }else{
            mPassword.setError(null);
            return true;
        }
    }

    public void loginUser(View view){
        if(!emailValidate() | !passwordValidate()){
            return;
        }else{
            isUser();
        }
    }
    private void isUser(){
        final String userEntertedEmail = mEmail.getText().toString().replaceAll("@gmail.com","");
        final String userEnteredPassword = mPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkuser = reference.orderByChild("email").equalTo(userEntertedEmail);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    mEmail.setError(null);

                    String passwordfromDB = snapshot.child(userEntertedEmail).child("password").getValue(String.class);
                    if(passwordfromDB.equals(userEnteredPassword)){

                        //loggedin
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Email,userEntertedEmail);
                        editor.putString(Password,userEnteredPassword);
                        editor.commit();

                        String emergencyphonefromDB = snapshot.child(userEntertedEmail).child("emergencyphone").getValue(String.class);
                        String fullname = snapshot.child(userEntertedEmail).child("fullname").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(),MainPage.class);
                        intent.putExtra("emergencyphone",emergencyphonefromDB);
                        intent.putExtra("fullName",fullname);

                        startActivity(intent);
                    }else{
                        mPassword.setError("Invalid Password");
                        mPassword.requestFocus();
                    }
                }
                else{
                    mEmail.setError("No such user exists");
                    mEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}



