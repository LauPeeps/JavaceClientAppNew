package com.example.javaceclientapp;


import static com.example.javaceclientapp.MainActivity.userNow;
import static com.example.javaceclientapp.Submodule.uid;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleViewholder> {

    FirebaseFirestore firestore;
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
                String moduleIdGive = moduleModelList.get(position).getModule_id();
                String moduleId = moduleModelList.get(position).getModule_id();
                String moduleName = moduleModelList.get(position).getModule_name();
                Long submodules = moduleModelList.get(position).getSubmodules();

                Intent intent = new Intent(module, Submodule.class);
                intent.putExtra("moduleIdGive", moduleIdGive);
                intent.putExtra("moduleid", moduleId);
                intent.putExtra("modulename", moduleName);
                intent.putExtra("submodules", submodules);

               // module.startProgress(moduleId, position);
                module.createUserCollection(position, moduleId, moduleName);
                module.startActivity(intent);
            }

            @Override
            public void onOneLongClick(View view, int position) {


            }
        });


        return moduleViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull ModuleViewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.moduleName.setText(moduleModelList.get(position).getModule_name());

        firestore = FirebaseFirestore.getInstance();


    }

    @Override
    public int getItemCount() {
        return moduleModelList.size();
    }
}
