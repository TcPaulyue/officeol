package com.example.officeol;

import com.example.officeol.bean.JupiterUser;
import com.example.officeol.bean.JupiterWord;
import com.example.officeol.bean.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = OfficeolApplication.class)
//@Transactional
public class JupiterUsertest {
    public Message message;
    public Message mymessage;
    public JupiterWord jupiterWord=new JupiterWord();
    public JupiterUser jupiterUser=new JupiterUser("1","1");
    public JupiterUser jupiterUser1=new JupiterUser("2","1");

    @Test
    public void testxForm()throws Exception
    {
        jupiterWord.jupiterUsers.add(jupiterUser);
        jupiterWord.jupiterUsers.add(jupiterUser1);
        jupiterUser.txt="11111";
        message=new Message("1","1",2,"insert","h",0);
        mymessage=new Message("1","2",3,"insert","ello",0);
        jupiterUser.xForm(message,mymessage);
        System.out.println("xForm"+message.getPosition()+"||"+mymessage.getPosition());
        jupiterUser.editTxt(message);
        jupiterUser.editTxt(mymessage);
        System.out.println("jupiterUser:   "+jupiterUser.getTxt());

        jupiterUser.txt="11111";
        message=new Message("1","1",2,"insert","h",0);
        mymessage=new Message("1","2",3,"delete","2",0);
        jupiterUser.xForm(message,mymessage);
        System.out.println("xForm"+message.getPosition()+"||"+mymessage.getPosition());
        jupiterUser.editTxt(message);
        jupiterUser.editTxt(mymessage);
        System.out.println("jupiterUser1"+jupiterUser.getTxt());

        jupiterUser.txt="abcde";
        message=new Message("1","1",2,"delete","1",0);
        mymessage=new Message("1","2",3,"delete","2",0);
        jupiterUser.xForm(message,mymessage);
        System.out.println("xForm"+message.getPosition()+"||"+mymessage.getPosition());
        jupiterUser.editTxt(message);
        jupiterUser.editTxt(mymessage);
        System.out.println("jupiterUser:   "+jupiterUser.getTxt());


        jupiterUser1.txt="11222";
        message=new Message("1","1",2,"insert","hello",0);
        mymessage=new Message("1","2",4,"delete","2",0);
        jupiterUser1.xForm(message,mymessage);
        System.out.println("xForm"+mymessage.getPosition()+"||"+message.getPosition());
        jupiterUser1.editTxt(message);
        jupiterUser1.editTxt(mymessage);
        System.out.println("jupiterUser1:   "+jupiterUser1.getTxt());
    }

}

