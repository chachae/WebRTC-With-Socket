package com.chachae.webrtc.interceptor;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/14 11:31
 */
@Slf4j
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

//  public static void main(String[] args) {
//    String url = "wss://127.0.0.1:8443/websocket/1001/2002";
//    System.out.println(url.split("/")[4]);
//  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse seHttpResponse,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    String roomId = serverHttpRequest.getURI().toString().split("/")[4];
    String userId = serverHttpRequest.getURI().toString().split("/")[5];
    attributes.put("roomId", roomId);
    attributes.put("userId", userId);
    log.info("握手之前");
    //从request里面获取对象，存放attributes
    return super.beforeHandshake(serverHttpRequest, seHttpResponse, wsHandler, attributes);
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
      Exception ex) {
    log.info("握手之后");
    super.afterHandshake(request, response, wsHandler, ex);
  }
}
