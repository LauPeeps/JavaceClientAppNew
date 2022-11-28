package com.example.javaceclientapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.List;

public class SubmoduleAdapter extends RecyclerView.Adapter<SubmoduleViewholder> {
    static String subId;
    static  String moduleId;
    Submodule submodule;
    List<SubmoduleModel> submoduleModelList;


    public SubmoduleAdapter(Submodule submodule, List<SubmoduleModel> submoduleModelList) {
        this.submodule = submodule;
        this.submoduleModelList = submoduleModelList;
    }

    @NonNull
    @Override
    public SubmoduleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent, false);

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
    }

    @Override
    public int getItemCount() {
        return submoduleModelList.size();
    }
}
