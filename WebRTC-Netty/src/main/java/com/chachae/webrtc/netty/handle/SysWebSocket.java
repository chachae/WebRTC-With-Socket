package com.chachae.webrtc.netty.handle;

import com.alibaba.fastjson.JSON;
import com.chachae.webrtc.netty.config.SessionConfig;
import com.chachae.webrtc.netty.constant.MsgTypeConstant;
import com.chachae.webrtc.netty.entity.Message;
import com.chachae.webrtc.netty.service.RedisMessage;
import com.chachae.webrtc.netty.socket.annotation.BeforeHandshake;
import com.chachae.webrtc.netty.socket.annotation.OnBinary;
import com.chachae.webrtc.netty.socket.annotation.OnClose;
import com.chachae.webrtc.netty.socket.annotation.OnError;
import com.chachae.webrtc.netty.socket.annotation.OnEvent;
import com.chachae.webrtc.netty.socket.annotation.OnMessage;
import com.chachae.webrtc.netty.socket.annotation.OnOpen;
import com.chachae.webrtc.netty.socket.annotation.PathVariable;
import com.chachae.webrtc.netty.socket.annotation.ServerEndpoint;
import com.chachae.webrtc.netty.socket.pojo.Session;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Map.Entry;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/9 20:00
 */
@ServerEndpoint(path = "/ws/{id}", port = "${ws.port}")
public class SysWebSocket implements RedisMessage {

  @BeforeHandshake
  public void handshake(Session session, HttpHeaders headers, @PathVariable String id) {
    session.setSubprotocols("stomp");
    session.setAttribute("id", id);
    if (id == null) {
      session.close();
    }
  }

  @OnOpen
  public void onOpen(Session session, HttpHeaders headers, @PathVariable String id) {
    // 实际业务，key=用户id，value=session
    SessionConfig.sessionMap.putIfAbsent(id, session);
    for (Entry<String, Session> entry : SessionConfig.sessionMap.entrySet()) {
      System.out.println(entry.getKey() + ":" + entry.getValue());
    }
  }

  @OnClose
  public void onClose(Session session) {
    Object id = session.getAttribute("id");
    if (id instanceof String) {
      SessionConfig.sessionMap.remove(id);
    }
    System.out.println("--------------------");
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    throwable.printStackTrace();
  }

  @OnMessage
  public void onMessage(Session session, String message) {
    System.out.println(message);
    Message msg = JSON.parseObject(message, Message.class);
    if (message != null && SessionConfig.sessionMap.get(msg.getToId()) != null) {
      // SessionConfig.sessionMap.get(msg.getToId()).sendText(msg.getText());
      receiveMessage(message);
    }
  }

  @OnBinary
  public void onBinary(Session session, byte[] bytes) {
    for (byte b : bytes) {
      System.out.println(b);
    }
    session.sendBinary(bytes);
  }

  @OnEvent
  public void onEvent(Session session, Object evt) {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
      switch (idleStateEvent.state()) {
        case READER_IDLE:
          System.out.println("read idle");
          break;
        case WRITER_IDLE:
          System.out.println("write idle");
          break;
        case ALL_IDLE:
          System.out.println("all idle");
          break;
        default:
          break;
      }
    }
  }

  /**
   * 接收广播消息
   *
   * @param message 消息 JSON
   */
  @Override
  public void receiveMessage(String message) {
    Message msg = JSON.parseObject(message, Message.class);
    // 此处判断 channel 是否存在该实例
    if (msg != null && SessionConfig.sessionMap.get(msg.getToId()) != null) {
      handlerMessage(msg, SessionConfig.sessionMap.get(msg.getToId()));
    }
  }

  private void handlerMessage(Message message, Session session) {
    switch (message.getCommand()) {
      case MsgTypeConstant.CMD:
      case MsgTypeConstant.ANSWER:
      case MsgTypeConstant.OFFER:
      case MsgTypeConstant.CANDIDATE:
        handleCmd(message, session);
        break;
      default:
        break;
    }
  }

  private void handleCmd(Message message, Session session) {
    String fromId = message.getFromId();
    message.setFromId(message.getToId());
    message.setToId(fromId);
    session.sendText(JSON.toJSONString(message));
  }

}
