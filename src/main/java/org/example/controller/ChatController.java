package org.example.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatController {


    @Autowired
    private ChatService chatService;


    @GetMapping("chat")
    public String chat(@RequestParam(required = true) String text) {
        log.info("msg: {}", text);
        if (StrUtil.isEmpty(text)) {
            return "text is empty";
        }
        String response = chatService.chat(text);

        return response;
    }


}
