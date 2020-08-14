package com.chachae.webrtc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 20:36
 */

@Slf4j
@Controller
public class MainController {

  @GetMapping({"/", "index"})
  public String goIndex() {
    return "index";
  }
}
