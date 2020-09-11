package com.chachae.webrtc.netty.controller;

import com.chachae.webrtc.netty.constant.SocketConfigConstant;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/11 14:47
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("message")
public class SocketMessageController {

  private final RedisTemplate<String, String> stringRedisTemplate;

  @PostMapping("1t1")
  public void oneToOne(@NotBlank(message = "{required}") String message) {
    stringRedisTemplate.convertAndSend(SocketConfigConstant.MESSAGE_TOPIC, message);
  }


}
