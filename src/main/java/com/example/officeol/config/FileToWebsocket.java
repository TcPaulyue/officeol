package com.example.officeol.config;

import com.example.officeol.service.WebSocket;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class FileToWebsocket {
    public static HashMap<String, CopyOnWriteArraySet<WebSocket>> fileToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocket>>();
}
