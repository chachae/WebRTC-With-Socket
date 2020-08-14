package com.chachae.webrtc.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:16
 */
@Data
public class SystemUser implements Serializable {

  private static final long serialVersionUID = 4703015886585800107L;

  private Long userId;
  private String username;
  private String password;

}
