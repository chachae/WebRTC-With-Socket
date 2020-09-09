package com.chachae.webrtc.service;

import com.chachae.webrtc.entity.ConnectionSystemUser;
import java.util.Set;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:15
 */
public interface IRoomService {

  void enterRoom(String roomId, ConnectionSystemUser user);

  void createRoom(String roomId);

  Set<ConnectionSystemUser> listUser(String roomId);

  Long countUser(String roomId);

  void removeUser(String roomId, String userId);
}
