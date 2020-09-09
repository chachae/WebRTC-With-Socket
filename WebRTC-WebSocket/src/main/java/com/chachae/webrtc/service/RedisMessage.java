package com.chachae.webrtc.service;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/14 11:24
 */
public interface RedisMessage {

  /**
   * 接受信息
   *
   * @param message 消息 JSON
   */
  void receiveMessage(String message);
}
