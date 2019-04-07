package com.example.officeol.bean;

public class Message {
    String fileId;
    String userId;
    int position;
    String operation;
    String message;
    public int state;
    public Message(String fileId,String userId,int position,String operation,String message,int state){
        this.fileId =fileId;
        this.userId =userId;
        this.position =position;
        this.operation =operation;
        this.message =message;
        this.state=state;
    }
    public Message(Message otherMessage){
        this.fileId = otherMessage.fileId;
        this.userId = otherMessage.userId;
        this.position = otherMessage.position;
        this.operation = otherMessage.operation;
        this.message = otherMessage.message;
        this.state = otherMessage.state;
    }
    @Override
    public String toString(){
        String messageStr = "position:"+position+", message:"+message+", state:"+state;
        return messageStr;
    }

    public int getState()
    {
        return this.state;
    }
    public int changeState()
    {
        return state++;
    }

    public int getPosition() {
        return position;
    }
    public void addPosition(int count){
        position=position+count;
    }

    public void deletePosition(int count){
        if(position>count)
            position=position-count;
        else
            System.err.println("error");
    }

    public String getMessage() {
        return message;
    }

    public String getFileId() {
        return fileId;
    }

    public String getOperation() {
        return operation;
    }

    public String getUserId() {
        return userId;
    }
}
