package com.chachae.webrtc.netty.service;

public interface RedisMessage {

  /**
   * 接受信息
   *
   * @param message 消息 JSON
   */
  void receiveMessage(String message);
}
