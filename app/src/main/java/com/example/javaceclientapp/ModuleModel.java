package com.example.javaceclientapp;

import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.List;

public class ModuleModel {

    public ModuleModel() {};

    String module_id, module_name;
    Timestamp module_created;
    Long submodules, user_data;


    public ModuleModel(String module_id, String module_name, Timestamp module_created, Long submodules, Long user_data) {
        this.module_id = module_id;
        this.module_name = module_name;
        this.module_created = module_created;
        this.submodules = submodules;
        this.user_data = user_data;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public Timestamp getModule_created() {
        return module_created;
    }

    public void setModule_created(Timestamp module_created) {
        this.module_created = module_created;
    }

    public Long getSubmodules() {
        return submodules;
    }

    public void setSubmodules(Long submodules) {
        this.submodules = submodules;
    }

    public Long getUser_data() {
        return user_data;
    }

    public void setUser_data(Long user_data) {
        this.user_data = user_data;
    }
}
