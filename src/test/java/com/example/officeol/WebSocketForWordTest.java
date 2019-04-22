package com.example.officeol;

import com.example.officeol.bean.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.example.officeol.config.JupiterList.jupiterWordList;

@RunWith(SpringJUnit4ClassRunner.class)
public class WebSocketForWordTest {
    public Message message;
    public Message message1;
    public Message message2;
    //public JupiterWordList jupiterWordList=new JupiterWordList();
    @Test
    public void testWebsocket()throws Exception
    {
        message=new Message("1","2",0,"insert","aa",0);
        message1=new Message("1","1",0,"insert","bb",1);
        message2=new Message("1","2",1,"insert","cc",1);
        jupiterWordList.addNewJupiterWord("1","2");
        jupiterWordList.addNewJupiterUser("1","1");
        jupiterWordList.addNewJupiterUser("1","3");
//        WebSocket webSocket=new WebSocket();
//        webSocket.userId="2";
//        webSocket.fileId="1";
//        CopyOnWriteArraySet<WebSocket> webSockets=new CopyOnWriteArraySet<>();
//        webSockets.add(webSocket);
//        fileToWebsocket.put("1",webSockets);
//        WebSocket webSocket1=new WebSocket();
//        webSocket1.fileId="1";
//        webSocket1.userId="1";
//        fileToWebsocket.get("1").add(webSocket1);
//        WebSocket webSocket2=new WebSocket();
//        webSocket2.fileId="1";
//        webSocket2.userId="3";
//        fileToWebsocket.get("1").add(webSocket2);
        jupiterWordList.getJupiterWord("1").getJupiterUser("2").Receive(message);
        jupiterWordList.getJupiterWord("1").getJupiterUser("1").Receive(message1);
        jupiterWordList.getJupiterWord("1").getJupiterUser("2").Receive(message2);
        jupiterWordList.getJupiterWord("1").displayDocTxt();
    }

}
