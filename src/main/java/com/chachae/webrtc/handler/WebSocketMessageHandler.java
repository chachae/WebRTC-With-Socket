package com.chachae.webrtc.handler;

import com.alibaba.fastjson.JSON;
import com.chachae.webrtc.constant.MessageConstant;
import com.chachae.webrtc.entity.ConnectionSystemUser;
import com.chachae.webrtc.entity.Message;
import com.chachae.webrtc.service.IMessageForwardService;
import com.chachae.webrtc.service.IRoomService;
import com.chachae.webrtc.service.RedisMessage;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 22:42
 */
@Slf4j
@Component
public class WebSocketMessageHandler implements WebSocketHandler, RedisMessage {

  private static IRoomService roomService;
  private static IMessageForwardService messageForwardService;

  @Autowired
  private void setRoomService(IRoomService roomService) {
    WebSocketMessageHandler.roomService = roomService;
  }

  @Autowired
  private void setMessageForwardService(IMessageForwardService messageForwardService) {
    WebSocketMessageHandler.messageForwardService = messageForwardService;
  }

  private static final Map<String, WebSocketSession> socketMap = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    //获取用户信息
    String userId = getSessionUserId(session);
    String roomId = getSessionRoomId(session);
    socketMap.put(userId, session);
    roomService.enterRoom(roomId, new ConnectionSystemUser(userId, roomId));
  }

  /**
   * 消息广播
   */
  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
    Message message = JSON.parseObject(webSocketMessage.getPayload().toString(), Message.class);
    sendMessage(message);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable throwable) {
    log.error("用户 {} 连接错误，异常信息如下：{}", getSessionUserId(session), throwable);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    // 即使连接错误，回调了onError方法，最终也会回调onClose方法
    String userId = getSessionUserId(session);
    roomService.countUser(getSessionRoomId(session));
    socketMap.remove(userId);
    session.close();
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }

  /**
   * 接收广播消息
   *
   * @param message 消息 JSON
   */
  @Override
  public void receiveMessage(String message) {
    sendMessage(JSON.parseObject(message, Message.class));
  }

  /**
   * 处理消息类型
   *
   * @param message 消息对象
   */
  public boolean sendMessage(Message message) {
    // 获取广播消息指令
    switch (message.getCommand()) {
      // 1对1发送消息
      case MessageConstant.TYPE_COMMAND_OFFER:
      case MessageConstant.TYPE_COMMAND_ANSWER:
      case MessageConstant.TYPE_COMMAND_CANDIDATE:
      case MessageConstant.COMMAND_SEND_ONE:
      case MessageConstant.COMMAND_SEND_CMD:
        return messageForwardService.sendMessageToOne(message, socketMap.get(message.getReceiverId()));
      // 群组内群发
      case MessageConstant.COMMAND_SEND_GROUP:
        Set<ConnectionSystemUser> users = roomService.listUser(message.getReceiverRoomId());
        return messageForwardService.sendMessageGroup(message, users, socketMap);
      // 发送心跳
      case MessageConstant.TYPE_COMMAND_HEART:
        return messageForwardService.sendMessageToOne(message, socketMap.get(message.getUserId()));
      default:
        return true;
    }
  }

  private String getSessionUserId(WebSocketSession session) {
    return (String) session.getAttributes().get("userId");
  }

  private String getSessionRoomId(WebSocketSession session) {
    return (String) session.getAttributes().get("roomId");
  }
}
