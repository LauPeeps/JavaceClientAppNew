package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    TextView goRegister;
    EditText email, password;
    Button loginBtn;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);

        goRegister = findViewById(R.id.goToRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uEmail = email.getText().toString();
                String uPass = password.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
                    email.setError("Invalid Email");
                    email.setFocusable(true);

                } if (uPass.isEmpty()) {
                    password.setError("Please enter your password");
                    password.setFocusable(true);
                }else {
                    loginUser(uEmail, uPass);
                }
            }
        });


        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
               startActivity(intent);
            }
        });

    }

    private void loginUser(String email, String password) {
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        String getEmail = user.getEmail();
                        String getUid = user.getUid();
                        HashMap<Object, String> hashMap = new HashMap<>();
                        hashMap.put("email", getEmail);
                        hashMap.put("uid", getUid);
                        hashMap.put("name", "");
                        hashMap.put("onlineStatus", "online");
                        hashMap.put("typingTo", "noOne");
                        hashMap.put("phone", "");
                        hashMap.put("image", "");
                        hashMap.put("cover", "");

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

                        databaseReference.child(getUid).setValue(hashMap);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "No account exist", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}