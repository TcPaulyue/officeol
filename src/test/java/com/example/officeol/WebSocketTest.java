package com.example.officeol;

import com.alibaba.fastjson.JSONObject;
import com.example.officeol.bean.Ops;
import com.example.officeol.service.WebSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OfficeolApplication.class)
@Transactional
public class WebSocketTest {

    public Session session;
    @Test
    public void testWebsocket()
    {

    }

}
