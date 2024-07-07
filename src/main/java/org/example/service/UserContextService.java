package org.example.service;

import org.example.domain.UserContext;
import org.example.domain.UserContextMessage;

public interface UserContextService {

    UserContext getUserContext(String userId);

    void addUserContextMessage(String userId, UserContextMessage userContextMessage);

}
