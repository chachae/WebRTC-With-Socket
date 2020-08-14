package com.chachae.webrtc.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:20
 */
@Data
public class Room implements Serializable {

  private static final long serialVersionUID = 5897321139282629928L;

  private Long roomId;
  private String roomName;

}
