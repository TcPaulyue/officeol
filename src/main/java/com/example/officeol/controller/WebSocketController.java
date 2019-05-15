package com.example.officeol.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Controller
public class WebSocketController {
    @RequestMapping(value = "/websocket/{userId}/{fileId}")
    public String websocket(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId, Model model) {
       try{
           System.out.println(userId+"   "+fileId);
           model.addAttribute("userId",userId);
           model.addAttribute("fileId",fileId);
           return "websocket";
       }catch (Exception e){
           e.printStackTrace();
           return "error";
       }
    }
}

