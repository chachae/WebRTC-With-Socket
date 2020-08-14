package com.chachae.webrtc.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 22:10
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

  private static final long serialVersionUID = -8713837163942965721L;

  private String msg;
  private transient T data;

  private R(String msg, T data) {
    this.msg = msg;
    this.data = data;
  }

  public static R<Object> ok() {
    return R.ok(null);
  }

  public static <T> R<T> ok(T data) {
    return new R<>("ok", data);
  }

  public static R<String> fail() {
    return new R<>("fail", null);
  }

  public static R<String> fail(String msg) {
    return new R<>(msg, null);
  }
}
