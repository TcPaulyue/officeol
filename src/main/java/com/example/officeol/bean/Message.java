package com.example.officeol.bean;

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
