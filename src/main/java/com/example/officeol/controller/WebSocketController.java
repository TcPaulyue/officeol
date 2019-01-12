package com.example.officeol.controller;

import com.example.officeol.bean.Message;
import com.example.officeol.bean.Response;
//import com.example.officeol.service.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@Controller
public class WebSocketController {
//    @Autowired
//    //private WebSocketService ws;
//    private MyWebSocket ws;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @RequestMapping(value = "/ws")
//    public String ws() {
//        return "ws";
//    }

    /*    @RequestMapping(value = "/websocket/{username}")
        public String websocket(@PathVariable("username") String username){
            return "websocket"+username;
        }*/
    @RequestMapping(value = "/websocket/{userId}/{fileId}")
    public String websocket(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId, Model model) {
       try{
           model.addAttribute("userId",userId);
           model.addAttribute("fileId",fileId);
            return "websocket";
       }catch (Exception e){
           //logger.info("跳转到websocket的页面上发生异常，异常信息是："+e.getMessage());
           return "error";


       }
    }

    //http://localhost:8080/ws
  //  @MessageMapping("/welcome")//浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    //服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
//    @SendTo("/topic/getResponse")
//    public Response say(Message message) throws Exception {
//        Thread.sleep(100);
//        if (ws.findKeySheet(message) == null) {
//            ws.addNewSheet(message);
//        }
//        if (ws.findKeyUser(message) == null) {
//            ws.addNewUser(message);
//        }
//        ws.normalOperation(message);
//        ws.display();
//        return ws.sendMessage(message);
//    }
}

