package com.example.officeol.config;

import com.example.officeol.service.WebSocketForWord;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WordToWebsocket {
    public static HashMap<String, CopyOnWriteArraySet<WebSocketForWord>> wordToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocketForWord>>();
}
