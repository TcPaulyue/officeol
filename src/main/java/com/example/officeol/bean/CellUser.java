package com.example.officeol.bean;

public class CellUser {
    String row;
    String col;
    public String userId;

    public CellUser(String row, String col, String userId)
    {
        this.row=row;
        this.col=col;
        this.userId=userId;
    }
    public String getCol() {
        return col;
    }

    public String getRow() {
        return row;
    }
    public void updateRowAndCol(String row,String col){
        this.row=row;
        this.col=col;
    }
}
