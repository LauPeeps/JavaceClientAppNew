package com.example.javaceclientapp;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class ModuleAdapter extends RecyclerView.Adapter<ModuleViewholder> {

    Module module;
    List<ModuleModel> moduleModelList;


    public ModuleAdapter(Module module, List<ModuleModel> moduleModelList) {
        this.module = module;
        this.moduleModelList = moduleModelList;
    }

    @NonNull
    @Override
    public ModuleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent, false);

        ModuleViewholder moduleViewholder = new ModuleViewholder(itemView);

        moduleViewholder.setOnClickListener(new ModuleViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {
                String moduleId = moduleModelList.get(position).getModule_id();
                String moduleName = moduleModelList.get(position).getModule_name();
                Long submodules = moduleModelList.get(position).getSubmodules();

                Intent intent = new Intent(module, Submodule.class);
                intent.putExtra("moduleid", moduleId);
                intent.putExtra("modulename", moduleName);
                intent.putExtra("submodules", submodules);

                module.startActivity(intent);
            }

            @Override
            public void onOneLongClick(View view, int position) {


            }
        });

        return moduleViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull ModuleViewholder holder, int position) {
        holder.moduleName.setText(moduleModelList.get(position).getModule_name());
        holder.modulePreview.setText(moduleModelList.get(position).getModule_preview());

    }

    @Override
    public int getItemCount() {
        return moduleModelList.size();
    }
}
