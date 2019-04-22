package com.example.officeol.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.officeol.bean.*;
//import com.example.officeol.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.example.officeol.config.JupiterList.jupiterWordList;
import static com.example.officeol.config.WordToWebsocket.wordToWebsocket;

@ServerEndpoint(value = "/word/{userId}/{fileId}")
@Service
public class WebSocketForWord {

    //@Autowired
    //private UserRepository userRepository= (UserRepository) MyApplicationContextAware.getApplicationContext().getBean(UserRepository.class);

    private Session session;
    public String userId;
    public String fileId;

    //public static JupiterWordList jupiterWordList=new JupiterWordList();
    //文档Id和websockets的对应，用以进行分组广播的操作
    //public static HashMap<String,CopyOnWriteArraySet<WebSocket>> WordToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocket>>();


    @OnOpen
    public void onOpen( Session session)throws Exception {
        this.session = session;
        this.userId=this.getUser(session);
        this.fileId=this.getfileId(session);
        if(jupiterWordList.addNewJupiterWordorNot(fileId)==true){
            jupiterWordList.addNewJupiterWord(fileId,userId);
            jupiterWordList.addNewJupiterUser(fileId,"0");//新建文档时加入副本
            CopyOnWriteArraySet<WebSocketForWord> webSocketForWords =new CopyOnWriteArraySet<>();
            webSocketForWords.add(this);
            wordToWebsocket.put(fileId, webSocketForWords);
            System.out.println("new doc: "+fileId+" join in! and userId is "+userId);
        }
        else {//add a new user to a existed file
            if(jupiterWordList.getJupiterWord(fileId).getJupiterUser(userId)==null) {
                jupiterWordList.addNewJupiterUser(fileId,userId);
                jupiterWordList.getJupiterWord(fileId).getJupiterUser(userId).txt=
                        jupiterWordList.getJupiterWord(fileId).getJupiterUser("0").txt;
                wordToWebsocket.get(fileId).add(this);
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("userId","0");
                map.put("fileId",fileId);
                map.put("position",0);
                map.put("message",jupiterWordList.getJupiterWord(fileId).getJupiterUser(userId).txt);
                map.put("operation","insert");
                map.put("state",0);
                this.sendMessage(JSON.toJSONString(map));
                System.out.println("new user: "+userId+" join in "+"doc "+fileId);
            }
            else {//sonething need to attact
                System.err.println("error when websocket open");
                wordToWebsocket.get(fileId).add(this);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception{
        System.out.println("message form: "+message);
        JSONObject jsonObject= JSON.parseObject(message);
        String fileId0=jsonObject.getString("fileId");
        String userId0=jsonObject.getString("userId");
        int position=jsonObject.getInteger("position");
        String operation=jsonObject.getString("operation");
        String message1=jsonObject.getString("message");
        int state=jsonObject.getInteger("state");
        if(this.fileId.equals(fileId0)==false||this.userId.equals(userId0)==false) {
            System.err.println("fileId or userId doesnt pair");
        }
        Message newMessage=new Message(fileId,userId,position,operation,message1,state);
        jupiterWordList.getJupiterWord(fileId).getJupiterUser(userId).Receive(newMessage);
        jupiterWordList.getJupiterWord(fileId).displayDocTxt();
    }

    /*
    * the function of sending message to client
    * */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /*
     * compute user numbers
     * */
    public static synchronized int getOnlineCount(String fileId) {
        return wordToWebsocket.get(fileId).size();
    }

    @OnClose
    public void onClose() {
        wordToWebsocket.get(fileId).remove(this);
        jupiterWordList.getJupiterWord(fileId).jupiterUsers.remove
                (jupiterWordList.getJupiterWord(fileId).getJupiterUser(userId));
        System.out.println(this.getfileId(session)+":有一连接关闭！当前在线人数为"+getOnlineCount(this.getfileId(session)));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("Websocket发生错误");
        error.printStackTrace();
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
        /*
     * 群发消息
     * */
    public void sendMessagetoAll(String fileId,String message)throws IOException{
        for(WebSocketForWord webSocketForWord :wordToWebsocket.get(fileId)){
            try {
                webSocketForWord.sendMessage(message);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * 消息发给除自己之外的所有人
     * */
    public void sendMessagetoothers(String fileId,String message)throws IOException{
        for(WebSocketForWord webSocketForWord :wordToWebsocket.get(fileId)){
            if(webSocketForWord.equals(this)==false)
                try {
                    webSocketForWord.sendMessage(message);
                }catch (IOException e){
                    e.printStackTrace();
                }
        }
    }
}
