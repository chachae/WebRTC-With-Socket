package com.chachae.webrtc.service;

import com.chachae.webrtc.entity.ConnectionSystemUser;
import com.chachae.webrtc.entity.Message;
import java.util.Map;
import java.util.Set;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 23:16
 */
public interface IMessageForwardService {

  /**
   * 发送给某个人
   *
   * @param message 消息 JSON
   */
  boolean sendMessageToOne(Message message, WebSocketSession session);

  /**
   * 发送给某个群组
   *
   * @param message 消息 JSON
   */
  boolean sendMessageGroup(Message message, Set<ConnectionSystemUser> sessions, Map<String, WebSocketSession> socketMap);
}
