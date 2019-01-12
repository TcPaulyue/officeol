package com.example.officeol.bean;

import com.example.officeol.service.WebSocket;

import java.util.concurrent.CopyOnWriteArraySet;

public class Word {
    private String WordId;

    private CopyOnWriteArraySet<User> users=new CopyOnWriteArraySet<>();

    private Message messageset=new Message();

    public Word(String WordId){
        this.WordId=WordId;
    }

    public String getWordId() {
        return WordId;
    }

    public CopyOnWriteArraySet<User> getUsers() {
        return users;
    }

    public Message getMessageset() {
        return messageset;
    }
}