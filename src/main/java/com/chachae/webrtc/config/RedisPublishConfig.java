package com.chachae.webrtc.config;

import com.chachae.webrtc.service.RedisMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/14 11:19
 */
@Configuration
public class RedisPublishConfig {

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(listenerAdapter, new PatternTopic("chart"));
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(RedisMessage receiver) {
    // 这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用receiveMessage
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

}
