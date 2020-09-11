package com.chachae.webrtc.netty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/9/11 13:58
 */

@Controller
public class ViewController {

  @GetMapping("monitor")
  public String toMonitor() {
    return "monitor";
  }

}
