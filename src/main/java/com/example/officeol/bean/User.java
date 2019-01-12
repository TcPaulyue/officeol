package com.example.officeol.bean;

import com.example.officeol.service.WebSocket;

public class User {
    private String id;

    public String name;


    public User(String userId)
    {
        this.id=userId;
    }
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
