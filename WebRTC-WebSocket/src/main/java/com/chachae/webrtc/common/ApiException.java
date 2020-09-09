package com.chachae.webrtc.common;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 22:28
 */
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = -6916154417432027437L;

  public ApiException(String message) {
    super(message);
  }
}