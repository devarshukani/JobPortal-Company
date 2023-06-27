package com.bigstride.jobportal_company.Model;

import java.util.ArrayList;
import java.util.List;

public class CompanyJobListingModel {
    String job_position;
    String starting_date;
    String apply_before_date;
    String minimum_qualification_required;
    String job_requirement;
    String job_description;
    String job_type;
    String experience_required;
    String document_id;
    ArrayList<String> required_skills = new ArrayList<>();

    public CompanyJobListingModel(String job_position, String starting_date, String apply_before_date, String minimum_qualification_required, String job_requirement, String job_description, String job_type, String experience_required,ArrayList<String> required_skills,  String document_id) {
        this.job_position = job_position;
        this.starting_date = starting_date;
        this.apply_before_date = apply_before_date;
        this.minimum_qualification_required = minimum_qualification_required;
        this.job_requirement = job_requirement;
        this.job_description = job_description;
        this.document_id = document_id;
        this.job_type = job_type;
        this.experience_required = experience_required;
        this.required_skills = required_skills;
    }

    public String getJob_position() {
        return job_position;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public String getApply_before_date() {
        return apply_before_date;
    }

    public String getMinimum_qualification_required() {
        return minimum_qualification_required;
    }

    public String getJob_requirement() {
        return job_requirement;
    }

    public String getJob_description() {
        return job_description;
    }

    public String getDocument_id() {
        return document_id;
    }

    public String getJob_type() {
        return job_type;
    }

    public String getExperience_required() {
        return experience_required;
    }

    public ArrayList<String> getRequired_skills() {
        return required_skills;
    }
}
