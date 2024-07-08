package org.example.domain;

import lombok.Data;

@Data
public class UserSendMessage {

    private String userId;

    private boolean useKnowledge;

    private String message;

}
