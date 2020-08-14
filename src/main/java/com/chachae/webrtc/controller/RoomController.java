package com.chachae.webrtc.controller;

import com.chachae.webrtc.common.R;
import com.chachae.webrtc.entity.ConnectionSystemUser;
import com.chachae.webrtc.service.IRoomService;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("room")
@RequiredArgsConstructor
public class RoomController {

  private final IRoomService roomService;

  @GetMapping("{roomId}")
  public R<Set<ConnectionSystemUser>> getRoom(@PathVariable @NotNull(message = "{required}") String roomId) {
    return R.ok(roomService.listUser(roomId));
  }

  @PostMapping("{roomId}")
  public void enterRoom(@PathVariable @NotNull(message = "{required}") String roomId, @Valid ConnectionSystemUser user) {
    roomService.enterRoom(roomId, user);
  }
}
