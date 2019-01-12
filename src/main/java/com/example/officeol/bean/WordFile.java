package com.example.officeol.bean;

import java.util.HashMap;
import java.util.Map;

public class WordFile {
    public Map<String,ContentBlock> blockMap=new HashMap<>();

    public Map<String, ContentBlock> getBlockMap() {
        return blockMap;
    }
    public void addNewBlock(ContentBlock contentBlock){
            blockMap.put(contentBlock.key,contentBlock);
    }
    public void editBlock(ContentBlock contentBlock){

    }
}
