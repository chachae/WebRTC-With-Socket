package com.chachae.webrtc.netty.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/9 21:15
 */
@Data
public class Message implements Serializable {

  /**
   * 发送方id
   */
  private String fromId;

  /**
   * 接收方id
   */
  private String toId;

  /**
   * 发送文本
   */
  private String content;

  /**
   * 消息类型
   */
  private String command;

}
