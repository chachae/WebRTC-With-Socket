package com.chachae.webrtc.cache;

import com.chachae.webrtc.common.AbstractRedisCache;
import com.chachae.webrtc.common.RedisService;
import com.chachae.webrtc.entity.ConnectionSystemUser;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 21:25
 */
@Service
@RequiredArgsConstructor
public class RoomCache extends AbstractRedisCache<Set<ConnectionSystemUser>> {

  private final RedisService redisService;
  private static final String CACHE_PREFIX = "webrtc:room:";

  @Override
  @SuppressWarnings("unchecked")
  public Set<ConnectionSystemUser> get(Object key) {
    Set<Object> set = redisService.sGet(CACHE_PREFIX + key);
    return set != null ? (Set) set : null;
  }

  public long count(String roomId) {
    return redisService.sGetSetSize(CACHE_PREFIX + roomId);
  }

  public void remove(String roomId, String userId) {
    Set<ConnectionSystemUser> set = get(roomId);
    for (ConnectionSystemUser cur : set) {
      if (cur.getUserId().equals(userId)) {
        redisService.setRemove(CACHE_PREFIX + roomId, cur);
        break;
      }
    }
  }

  public void add(String roomId, ConnectionSystemUser user) {
    redisService.sSetAndTime(CACHE_PREFIX + roomId, 7200L, user);
  }
}
