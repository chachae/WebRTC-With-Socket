package com.chachae.webrtc.netty.util;

import java.util.UUID;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/10 10:59
 */
public class IdUtil {

  private IdUtil() {
  }

  private static String getUUID(){
    return UUID.randomUUID().toString();
  }

  public static String uuid() {
    return getUUID();
  }

  public static String fastUUID() {
    return getUUID().replace("-", "");
  }

}
