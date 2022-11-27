package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Dialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText userName, userFullName, phone, userEmail, userPassword;
    Button registerUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextView regText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regText = findViewById(R.id.registerText);

        userName = findViewById(R.id.userUsername);
        userFullName = findViewById(R.id.userFullName);
        phone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);



        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(RegisterActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        registerUser = findViewById(R.id.userRegBtn);


        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (userName.getText().toString().isEmpty()) {
                        userName.setError("Please enter username");
                        return;
                    } if (userFullName.getText().toString().isEmpty()) {
                        userFullName.setError("Please enter the full name");
                        return;
                    } if (phone.getText().toString().isEmpty()) {
                        phone.setError("Please enter phone number");
                        return;
                    } if (userPassword.getText().toString().isEmpty() || userPassword.getText().toString().length() <= 7) {
                        userPassword.setError("Password should not be empty and more than 7");
                        return;
                    } if (userEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                        userEmail.setError("Invalid email");
                        return;
                    }

                    Intent intent = new Intent(RegisterActivity.this, VerifyAccountActivity.class);
                    intent.putExtra("phoneNumber", phone.getText().toString());
                    intent.putExtra("userName", userName.getText().toString());
                    intent.putExtra("userFullName", userFullName.getText().toString());
                    intent.putExtra("userEmail", userEmail.getText().toString());
                    intent.putExtra("userPassword", userPassword.getText().toString());
                    startActivity(intent);
                }

            });
    }

}