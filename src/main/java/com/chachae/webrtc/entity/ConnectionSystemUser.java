package com.chachae.webrtc.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * websocket 连接用户
 *
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionSystemUser implements Serializable {

  private static final long serialVersionUID = 1890550997179325107L;

  @NotNull(message = "{required}")
  private String userId;
  private String roomId;

}
