package com.example.javaceclientapp;


public class SubmoduleModel {
    String submodule_id, submodule_preview;

    public SubmoduleModel(String submodule_id, String submodule_preview) {
        this.submodule_id = submodule_id;
        this.submodule_preview = submodule_preview;
    }

    public String getSubmodule_id() {
        return submodule_id;
    }

    public void setSubmodule_id(String submodule_id) {
        this.submodule_id = submodule_id;
    }

    public String getSubmodule_preview() {
        return submodule_preview;
    }

    public void setSubmodule_preview(String submodule_preview) {
        this.submodule_preview = submodule_preview;
    }
}
