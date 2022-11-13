package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ForumDetails extends AppCompatActivity {
    TextView  userName, userTime, forumTitle, forumDescription, forumLikes, forumComm;
    String uName, uDp, imageForum, likes;
    ImageView forumImage, sendComment, imageInComment;
    EditText typeComment;
    ImageButton moreBtn;
    TextView comments;
    RecyclerView recyclerView;
    ActionBar actionBar;
    String postId;
    Boolean makeLike = false;
    String myemail, myuid, myname, mydp;
    List<ModelComment> commentShow;
    AdapterComment adapterComment;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_details);


        postId = getIntent().getStringExtra("pid");

        firebaseAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.userName);
        userTime = findViewById(R.id.userTime);
        forumTitle = findViewById(R.id.forumTitle);
        forumDescription = findViewById(R.id.forumDesc);
        forumLikes = findViewById(R.id.forumLikes);
        forumComm = findViewById(R.id.forumComm);

        typeComment = findViewById(R.id.typeComment);
        sendComment = findViewById(R.id.sendComment);

        moreBtn = findViewById(R.id.moreBtn);

        forumImage = findViewById(R.id.forumImage);

        recyclerView = findViewById(R.id.recycleComment);

        myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressDialog = new ProgressDialog(this);

        loadPostInfo();

        loadUserInfo();


        loadComments();

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

    }


    private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        commentShow = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentShow.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelComment modelComment = dataSnapshot1.getValue(ModelComment.class);
                    commentShow.add(modelComment);
                    adapterComment = new AdapterComment(getApplicationContext(), commentShow, myuid, postId);
                    recyclerView.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    private void postComment() {
        progressDialog.setMessage("Adding Comment");

        String comment = typeComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(ForumDetails.this, "Empty comment", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.show();
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference datarf = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("commentId", timestamp);
        hashMap.put("comment", comment);
        hashMap.put("timePosted", timestamp);
        hashMap.put("uid", myuid);
        hashMap.put("email", myemail);
        hashMap.put("commenter", myname);

        datarf.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(ForumDetails.this, "Added", Toast.LENGTH_LONG).show();
                typeComment.setText("");
                updateComment();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ForumDetails.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
    boolean count = false;

    private void updateComment() {
        count = true;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (count) {
                    String comments = "" + dataSnapshot.child("comments").getValue();
                    int newComment = Integer.parseInt(comments) + 1;
                    reference.child("comments").setValue("" + newComment);
                    count = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {

        Query myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.orderByChild("uid").equalTo(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    myname = dataSnapshot1.child("name").getValue().toString();
                    mydp = dataSnapshot1.child("image").getValue().toString();
                    try {
                        Glide.with(ForumDetails.this).load(mydp).into(imageInComment);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadPostInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = databaseReference.orderByChild("timePosted").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String commentExist = dataSnapshot1.child("comments").getValue().toString();
                    String description = dataSnapshot1.child("description").getValue().toString();
                    String email = dataSnapshot1.child("email").getValue().toString();
                    String likes = dataSnapshot1.child("likes").getValue().toString();
                    String name = dataSnapshot1.child("name").getValue().toString();
                    String timePosted = dataSnapshot1.child("timePosted").getValue().toString();
                    String title = dataSnapshot1.child("title").getValue().toString();
                    String uid = dataSnapshot1.child("uid").getValue().toString();
                    String userImage = dataSnapshot1.child("userImage").getValue().toString();

                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(timePosted));
                    String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                    userName.setText(name);
                    userTime.setText(timedate);
                    forumDescription.setText(description);
                    forumTitle.setText(title);
                    forumLikes.setText(likes + " Likes");
                    forumComm.setText(commentExist + " Comments");

                    if (userImage.equals("noImage")) {
                        forumImage.setVisibility(View.GONE);
                    } else {
                        forumImage.setVisibility(View.VISIBLE);
                        try {
                            Glide.with(ForumDetails.this).load(userImage).into(forumImage);
                        } catch (Exception e) {

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}