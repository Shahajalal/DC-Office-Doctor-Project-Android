package com.future.it.park.doctorapp;

public class PatientList {
    String user_id,created_at,name;

    public PatientList(String user_id, String created_at, String name) {
        this.user_id = user_id;
        this.created_at = created_at;
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }
}
