package com.example.officeol.bean;

import java.util.ArrayList;

public class SheetList extends File {
    public ArrayList<Sheet> sheetArrayList=new ArrayList<>();
    public void addNewSheet(String fileId)
    {
        Sheet sheet=new Sheet(fileId);
        sheetArrayList.add(sheet);
    }
    public Sheet getSheet(String fileId){
        for(Sheet sheet:sheetArrayList){
            if(sheet.getSheetId().equals(fileId))
                return sheet;
        }
        System.err.println("sheet"+fileId+" is not existed!!");
        return null;
    }
    public boolean addNewSheetOrNot(String fileId){
        for(Sheet sheet:sheetArrayList){
            if(sheet.getSheetId().equals(fileId)) {
                return false;
            }
        }
        return true;
    }
}
