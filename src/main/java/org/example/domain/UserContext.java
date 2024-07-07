package org.example.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserContext {

    private String userId;

    List<UserContextMessage> userMessage;

}
