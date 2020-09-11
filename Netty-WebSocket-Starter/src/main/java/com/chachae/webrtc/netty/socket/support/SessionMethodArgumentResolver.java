package com.chachae.webrtc.netty.socket.support;

import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;
import com.chachae.webrtc.netty.socket.pojo.Session;

import static com.chachae.webrtc.netty.socket.pojo.PojoEndpointServer.SESSION_KEY;

public class SessionMethodArgumentResolver implements MethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Session.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
      return channel.attr(SESSION_KEY).get();
  }
}
