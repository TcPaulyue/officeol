package com.example.officeol.bean;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class JupiterWord {
    public String fileId;
    public ArrayList<JupiterUser> jupiterUsers=new ArrayList<>();

    public JupiterUser getJupiterUser(String userId)
    {
     for(JupiterUser jupiterUser:jupiterUsers)
     {
         if(jupiterUser.userId.equals(userId))
             return jupiterUser;
     }
     return null;
    }
    public void displayDocTxt()
    {
        for(JupiterUser jupiterUser:jupiterUsers)
        {
            System.out.println("userId: "+jupiterUser.userId+"  mymsgs: "+jupiterUser.myMsgs+"  othermsgs: "+jupiterUser.otherMsgs+"  docinf: "+jupiterUser.getTxt());
        }
    }
}
