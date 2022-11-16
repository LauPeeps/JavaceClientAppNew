package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password;
    Button registerBtn;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.register);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = username.getText().toString();
                String getEmail = email.getText().toString();
                String getPass = password.getText().toString();

                if (getUsername.isEmpty()) {
                    username.setError("Please enter username");
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
                    email.setError("Invalid email");
                    email.setFocusable(true);
                } if (getPass.length() <= 7) {
                    password.setError("Password must be at least 8 characters ");
                    password.setFocusable(true);
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(getEmail,getPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Successfully registered the account", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = firestore.collection("Users").document(firebaseUser.getUid());
                            String uids = firebaseUser.getUid();
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("name", getUsername);
                            userInfo.put("email", getEmail);
                            userInfo.put("user", "yes");
                            userInfo.put("uid", uids);
                            userInfo.put("score", 0);

                            documentReference.set(userInfo);

                            FirebaseUser firebaseUsers = firebaseAuth.getCurrentUser();
                            assert firebaseUsers != null;
                            String uid = firebaseUsers.getUid();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", getEmail);
                            hashMap.put("name", getUsername);
                            hashMap.put("uid", uid);
                            hashMap.put("user", "yes");
                            hashMap.put("image", "");

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://javacemahman-10e8a-default-rtdb.firebaseio.com/");
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(uid).setValue(hashMap);


                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}