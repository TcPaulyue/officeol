package com.example.officeol.bean;

import com.alibaba.fastjson.JSON;
import com.example.officeol.service.WebSocket;

import java.util.*;

import static com.example.officeol.config.FileToWebsocket.fileToWebsocket;
import static com.example.officeol.config.JupiterList.jupiterWordList;

public class JupiterUser {
    String fileId;
    String userId;
    public String txt;
    int myMsgs;
    int otherMsgs;
    Queue<Message> outgoing=new LinkedList<>();
    public void printOutGoing(){
        System.out.println("#####userId:"+userId+", myMsg:"+myMsgs+"#####");
        for(Message message : outgoing){
            System.out.println(message.toString());
        }
        System.out.println("---------END----------");
    }
    public JupiterUser(String userId, String fileId)
    {
        this.userId=userId;
        this.fileId=fileId;
        txt=null;
        myMsgs=otherMsgs=0;
    }
    public void editTxt(Message message)
    {
        int position=message.getPosition();
        String operation=message.getOperation();
        String op=message.getMessage();
        if(operation.equals("insert"))
        {
            if(position==0)
            {
                if(txt!=null){
                String s=op+txt;
                txt=s;}
                else
                    txt=op;
            }
            else{
            String old = txt.substring(position);
            txt=txt.substring(0,position);
            txt=txt.concat(op+old);
        //    System.out.println(txt);
            }
        }
        else if(operation.equals("delete"))
        {
            int k=Integer.parseInt(op);
           if(k<position){
                String s1=txt.substring(0,position-k);
                String s2=txt.substring(position);
                txt = s1+s2;
           }
           else if(k==position)
           {
               String s1=txt.substring(position);
               txt=s1;
           }
           else
           {
               System.err.println("k>position error!");
           }
        //    System.out.println(txt);
        }
    }

    public void Generate(Message message)throws Exception
    {
        editTxt(message);
        Message newmessage=new Message(message);

     //   System.out.println("-------START--------");
     //   System.out.println("userId:"+userId);
     //   System.out.println("oldState:"+newmessage.state+",newState:"+otherMsgs);


        newmessage.state=otherMsgs;
        sendMessagetoClient(newmessage,userId);
     //   System.out.println(newmessage.toString());
     //   System.out.println("TO OUTGOING(should be):"+myMsgs);
        newmessage.state=myMsgs;
        outgoing.offer(newmessage);
     //   System.out.println(newmessage.toString());
     //   System.out.println("--------END---------");
        myMsgs++;
    }

    public void Receive(Message message)throws Exception
    {
        int count = 0;
        for(Message message1:outgoing)
        {
            if(message1.state<message.state)
                count++;
        }
        for(int i =0;i<count;i++)
            outgoing.poll();
        printOutGoing();
        for(Message message1:outgoing)
        {
            xForm(message,message1);
        }
        printOutGoing();
       // System.out.println("OT之后： "+message.position+"   "+message.getMessage());
        editTxt(message);
        otherMsgs=otherMsgs+1;
        sendMessagetoOtherServer(message);
    }

    public void sendMessagetoClient(Message message,String userId) throws Exception
    {
        for(WebSocket webSocket:fileToWebsocket.get(message.fileId))
        {
            if(webSocket.userId.equals(userId))
            {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("userId",message.userId);
                map.put("fileId",message.fileId);
                map.put("position",message.getPosition());
                map.put("message",message.getMessage());
                map.put("operation",message.getOperation());
                map.put("state",message.getState());
                webSocket.sendMessage(JSON.toJSONString(map));
//                webSocket.sendMessage(message.toString());
 //               System.out.println("have send message to client!");
            }
        }
    }
    public void sendMessagetoOtherServer(Message message)throws Exception
    {
        for(JupiterUser jupiterUser: jupiterWordList.getJupiterWord(message.fileId).jupiterUsers)
        {
            if(jupiterUser.userId.equals(message.userId)==false)
                jupiterUser.Generate(message);
        }
    }

    public void xForm(Message myMessage,Message otherMessage)
    {
        if(myMessage.getOperation().equals("insert")&&otherMessage.getOperation().equals("insert")){
            if(myMessage.getPosition()<otherMessage.getPosition())
                otherMessage.addPosition(myMessage.getMessage().length());
            else if(myMessage.getPosition()>otherMessage.getPosition())
                myMessage.addPosition(otherMessage.getMessage().length());
            else
            {
                otherMessage.addPosition(myMessage.getMessage().length());
            }
        }
        else if(myMessage.getOperation().equals("insert")&&otherMessage.getOperation().equals("delete")){
            if(myMessage.getPosition()<otherMessage.getPosition())
                otherMessage.addPosition(myMessage.getMessage().length());
            else if(myMessage.getPosition()>otherMessage.getPosition())
                myMessage.deletePosition(otherMessage.getMessage().length());
            else{
                otherMessage.addPosition(myMessage.getMessage().length());
            }
        }
        else if(myMessage.getOperation().equals("delete")&&otherMessage.getOperation().equals("insert")){
            if(myMessage.getPosition()<otherMessage.getPosition())
                otherMessage.deletePosition(myMessage.getMessage().length());
            else if(myMessage.getPosition()>otherMessage.getPosition())
                otherMessage.addPosition(myMessage.getMessage().length());
            else
                otherMessage.deletePosition(myMessage.getMessage().length());
        }
        else if(myMessage.getOperation().equals("delete")&&otherMessage.getOperation().equals("delete")){
            if(myMessage.getPosition()<otherMessage.getPosition())
                otherMessage.deletePosition(myMessage.getMessage().length());
            else if(myMessage.getPosition()>otherMessage.getPosition())
                myMessage.deletePosition(otherMessage.getMessage().length());
            else
                otherMessage.deletePosition(myMessage.getMessage().length());
        }
    }

    public String getTxt() {
        return txt;
    }
}
