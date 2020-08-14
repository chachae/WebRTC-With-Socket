package com.chachae.webrtc.common;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 20:52
 */
public abstract class AbstractRedisCache<T> {

  public T get(Object key) {
    return null;
  }

  public void put(Object key, T data) {
  }

  public void put(Object key, T data, Long seconds) {
  }
}
