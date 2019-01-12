package com.example.officeol.bean;

import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Stack;

public class Message {
    Stack<ContentBlock> message = new Stack<>();

    public void addMessage(ContentBlock contentBlock){
        message.push(contentBlock);
    }
    public void deleteMessage(ContentBlock contentBlock){
        message.pop();
    }
}
