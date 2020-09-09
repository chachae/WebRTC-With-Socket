package com.chachae.webrtc.service.impl;

import com.alibaba.fastjson.JSON;
import com.chachae.webrtc.entity.ConnectionSystemUser;
import com.chachae.webrtc.entity.Message;
import com.chachae.webrtc.service.IMessageForwardService;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 23:16
 */
@Service
public class MessageForwardServiceImpl implements IMessageForwardService {

  @Override
  public boolean sendMessageToOne(Message message, WebSocketSession session) {
    if (session == null || !session.isOpen()) {
      return false;
    }
    try {
      session.sendMessage(new TextMessage(JSON.toJSONString(message)));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean sendMessageGroup(Message message, Set<ConnectionSystemUser> users, Map<String, WebSocketSession> socketMap) {
    for (ConnectionSystemUser user : users) {
      // 默认不发送给本人
      if (!message.getUserId().equals(user.getUserId())) {
        this.sendMessageToOne(message, socketMap.get(user.getUserId()));
      }
    }
    return true;
  }
}
