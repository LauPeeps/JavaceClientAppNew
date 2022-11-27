package com.example.javaceclientapp;

import com.google.firebase.Timestamp;

public class ModuleModel {

    String module_id, module_name, module_preview;
    Timestamp module_created;
    Long submodules;

    public ModuleModel(String module_id, String module_name, String module_preview, Timestamp module_created, Long submodules) {
        this.module_id = module_id;
        this.module_name = module_name;
        this.module_preview = module_preview;
        this.module_created = module_created;
        this.submodules = submodules;
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

    public String getModule_preview() {
        return module_preview;
    }

    public void setModule_preview(String module_preview) {
        this.module_preview = module_preview;
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
}
