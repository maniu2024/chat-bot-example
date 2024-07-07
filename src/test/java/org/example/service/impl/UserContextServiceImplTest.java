package org.example.service.impl;

import org.example.domain.UserContext;
import org.example.domain.UserContextMessage;
import org.example.service.UserContextService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserContextServiceImplTest {

    @Autowired
    private UserContextService userContextService;

    @Test
    public void getUserContext() {

        UserContext userContext = userContextService.getUserContext("1");
        System.out.println("userContext = " + userContext);
    }

    @Test
    public void addUserContext() {
        UserContextMessage userContextMessage = new UserContextMessage();
        userContextMessage.setMessage("Hi-2");
        userContextMessage.setSendTime(new Date());
        userContextService.addUserContextMessage("1",userContextMessage);
    }
}