package com.example.officeol.config;

import com.example.officeol.service.WebSocketForSheet;
import com.example.officeol.service.WebSocketForWord;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class SheetToWebSocket {
    public static HashMap<String, CopyOnWriteArraySet<WebSocketForSheet>> sheetToWebsocket=new HashMap<String, CopyOnWriteArraySet<WebSocketForSheet>>();
}
