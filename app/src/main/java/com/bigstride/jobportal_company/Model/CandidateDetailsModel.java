package com.bigstride.jobportal_company.Model;

public class CandidateDetailsModel {
    String user_id;
    String full_name;
    String date_of_birth;
    String gender;
    String contact_no;
    String email;
    String summary;
    String address;
    String application_status;

    public CandidateDetailsModel(String user_id, String full_name,String date_of_birth,  String gender, String contact_no, String email, String summary, String address, String application_status) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.contact_no = contact_no;
        this.email = email;
        this.summary = summary;
        this.address = address;
        this.application_status = application_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getEmail() {
        return email;
    }

    public String getSummary() {
        return summary;
    }

    public String getAddress() {
        return address;
    }

    public String getApplication_status() {
        return application_status;
    }
}
