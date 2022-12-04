package com.example.javaceclientapp;

import static com.example.javaceclientapp.MainActivity.userNow;
import static com.example.javaceclientapp.Submodule.moduleIdforMainActivity;
import static com.example.javaceclientapp.Submodule.moduleName;
import static com.example.javaceclientapp.Submodule.uid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmoduleAdapter extends RecyclerView.Adapter<SubmoduleViewholder> {
    static String subId;
    static  String moduleId;
    Submodule submodule;
    List<SubmoduleModel> submoduleModelList;
    FirebaseFirestore firestore;


    public SubmoduleAdapter(Submodule submodule, List<SubmoduleModel> submoduleModelList) {
        this.submodule = submodule;
        this.submoduleModelList = submoduleModelList;
    }

    @NonNull
    @Override
    public SubmoduleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.submodule_item, parent, false);

        SubmoduleViewholder submoduleViewholder = new SubmoduleViewholder(itemView);

        submoduleViewholder.setOnClickListener(new SubmoduleViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {
                subId = submoduleModelList.get(position).getSubmodule_id();
                String subName = submoduleModelList.get(position).getSubmodule_preview();
                moduleId = submodule.moduleId;
                Intent intent = new Intent(submodule, TopicActivity.class);
                intent.putExtra("subid", subId);
                intent.putExtra("subname", subName);
                intent.putExtra("moduleid", moduleId);

                submodule.dualStateProgress(position, subId);
                //submodule.addSubmoduleProgress(moduleId, subId, position);
              //  submodule.increaseProgress(moduleId, subId, position);
                submodule.startActivity(intent);

            }

            @Override
            public void onOneLongClick(View view, int position) {

            }
        });


        return submoduleViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull SubmoduleViewholder holder, int position) {
        holder.subModuleName.setText(submoduleModelList.get(position).getSubmodule_id());


        firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.collection("Users").document(userNow).collection(userNow).document(moduleIdforMainActivity);

        firestore.collection("Quizzes").document(moduleIdforMainActivity).collection(userNow).document("Progress_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Long subsViewed = documentSnapshot.getLong("submodules");
                    firestore.collection("Quizzes").document(moduleIdforMainActivity).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Long subsAvailable = documentSnapshot.getLong("submodules");
                                float data1 = (float) subsViewed;
                                float data2 = (float) subsAvailable;
                                int result = (int) (data1 / data2 * 100);

                                Map<String, Object> data = new HashMap<>();
                                data.put("module_id", moduleIdforMainActivity);
                                data.put("module_name", moduleName);
                                data.put("progress", result);

                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            documentReference.update(data);
                                        } else {
                                            documentReference.set(data);
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });


        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Quizzes").document(submodule.moduleId).collection(uid).document(submoduleModelList.get(position).getSubmodule_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    holder.tractCurrentUserState.setImageResource(R.drawable.check);
                } else {
                    holder.tractCurrentUserState.setImageResource(R.drawable.x);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return submoduleModelList.size();
    }
}
