package com.example.officeol.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentBlock {
    public String key;

    public String style="unstyle";

    public String text="";

    public List<CharacterMetaData> characterList = new ArrayList<>();

    public int depth=0;

    public Map<String,String> data = new HashMap<>();
    public String getKey() {
        return key;
    }
    public void changeText(){
        text=text+'|';
    }
}
