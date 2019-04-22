package com.example.officeol.bean;

import java.util.ArrayList;
import java.util.Map;

public class Sheet {
    private String sheetId;

    public ArrayList<CellUser> cellUserArrayList =new ArrayList<>();
    public Sheet(String sheetId)
    {
        this.sheetId=sheetId;
    }

    public void addNewUser(String row,String col,String userId)
    {
        CellUser cellUser=new CellUser(row,col,userId);
        cellUserArrayList.add(cellUser);
    }
    public boolean addNewUserOrNot(String userId)
    {
        for(CellUser cellUser:cellUserArrayList) {
            if(cellUser.userId.equals(userId))
                return false;
        }
        return true;
    }
    public void deleteUser(String userId)
    {
        boolean test=true;
        for(CellUser cellUser:cellUserArrayList) {
            if(cellUser.userId.equals(userId)) {
                test=false;
                cellUserArrayList.remove(cellUser);
                break;
            }
        }
        if(test==true) {
            System.err.println("this user is not existed!!");
        }
    }
    public String getSheetId() {
        return sheetId;
    }

    public Boolean LockOrNot(String row,String col,String userId){
        for(CellUser cellUser:cellUserArrayList){
            if(cellUser.userId.equals(userId)){
                if(cellUser.row.equals(row)&&cellUser.col.equals(col))
                    return false;
                else{
                    for(CellUser cellUser1:cellUserArrayList){
                        if(cellUser1.row.equals(row)&&cellUser1.col.equals(col))
                            return true;
                    }
                }
            }
            else {
                if(cellUser.row.equals(row)&&cellUser.col.equals(col))
                    return true;
            }
        }
        return false;
    }
    public void updataSheet(String row,String col,String userId){
        for(CellUser cellUser:cellUserArrayList)
        {
            if(cellUser.userId.equals(userId)) {
                cellUser.updateRowAndCol(row,col);
                break;
            }
        }
    }
    public void displaySheetInfo(){
        System.out.println(sheetId);
        for(CellUser cellUser:cellUserArrayList){
            System.out.println("celluserId: "+cellUser.userId+"    row:  "+cellUser.row+"   col:   "+cellUser.col);
        }
    }
}
