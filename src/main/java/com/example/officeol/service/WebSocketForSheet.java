package com.example.officeol.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.example.officeol.config.SheetToWebSocket.sheetToWebsocket;
import static com.example.officeol.config.StaticSheetList.sheetList;


@ServerEndpoint(value = "/sheet/{userId}/{fileId}")
@Service
public class WebSocketForSheet {


    private Session session;
    public String userId;
    public String fileId;

    @OnOpen
    public void onOpen( Session session)throws Exception {
        this.session = session;
        this.userId=this.getUser(session);
        this.fileId=this.getfileId(session);
        if(sheetList.addNewSheetOrNot(fileId)==true){
            sheetList.addNewSheet(fileId);
            CopyOnWriteArraySet<WebSocketForSheet> webSocketForSheets =new CopyOnWriteArraySet<>();
            webSocketForSheets.add(this);
            sheetToWebsocket.put(fileId, webSocketForSheets);
            System.out.println("new sheet: "+fileId+" join in! and userId is "+userId);
        }
        else {//add a new user to a existed file
            if(sheetList.getSheet(fileId).addNewUserOrNot(userId)==true) {
                sheetToWebsocket.get(fileId).add(this);
                System.out.println("new user: "+userId+" join in "+"sheet "+fileId);
            }
            else {//sonething need to attact
                System.err.println("error when websocket open");
                sheetToWebsocket.get(fileId).add(this);
            }
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) throws Exception{
        System.out.println("message form: "+message);
        JSONObject jsonObject= JSON.parseObject(message);
        String row=jsonObject.getString("row");
        String col=jsonObject.getString("col");
        String operation=jsonObject.getString("operation");
        Map<String,Object> map=this.setSendMessageForm(row,col,userId,fileId,operation);
        this.sendMessagetoAll(fileId,JSON.toJSONString(map));
        sheetList.getSheet(fileId).displaySheetInfo();
    }

    @OnClose
    public void onClose() {
        sheetToWebsocket.get(fileId).remove(this);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /*
     * get userId from session.url
     * */
    public String getUser(Session session){
        String url=session.getRequestURI().toString();
        //System.out.println("000"+url);
        int i=url.lastIndexOf('/');
        url=url.substring(0,i);
        i=url.lastIndexOf('/');
        //System.out.println("url:user:"+url.substring(i+1));
        return url.substring(i+1);
    }
    /*
     *get fileId from session.url
     *  */
    public String getfileId(Session session){
        String url=session.getRequestURI().toString();
        int i=url.lastIndexOf('/');
        return url.substring(i+1);
    }

    public Map<String,Object> setSendMessageForm(String row,String col,String userId,String fileId,String operation){
        if(sheetList.getSheet(fileId).LockOrNot(row,col,userId)==true){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("fileId",fileId);
            map.put("operation",operation);
            map.put("editable","false");
            return map;
        }
        else{
            if(sheetList.getSheet(fileId).addNewUserOrNot(userId)==true)
                sheetList.getSheet(fileId).addNewUser(row, col, userId);
            else
                sheetList.getSheet(fileId).updataSheet(row,col,userId);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("fileId",fileId);
            map.put("operation",operation);
            map.put("editable","true");
            return map;
        }
    }
    /*
     * 群发消息
     * */
    public void sendMessagetoAll(String fileId,String message)throws IOException{
        int count=0;
        for(WebSocketForSheet webSocketForSheet: sheetToWebsocket.get(fileId)){
            try {
                // System.out.println("用户"+webSocket.getUser(session));
                webSocketForSheet.sendMessage(message);
                count++;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("发送给用户的个数为"+count);
    }
    /*
     * 消息发给除自己之外的所有人
     * */
    public void sendMessagetoothers(String fileId,String message)throws IOException{
        for(WebSocketForSheet webSocketForSheet: sheetToWebsocket.get(fileId)){
            if(webSocketForSheet.equals(this)==false)
                try {
                    webSocketForSheet.sendMessage(message);
                }catch (IOException e){
                    e.printStackTrace();
                }
        }
    }
}
