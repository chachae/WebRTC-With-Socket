package com.chachae.webrtc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.apache.commons.lang3.ObjectUtils;
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
    if (ObjectUtils.isEmpty(socketMap.get(userId))) {
      socketMap.put(userId, session);
      ConnectionSystemUser user = new ConnectionSystemUser(userId, roomId);
      roomService.enterRoom(roomId, user);
    }
    Long count = roomService.countUser(roomId);
    log.info("用户 {} 连接 socket 服务器成功，当前房间 ID 为：{}，当前组用户人数共有 {} 人。", userId, roomId, count);
  }

  /**
   * 消息广播
   */
  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
    Message message = JSONObject.parseObject(webSocketMessage.getPayload().toString(), Message.class);
    log.info("收到来自用户：{} 的信息：{}", message.getUserId(), message.getContent());
    sendMessage(message);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable throwable) {
    log.error("用户 {} 连接错误，异常信息如下：{}", getSessionUserId(session), throwable);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    //即使连接错误，回调了onError方法，最终也会回调onClose方法，所有退出房间写在这里比较合适
    String userId = getSessionUserId(session);
    String roomId = getSessionRoomId(session);
    roomService.removeUser(roomId, userId);
    session.close();
    socketMap.remove(userId);
    Long count = roomService.countUser(roomId);
    log.info("用户 {} 离开，当前房间 ID 为：{}，当前组用户人数共有 {} 人。", userId, roomId, count);
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }

  @Override
  public void receiveMessage(String message) {
    Message res = JSON.parseObject(message, Message.class);
    System.out.println(res);
    // 获取消息并将 JSON 转 bean
    boolean flag = sendMessage(res);
    if (flag) {
      log.info("我发送消息成功了！");
    }
  }

  /**
   * 处理消息类型
   *
   * @param message 消息对象
   */
  public boolean sendMessage(Message message) {
    switch (message.getCommand()) {
      // 1对1发送消息
      case MessageConstant.COMMAND_SEND_ONE:
        WebSocketSession session = socketMap.get(message.getReceiverId());
        return messageForwardService.sendMessageToOne(message, session);
      // 群组内群发
      case MessageConstant.COMMAND_SEND_GROUP:
        Set<ConnectionSystemUser> users = roomService.listUser(message.getReceiverRoomId());
        for (ConnectionSystemUser user : users) {
          messageForwardService.sendMessageToOne(message, socketMap.get(user.getUserId()));
        }
        return true;
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
