package com.example.javaceclientapp;


import static com.example.javaceclientapp.MainActivity.userNow;
import static com.example.javaceclientapp.Submodule.uid;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Timestamp timestamp = moduleModelList.get(position).getModule_created();
                Long submodules = moduleModelList.get(position).getSubmodules();

                Intent intent = new Intent(module, Submodule.class);
                intent.putExtra("moduleIdGive", moduleIdGive);
                intent.putExtra("moduleid", moduleId);
                intent.putExtra("modulename", moduleName);
                intent.putExtra("submodules", submodules);

               // module.startProgress(moduleId, position);
                module.createUserCollection(position, moduleId, moduleName, timestamp);
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

        module.fetchOverallProgress();

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Quizzes").document(moduleModelList.get(position).getModule_id()).collection(userNow).document("Progress_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    holder.progressValue.setText("0%");
                } else {
                    Long subsViewed = documentSnapshot.getLong("submodules");
                    firestore.collection("Quizzes").document(moduleModelList.get(position).getModule_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Long subsAvailable = documentSnapshot.getLong("submodules");

                                float data1 = (float) subsViewed;
                                float data2 = (float) subsAvailable;
                                int result = (int) (data1 / data2 * 100);
                                holder.progressValue.setText(String.valueOf(result) + "%");

                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return moduleModelList.size();
    }
}
