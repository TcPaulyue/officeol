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
        //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
        private static int onlineCount = 0;
        //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
        private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

        private Session session;

        public WordFile wordFile=new WordFile();
        //public static HashMap<String,String> SheetForUser=new HashMap<String, String>();

        public static ArrayList<Word> wordFiles=new ArrayList<>();

        public static HashMap<String,CopyOnWriteArraySet<WebSocket>> FileToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocket>>();

        //public static HashMap<String,CopyOnWriteArraySet<User>> UserForSheet=new HashMap<String, CopyOnWriteArraySet<User>>();

        //public static HashMap<String,CopyOnWriteArraySet<User>> UserForWord=new HashMap<String, CopyOnWriteArraySet<User>>();

        @OnOpen
        public void onOpen( Session session) {
            this.session = session;
            webSocketSet.add(this);     //加入set中
            System.out.println("url是:"+session.getRequestURI());
            String userId=this.getUser(session);
            String fileId=this.getfileId(session);
            if(this.findFile(fileId)==null) {
                addNewFile(fileId,userId);
            }
            else if(findUser(fileId,userId)==false){
                addNewUser(fileId,userId);
            }
            FileToWebsocket.get(fileId).add(this);
            display();
            addOnlineCount();           //在
            //JSONObject jsonObject= JSON.parseObject(message);
            //String sheetId=jsonObject.getString(message);
            //if(this.findKeySheet(sheetId)==null){

            //}
            System.out.println("有新连接加入！线数加1当前在线人数为" + getOnlineCount());
            System.out.println(session.toString());
        }

        @OnClose
        public void onClose() {
            webSocketSet.remove(this);  //从set中删除
            subOnlineCount();           //在线数减1
            System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        }

        @OnError
        public void onError(Session session, Throwable error) {
            System.out.println("发生错误");
            error.printStackTrace();
        }

        public void sendMessage(String message) throws IOException {
            this.session.getBasicRemote().sendText(message);
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
                ContentBlock contentBlock=JSONObject.toJavaObject(JSON.parseObject(textMessage),ContentBlock.class);
                //myMessage.addMessage(contentBlock);
                findFile(fileId).messageset.addMessage(contentBlock);
                Map<String,Object> map1 = new HashMap<String, Object>();
                map1.put("userId",userId);
                //map1.put("textMessage",textMessage);
                map1.put("textMessage",contentBlock);
                //群发消息
                for(WebSocket webSocket:FileToWebsocket.get(fileId)){
                    try {
                        webSocket.sendMessage(JSON.toJSONString(map1));
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
/*                for (User item : UserForWord.get(fileId)) {
                    try {
                        item.webSocket.sendMessage(JSON.toJSONString(map1));
                        //item.sendMessage(JSON.toJSONString(map1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        }

        public static void sendInfo(String message) throws Exception{
            for (WebSocket item : webSocketSet) {
                try {
                    item.sendMessage(message);
                } catch (IOException e) {
                    continue;
                }
            }
        }

        public static synchronized int getOnlineCount() {
            return onlineCount;
        }

        public static synchronized void addOnlineCount() {
            WebSocket.onlineCount++;
        }

        public static synchronized void subOnlineCount() {
            WebSocket.onlineCount--;
        }

        public String getUser(Session session){
            String url=session.getRequestURI().toString();
            System.out.println("000"+url);
            int i=url.lastIndexOf('/');
            url=url.substring(0,i);
            i=url.lastIndexOf('/');
            System.out.println("url:user:"+url.substring(i+1));
            return url.substring(i+1);
        }

        public String getfileId(Session session){
            String url=session.getRequestURI().toString();
            System.out.println("111"+url);
            int i=url.lastIndexOf('/');
            System.out.println("url:file:"+url.substring(i+1));
            return url.substring(i+1);
        }

        public Word findFile(String fileId){
            for(Word word:wordFiles){
                if(word.getWordId().equals(fileId)){
                    return word;
                }
            }
            return null;
        }

        public void addNewFile(String fileId,String userId){
            Word word=new Word(fileId);
            word.users.add(new User(userId));
            wordFiles.add(word);
            CopyOnWriteArraySet<WebSocket> webSockets=new CopyOnWriteArraySet<>();
            webSockets.add(this);
            FileToWebsocket.put(fileId,webSockets);
        }

       public boolean findUser(String fileId,String userId){
            if(findFile(fileId)!=null){
                Word item=findFile(fileId);
                for(User user:item.users){
                    if(user.getId().equals(userId))
                        return true;
                }
            }
            return false;
        }
        public void addNewUser(String fileId,String userId){
            User user=new User(userId);
            findFile(fileId).users.add(user);
        }
        public void display(){
            System.out.println("display:");
            for(Word word:wordFiles){
                System.out.println("fileId:"+word.getWordId()+"   userIds:"+word.getUsers()+"     messages:"+word.messageset);
                for(User user:word.getUsers()){
                    System.out.println(user.getId());
                }
            }
        }
}
