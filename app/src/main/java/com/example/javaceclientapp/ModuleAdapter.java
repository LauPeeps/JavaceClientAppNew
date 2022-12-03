package com.example.javaceclientapp;


import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


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

               // module.startProgress(moduleId, position);
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

            if (moduleModelList.get(position).getUser_data() == null) {

            } else {
                float data1 = moduleModelList.get(position).getUser_data();
                float data2 = moduleModelList.get(position).getSubmodules();
                float result = data1 / data2;
                int finalResult = (int) (result * 100);
                holder.progressValue.setText(String.valueOf(finalResult) + "%");
                holder.progressBar.setProgress(finalResult);
            }

    }

    @Override
    public int getItemCount() {
        return moduleModelList.size();
    }
}
