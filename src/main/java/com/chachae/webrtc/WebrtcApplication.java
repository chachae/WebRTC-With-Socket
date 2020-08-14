package com.chachae.webrtc;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication
public class WebrtcApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(WebrtcApplication.class)
        .bannerMode(Mode.OFF)
        .run(args);
  }

}