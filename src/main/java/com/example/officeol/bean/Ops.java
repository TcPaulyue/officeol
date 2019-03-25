package com.example.officeol.bean;

import javafx.util.Pair;

import java.util.List;

public class Ops {
    String userId;
    String fileId;
    String message;
    String fileType;
    public Ops(String fileId,String userId,String message,String fileType) {
        this.userId = userId;
        this.fileId = fileId;
        this.message = message;
        this.fileType=fileType;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getMessage() {
        return message;
    }
}
/*
class message{
    String userId;
    String fileId;
    String message;
}

class word{
    List<User>;
    String fileId;
    List<String> message;

}*/
