package com.bigstride.jobportal_company.Model;

public class SkillsModel {
    private String name;

    public SkillsModel() {
        // Required empty constructor for Firestore
    }

    public SkillsModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
