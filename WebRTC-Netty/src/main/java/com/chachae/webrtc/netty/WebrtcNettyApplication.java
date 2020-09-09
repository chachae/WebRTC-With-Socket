package com.chachae.webrtc.netty;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/9 14:55
 */
@ServletComponentScan
@SpringBootApplication
public class WebrtcNettyApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(WebrtcNettyApplication.class)
        .bannerMode(Mode.OFF)
        .run(args);
  }

}
