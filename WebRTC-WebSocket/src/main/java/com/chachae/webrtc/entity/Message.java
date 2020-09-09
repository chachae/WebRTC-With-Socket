package com.chachae.webrtc.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 23:05
 */
@Data
public class Message implements Serializable {

  private static final long serialVersionUID = -5457063706371166926L;

  // 房间内发送消息（不包含自己）
  public static final String COMMAND_SEND_MESSAGE_EVERYONE_EXCLUDE = "send::message::1";
  public static final String TYPE_COMMAND_ROOM_LIST = "roomList";
  public static final String TYPE_COMMAND_DIALOGUE = "dialogue";
  public static final String TYPE_COMMAND_READY = "ready";
  public static final String TYPE_COMMAND_OFFER = "offer";
  public static final String TYPE_COMMAND_ANSWER = "answer";
  public static final String TYPE_COMMAND_CANDIDATE = "candidate";

  private String command;
  private String userId;
  private String receiverId;
  private String receiverRoomId;
  private String roomId;
  private String content;

  public Message() {
  }
}
