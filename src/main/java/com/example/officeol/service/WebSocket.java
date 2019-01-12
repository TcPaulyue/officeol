package com.example.officeol.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.example.officeol.bean.Sheet;
import com.example.officeol.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

    @ServerEndpoint(value = "/websocket/{userId}/{fileId}")
    @Component
    public class WebSocket {

        private Session session;

        public WordFile wordFile=new WordFile();
        //public static HashMap<String,String> SheetForUser=new HashMap<String, String>();

        //存放所有的word类型的文档信息，包括该文档的所有用户和所有指令
        public static ArrayList<Word> wordFiles=new ArrayList<>();

        //文档Id和websockets的对应，用以进行分组广播的操作
        public static HashMap<String,CopyOnWriteArraySet<WebSocket>> FileToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocket>>();

        //public static HashMap<String,CopyOnWriteArraySet<User>> UserForSheet=new HashMap<String, CopyOnWriteArraySet<User>>();

        //public static HashMap<String,CopyOnWriteArraySet<User>> UserForWord=new HashMap<String, CopyOnWriteArraySet<User>>();

        @OnOpen
        public void onOpen( Session session) {
            this.session = session;
            //webSocketSet.add(this);     //加入set中
            //System.out.println("url是:"+session.getRequestURI());
            String userId=this.getUser(session);
            String fileId=this.getfileId(session);
            if(this.findFile(fileId)==null) {//open a new file
                addNewFile(fileId,userId);
            }
            else if(findUser(fileId,userId)==false){//a new user join a existed file
                addNewUser(fileId,userId);
            }
            else {
                FileToWebsocket.get(fileId).add(this);
            }
            display();
            //System.out.println("有新连接加入！线数加1当前在线人数为"+FileToWebsocket.get(fileId).size());
            System.out.println(fileId+":有新连接加入！当前在线人数为" + getOnlineCount(fileId));
        }

        @OnClose
        public void onClose() {
            FileToWebsocket.get(this.getfileId(session)).remove(this);
            System.out.println(this.getfileId(session)+":有一连接关闭！当前在线人数为"+getOnlineCount(this.getfileId(session)));
        }

        @OnError
        public void onError(Session session, Throwable error) {
            System.out.println("发生错误");
            error.printStackTrace();
        }

        @OnMessage
        public void onMessage(String message, Session session) throws Exception{
            System.out.println("来自客户端的消息:" + message);
            JSONObject jsonObject= JSON.parseObject(message);
            String fileType=jsonObject.getString("fileType");
            if(fileType.equals("word")){
                String fileId=jsonObject.getString("fileId");
                String userId=jsonObject.getString("userId");
                String textMessage=jsonObject.getString("message");
                ContentBlock contentBlock=null;
                try{
                    contentBlock=JSONObject.toJavaObject(JSON.parseObject(textMessage),ContentBlock.class);
                    //myMessage.addMessage(contentBlock);
                    findFile(fileId).getMessageset().addMessage(contentBlock);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("message格式错误");
                }
                Map<String,Object> map1 = new HashMap<String, Object>();
                map1.put("userId",userId);
                //map1.put("textMessage",textMessage);
                map1.put("textMessage",contentBlock);
                this.sendMessagetoothers(fileId,JSON.toJSONString(map1));
                //this.sendMessagetoAll(fileId,JSON.toJSONString(map1));
            }
        }
        /*
        * 群发消息
        * */
        public void sendMessagetoAll(String fileId,String message)throws IOException{
            for(WebSocket webSocket:FileToWebsocket.get(fileId)){
                try {
                    webSocket.sendMessage(message);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        /*
        * 消息发给除自己之外的所有人
        * */
        public void sendMessagetoothers(String fileId,String message)throws IOException{
            for(WebSocket webSocket:FileToWebsocket.get(fileId)){
                if(webSocket.equals(this)==false)
                try {
                    webSocket.sendMessage(message);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        public void sendMessage(String message) throws IOException {
            this.session.getBasicRemote().sendText(message);
        }
        /*
        * compute user numbers
        * */
        public static synchronized int getOnlineCount(String fileId) {
            return FileToWebsocket.get(fileId).size();
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
            System.out.println("url:user:"+url.substring(i+1));
            return url.substring(i+1);
        }
        /*
         *get fileId from session.url
          *  */
        public String getfileId(Session session){
            String url=session.getRequestURI().toString();
            System.out.println("111"+url);
            int i=url.lastIndexOf('/');
            System.out.println("url:file:"+url.substring(i+1));
            return url.substring(i+1);
        }

        /*
        * file existed or not
        * */
        public Word findFile(String fileId){
            for(Word word:wordFiles){
                if(word.getWordId().equals(fileId)){
                    return word;
                }
            }
            return null;
        }

        /*
        * add a new file in the FileToWebsocket
        * */
        public void addNewFile(String fileId,String userId){
            Word word=new Word(fileId);
            word.getUsers().add(new User(userId));
            wordFiles.add(word);
            CopyOnWriteArraySet<WebSocket> webSockets=new CopyOnWriteArraySet<>();
            webSockets.add(this);
            FileToWebsocket.put(fileId,webSockets);
        }
        /*
        * find user in the existed file
        * */
       public boolean findUser(String fileId,String userId){
            if(findFile(fileId)!=null){
                Word item=findFile(fileId);
                for(User user:item.getUsers()){
                    if(user.getId().equals(userId))
                        return true;
                }
            }
            return false;
        }
        /*
        * add a new user in the existed file
        * */
        public void addNewUser(String fileId,String userId){
            User user=new User(userId);
            findFile(fileId).getUsers().add(user);
            FileToWebsocket.get(fileId).add(this);
        }
        /*
        * show FileToWebsocket
        * */
        public void display(){
            System.out.println("display:");
            for(Word word:wordFiles){
                System.out.println("fileId:"+word.getWordId()+"   userIds:"+word.getUsers()+"     messages:"+word.getMessageset());
                for(User user:word.getUsers()){
                    System.out.println(user.getId());
                }
            }
        }
}
