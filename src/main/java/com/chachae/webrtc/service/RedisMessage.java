package com.chachae.webrtc.service;

import com.chachae.webrtc.entity.Message;
import org.springframework.stereotype.Component;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/14 11:24
 */
public interface RedisMessage {

  /**
   * 接受信息
   *
   * @param message
   */
  void receiveMessage(String message);
}
