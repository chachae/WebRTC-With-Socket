package com.chachae.webrtc.netty.config;

import com.chachae.webrtc.netty.socket.annotation.BeforeHandshake;
import com.chachae.webrtc.netty.socket.annotation.OnBinary;
import com.chachae.webrtc.netty.socket.annotation.OnClose;
import com.chachae.webrtc.netty.socket.annotation.OnError;
import com.chachae.webrtc.netty.socket.annotation.OnEvent;
import com.chachae.webrtc.netty.socket.annotation.OnMessage;
import com.chachae.webrtc.netty.socket.annotation.OnOpen;
import com.chachae.webrtc.netty.socket.annotation.PathVariable;
import com.chachae.webrtc.netty.socket.annotation.RequestParam;
import com.chachae.webrtc.netty.socket.annotation.ServerEndpoint;
import com.chachae.webrtc.netty.socket.pojo.Session;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.IOException;
import java.util.Map;
import org.springframework.util.MultiValueMap;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/9 20:00
 */
@ServerEndpoint(path = "/ws/{arg}")
public class SysWebSocket {

  @BeforeHandshake
  public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map<String, Object> pathMap) {
    session.setSubprotocols("stomp");
    if (!req.equals("ok")) {
      System.out.println("Authentication failed!");
      session.close();
    }
  }

  @OnOpen
  public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map<String, Object> pathMap) {
    System.out.println("new connection");
    System.out.println(req);
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    System.out.println("one connection closed");
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    throwable.printStackTrace();
  }

  @OnMessage
  public void onMessage(Session session, String message) {
    System.out.println(message);
    session.sendText("Hello Netty!");
  }

  @OnBinary
  public void onBinary(Session session, byte[] bytes) {
    for (byte b : bytes) {
      System.out.println(b);
    }
    session.sendBinary(bytes);
  }

  @OnEvent
  public void onEvent(Session session, Object evt) {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
      switch (idleStateEvent.state()) {
        case READER_IDLE:
          System.out.println("read idle");
          break;
        case WRITER_IDLE:
          System.out.println("write idle");
          break;
        case ALL_IDLE:
          System.out.println("all idle");
          break;
        default:
          break;
      }
    }
  }

}
