package org.example.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.UserSendMessage;
import org.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ChatController {


    @Autowired
    private ChatService chatService;


    @PostMapping("chat")
    public String chat(@RequestBody UserSendMessage userSendMessage) {
        log.info("msg: {}", JSON.toJSONString(userSendMessage));
        if (userSendMessage == null || StrUtil.isEmpty(userSendMessage.getMessage())) {
            return "参数非法";
        }
        String response = chatService.chat(userSendMessage);

        return response;
    }


}
