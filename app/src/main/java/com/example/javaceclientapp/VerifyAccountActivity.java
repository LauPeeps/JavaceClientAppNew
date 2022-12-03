package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyAccountActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    String globalVerificationCodeForSystem;
    Button userVerifyButton, goToLogin;
    EditText userPhoneVerification;
    ProgressBar progressBar;
    String phoneNumber, userName, userFullName, userEmail, userPassword;
    FirebaseAuth firebaseAuth;
    Dialog progressDialog, resultDialog;
    TextView userNameDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userPhoneVerification = findViewById(R.id.userPhoneVerification);
        userVerifyButton = findViewById(R.id.userVerifyButton);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        userName = getIntent().getStringExtra("userName");
        userFullName = getIntent().getStringExtra("userFullName");
        userEmail = getIntent().getStringExtra("userEmail");
        userPassword = getIntent().getStringExtra("userPassword");

        progressDialog = new Dialog(VerifyAccountActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        resultDialog = new Dialog(VerifyAccountActivity.this);
        resultDialog.setContentView(R.layout.verification_result_dialog);
        resultDialog.setCancelable(false);
        resultDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        resultDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        goToLogin = resultDialog.findViewById(R.id.goToLogins);
        userNameDisplay = resultDialog.findViewById(R.id.welcomeMessage);


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(userName, userFullName, phoneNumber, userEmail, userPassword);
                firebaseAuth.signOut();
                Intent intent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        sendVerificationToUserPhone(phoneNumber);

        userVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typeCode = userPhoneVerification.getText().toString();

                if (typeCode.isEmpty() || typeCode.length() < 6) {
                    userPhoneVerification.setError("Wrong OTP code");
                    userPhoneVerification.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    verifyOTPCode(typeCode);
                }
            }
        });

    }

    private void sendVerificationToUserPhone(String phoneNumber) {
       PhoneAuthProvider.getInstance().verifyPhoneNumber(
               "+63" + phoneNumber,
               60,
               TimeUnit.SECONDS,
               this,
               mCallbacks);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            globalVerificationCodeForSystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String OTPCode = phoneAuthCredential.getSmsCode();
            if (OTPCode != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyOTPCode(OTPCode);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyOTPCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(globalVerificationCodeForSystem, verificationCode);
        signInWithPhoneCredential(credential);
    }
    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

       firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   userNameDisplay.setText("Welcome, " + userFullName);
                   resultDialog.show();
                   Toast.makeText(VerifyAccountActivity.this, "Confirmed user!", Toast.LENGTH_SHORT).show();
               }
           }
       });


    }
    private void createUser(String username, String fullname, String phone, String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();
                DocumentReference documentReference = firestore.collection("Users").document(firebaseUser.getUid());
                Map<String, Object> student_data = new HashMap<>();
                student_data.put("uid", uid);
                student_data.put("username", username);
                student_data.put("fullname", fullname);
                student_data.put("phone", phone);
                student_data.put("email", email);
                student_data.put("user", "yes");
                student_data.put("score", "0");

                documentReference.set(student_data);

                HashMap<Object, String> hashMap = new HashMap<>();
                hashMap.put("uid", uid);
                hashMap.put("username", username);
                hashMap.put("fullname", fullname);
                hashMap.put("phone", phone);
                hashMap.put("email", email);
                hashMap.put("user", "yes");
                hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/javacemahman-10e8a.appspot.com/o/Users_Profile_Cover_image%2Fimage_oucSYp84pETqq8m2VocnmGpm2gF3?alt=media&token=5de45866-0b88-4ad2-988b-2b117a8d44cc");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://javacemahman-10e8a-default-rtdb.firebaseio.com/");
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                databaseReference.child(uid).setValue(hashMap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerifyAccountActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
            }
        });
    }

}