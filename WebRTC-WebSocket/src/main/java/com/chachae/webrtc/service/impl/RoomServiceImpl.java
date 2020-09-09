package com.chachae.webrtc.service.impl;

import com.chachae.webrtc.cache.RoomCache;
import com.chachae.webrtc.entity.ConnectionSystemUser;
import com.chachae.webrtc.service.IRoomService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:15
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

  private final RoomCache roomCache;

  @Override
  public synchronized void enterRoom(String roomId, ConnectionSystemUser user) {
    user.setRoomId(roomId);
    roomCache.add(roomId, user);
  }

  @Override
  public void createRoom(String roomId) {
    roomCache.put(roomId, null);
  }

  @Override
  public Set<ConnectionSystemUser> listUser(String roomId) {
    return roomCache.get(roomId);
  }

  @Override
  public Long countUser(String roomId) {
    return roomCache.count(roomId);
  }

  @Override
  public void removeUser(String roomId, String userId) {
    roomCache.remove(roomId, userId);
  }
}
