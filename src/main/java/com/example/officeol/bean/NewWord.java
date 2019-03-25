package com.example.officeol.bean;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;


public class NewWord {
    public ArrayList<User> users=new ArrayList<>();
    public String fileId;
    public ArrayList<String> contents=new ArrayList<>();
    public NewWord(String userId,String fileId)
    {
        User user=new User(userId);
        users.add(user);
        this.fileId=fileId;
    }
    public void addUsers(String userId)
    {
        boolean test=true;
        for(User user:users) {
            if(user.getId()==userId) {
                test=false;
                break;
            }
        }
        if(test==true) {
            User user=new User(userId);
            users.add(user);
        }
    }
    public void deleteUser(String userId)
    {
        User user=new User(userId);
        users.remove(user);
    }
    public void addContents(String ops)
    {
        contents.add(ops);
    }
}
