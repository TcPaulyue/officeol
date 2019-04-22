package com.example.officeol;

import com.example.officeol.bean.CellUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.example.officeol.config.StaticSheetList.sheetList;

@RunWith(SpringJUnit4ClassRunner.class)
public class WebSocketForSheetTest {
    @Test
    public void testLockOrNot(){
        sheetList.addNewSheet("0");
        sheetList.getSheet("0").addNewUser("1","1","s1");
        sheetList.getSheet("0").addNewUser("1","2","s2");
        System.out.println("s3"+"  row=1"+"   col=2  "+sheetList.getSheet("0").LockOrNot("1","2","s3"));
        System.out.println("s2"+"  row=1"+"   col=2  "+sheetList.getSheet("0").LockOrNot("1","2","s2"));
        System.out.println("s2"+"  row=1"+"   col=1  "+sheetList.getSheet("0").LockOrNot("1","1","s2"));
        System.out.println("s2"+"  row=2"+"   col=2  "+sheetList.getSheet("0").LockOrNot("2","2","s2"));
    }
    @Test
    public void addNewUser(){
        sheetList.addNewSheet("0");
        sheetList.getSheet("0").addNewUser("1","1","s1");
        sheetList.getSheet("0").addNewUser("1","2","s2");
        System.out.println("Test  addNewUserOrNot:==============");
        System.out.println("when s1 join in should be false and answer is :"+sheetList.getSheet("0").addNewUserOrNot("s1"));
        System.out.println("when s3 join in should be true and answer is :"+sheetList.getSheet("0").addNewUserOrNot("s3"));
        System.out.println("Test addNewUserOrNot Finished");
        System.out.println("Test addNewUser:========================");
        System.out.println("add new user s3");
        sheetList.getSheet("0").addNewUser("2","3","s3");
        sheetList.getSheet("0").updataSheet("2","3","s3");
        System.out.println("should be false:   "+sheetList.getSheet("0").LockOrNot("2","3","s3"));


    }
}
