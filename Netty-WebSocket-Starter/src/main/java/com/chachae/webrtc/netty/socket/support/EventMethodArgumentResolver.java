package com.chachae.webrtc.netty.socket.support;

import com.chachae.webrtc.netty.socket.annotation.OnEvent;
import io.netty.channel.Channel;
import java.util.Objects;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;

public class EventMethodArgumentResolver implements MethodArgumentResolver {

  private final AbstractBeanFactory beanFactory;

  public EventMethodArgumentResolver(AbstractBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }


  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Objects.requireNonNull(parameter.getMethod()).isAnnotationPresent(OnEvent.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
    if (object == null) {
      return null;
    }
    TypeConverter typeConverter = beanFactory.getTypeConverter();
    return typeConverter.convertIfNecessary(object, parameter.getParameterType());
  }
}
