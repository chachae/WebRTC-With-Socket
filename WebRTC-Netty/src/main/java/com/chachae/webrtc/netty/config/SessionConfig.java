package com.chachae.webrtc.netty.config;

import com.chachae.webrtc.netty.socket.pojo.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/10 10:51
 */
public interface SessionConfig {

  Map<String, Session> sessionMap = new ConcurrentHashMap<>();

}
