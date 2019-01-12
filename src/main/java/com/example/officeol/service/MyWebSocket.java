/*
package com.example.officeol.service;

import com.alibaba.fastjson.JSONObject;
import com.example.officeol.bean.Message;
import com.example.officeol.bean.Response;
import com.example.officeol.bean.Sheet;
import com.example.officeol.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
//@ServerEndpoint(value = "/ws")

@Component
public class MyWebSocket {
    @Autowired
    //使用SimpMessagingTemplate 向浏览器发送消息
    private SimpMessagingTemplate template;

    //用来记录当前的所有表单
    private ArrayList<Sheet> sheets=new ArrayList<Sheet>();
    //private Message message;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    //private Session session;


    public void addNewSheet(Message message)
    {
        Sheet sheet=new Sheet(message.getSheetId(),1);
        this.sheets.add(sheet);
        System.out.println("Add a new Sheet");
        System.out.println(sheet.sheetId);
    }

    public Sheet findKeySheet(Message message)
    {
        for(int i=0;i<this.sheets.size();i++) {
            if (this.sheets.get(i).getSheetId().compareTo(message.getSheetId()) ==0)
            {
                return this.sheets.get(i);
            }
        }
        return null;
    }
    public User findKeyUser(Message message)
    {
        Sheet sheet=this.findKeySheet(message);
        if(sheet!=null){
            for(int i=0;i<sheet.users.size();i++)
            {
                if(sheet.users.get(i).getId().compareTo(message.getUserId())==0)
                    return sheet.users.get(i);
            }
        }
        return null;
    }

    public void addNewUser(Message message){
        User user=findKeyUser(message);
        if(user==null){
            this.findKeySheet(message).addUser(message.getUserId());
        }
    }

    public void normalOperation(Message message)
    {
        Sheet sheet=this.findKeySheet(message);
        if(sheet!=null) {
            sheet.addMessage(message.getOperation());
            //System.out.println("22222222222222222222"+session.getId());
        }
    }

    public Response sendMessage(Message message)
    {
        JSONObject response=new JSONObject();
        response.put("operation",message.getOperation());
        response.put("url",message.getUrl());
        response.put("sheetId",message.getSheetId());
        response.put("userId",message.getUserId());
        return new Response(response.toJSONString());
    }
    public void display(){
        for(int i=0;i<this.sheets.size();i++){
            System.out.println(sheets.get(i).sheetId+"输出消息：");
            System.out.println("用户队列:");
            int j=0;
            Sheet sheet=this.sheets.get(i);
            for(j=0;j<this.sheets.get(i).users.size();j++)
                System.out.println(sheet.users.get(j).id);
            System.out.println("操作队列:");
            for(j=0;j<this.sheets.get(i).messages.size();j++)
                System.out.println(sheet.messages.get(j));
        }
    }

}
*/
