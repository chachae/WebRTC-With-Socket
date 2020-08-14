package com.chachae.webrtc.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/14 10:08
 */
@Component
public class SpringUtil implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  public SpringUtil() {
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    SpringUtil.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }

}
