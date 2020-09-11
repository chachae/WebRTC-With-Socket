package com.chachae.webrtc.netty.socket.support;

import static com.chachae.webrtc.netty.socket.pojo.PojoEndpointServer.URI_TEMPLATE;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.util.AntPathMatcher;

public class AntPathMatcherWraaper extends AntPathMatcher implements WsPathMatcher {

  private final String pattern;

  public AntPathMatcherWraaper(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public String getPattern() {
    return this.pattern;
  }

  @Override
  public boolean matchAndExtract(QueryStringDecoder decoder, Channel channel) {
    Map<String, String> variables = new LinkedHashMap<>();
    boolean result = doMatch(pattern, decoder.path(), true, variables);
    if (result) {
      channel.attr(URI_TEMPLATE).set(variables);
      return true;
    }
    return false;
  }
}
