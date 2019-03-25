package com.example.officeol.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.officeol.bean.*;
import com.example.officeol.repository.UserRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{userId}/{fileId}")
@Service
public class WebSocket {

    @Autowired
    private UserRepository userRepository= (UserRepository) MyApplicationContextAware.getApplicationContext().getBean(UserRepository.class);
    private Session session;

    public static WordFile wordFile=new WordFile();
    //存放所有的word类型的文档信息，包括该文档的所有用户和所有指令
   // public static ArrayList<Word> wordFiles=new ArrayList<>();

    //文档Id和websockets的对应，用以进行分组广播的操作
    public static HashMap<String,CopyOnWriteArraySet<WebSocket>> FileToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocket>>();

    //public static HashMap<String,CopyOnWriteArraySet<User>> UserForSheet=new HashMap<String, CopyOnWriteArraySet<User>>();

    //public static HashMap<String,CopyOnWriteArraySet<User>> UserForWord=new HashMap<String, CopyOnWriteArraySet<User>>();

    @OnOpen
    public void onOpen( Session session)throws Exception {
        this.session = session;
        String userId=this.getUser(session);
        String fileId=this.getfileId(session);
        if(wordFile.findWordOnOpen(fileId)==false) {//open a new file
            NewWord newWord=new NewWord(userId,fileId);
            wordFile.WordFiles.add(newWord);
            System.out.println("新文档"+fileId +"加入!!!");
            CopyOnWriteArraySet<WebSocket> webSockets=new CopyOnWriteArraySet<>();
            webSockets.add(this);
            FileToWebsocket.put(fileId,webSockets);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("mType","onOpen");
            ArrayList<String> messages=new ArrayList<>();
            for(String message:wordFile.getWord(fileId).contents)//重载
                messages.add(message);
            map.put("message",messages);
            this.sendMessage(JSON.toJSONString(map));
            User user = new User(userId);
            userRepository.save(user);
        }
        else if(wordFile.findUserOnOpen(fileId,userId)==false){//a new user join a existed file
            User user=new User(userId);
            wordFile.getWord(fileId).users.add(user);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("mType","onOpen");
            ArrayList<String> messages=new ArrayList<>();
            for(String message:wordFile.getWord(fileId).contents)
                //for(String message:findFile(fileId).getStringMessage())//重载
                messages.add(message);
            map.put("message",messages);
            this.sendMessage(JSON.toJSONString(map));
            System.out.println("新用户"+userId+"加入文档"+fileId+"!!!");
            FileToWebsocket.get(fileId).add(this);
        }
        else {//no new file,no new user
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("mType","onOpen");
            ArrayList<String> messages=new ArrayList<>();
            for(String message:wordFile.getWord(fileId).contents)
                // for(String message:findFile(fileId).getStringMessage())//重载
                messages.add(message);
            map.put("message",messages);
            this.sendMessage(JSON.toJSONString(map));
            FileToWebsocket.get(fileId).add(this);
            System.out.println("文档"+fileId+"有新的消息！！！");
        }
        display();
        System.out.println("文件"+fileId+":当前在线人数为" + getOnlineCount(fileId));
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
        switch(fileType){
            case"word":
                String fileId=jsonObject.getString("fileId");
                String userId=jsonObject.getString("userId");
                String textMessage=jsonObject.getString("message");
                String mType=jsonObject.getString("mType");
                if(mType!=null)//a new user join a existed file
                {
                    Ops ops=new Ops(fileId,userId,textMessage,fileType);
                    wordFile.getWord(ops.getFileId()).contents.clear();
                    wordFile.getWord(ops.getFileId()).contents.add(ops.getMessage());
                    //for(String content:wordFile.getWord(ops.getFileId()).contents)
                      //  System.out.println(content);
                }
                else {
                    try{
                        Ops ops=new Ops(fileId,userId,textMessage,fileType);
                        wordFile.getWord(ops.getFileId()).addContents(ops.getMessage());
                        for(String content:wordFile.getWord(ops.getFileId()).contents)
                            System.out.println(content);
                        FileToWebsocket.get(ops.getFileId()).add(this);
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("message格式错误");
                    }
                    Map<String,Object> map1 = new HashMap<String, Object>();
                    map1.put("userId",userId);
                    map1.put("message",textMessage);
                    this.sendMessagetoAll(fileId,JSON.toJSONString(map1));
                    break;
                }
            case "sheet":
                break;
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
    * show FileToWebsocket
    * */
    public void display(){
        System.out.println("display:======================");
        for(NewWord word:wordFile.WordFiles){
            System.out.println("fileId:"+word.fileId);
            System.out.println("userId:");
            for(User user:word.users){
                System.out.println(user.getId());
            }
            System.out.println("messages:");
            for(String content:word.contents){
                System.out.println(content);
            }
        }
        System.out.println("++++++++++++++++++++++++++++++");
    }
}
