package com.example.officeol.bean;

/**
 *
 */
public class User {

    private String id;

    public User(){}
    public User(String userId) {
        this.id=userId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
}
