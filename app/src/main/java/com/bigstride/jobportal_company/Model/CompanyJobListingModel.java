package com.bigstride.jobportal_company.Model;

public class CompanyJobListingModel {
    String job_position;
    String starting_date;
    String apply_before_date;
    String minimum_qualification_required;
    String job_requirement;
    String job_description;
    String document_id;

    public CompanyJobListingModel(String job_position, String starting_date, String apply_before_date, String minimum_qualification_required, String job_requirement, String job_description, String document_id) {
        this.job_position = job_position;
        this.starting_date = starting_date;
        this.apply_before_date = apply_before_date;
        this.minimum_qualification_required = minimum_qualification_required;
        this.job_requirement = job_requirement;
        this.job_description = job_description;
        this.document_id = document_id;
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

}
