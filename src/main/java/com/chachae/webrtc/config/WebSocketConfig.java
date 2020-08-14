package com.chachae.webrtc.config;

import com.chachae.webrtc.interceptor.WebSocketInterceptor;
import com.chachae.webrtc.handler.WebSocketMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 19:04
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private static final String MAPPING = "/websocket/{roomId}/{userId}";

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

    registry
        .addHandler(new WebSocketMessageHandler(), MAPPING)
        .setAllowedOrigins("*")
        .addInterceptors(new WebSocketInterceptor());
  }
}

