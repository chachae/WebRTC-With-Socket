package com.chachae.webrtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chachae.webrtc.entity.Message;
import com.chachae.webrtc.service.IMessageForwardService;
import java.io.IOException;
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
      session.sendMessage(new TextMessage(JSONObject.toJSONString(message)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }
}
