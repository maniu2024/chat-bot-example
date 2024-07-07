package org.example.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserContextMessage {

    private String message;

    private Date sendTime;

}
