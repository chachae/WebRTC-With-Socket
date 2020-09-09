package com.chachae.webrtc.controller;

import com.alibaba.fastjson.JSON;
import com.chachae.webrtc.constant.MessageConstant;
import com.chachae.webrtc.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/14 15:33
 */
@Validated
@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class MessageController {

  private final RedisTemplate<String, String> stringStringRedisTemplate;

  @PostMapping("/1t1")
  public void topicMsg(String message) {
    //广播消息到各个订阅者
    stringStringRedisTemplate.convertAndSend("chart", message);
  }

  @PostMapping("/group")
  public void groupMsg(String message) {
    //广播消息到各个订阅者
    stringStringRedisTemplate.convertAndSend("chart", message);
  }
}
