package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Message;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;

public class MessageTraceContextGetter implements TextMapGetter<Message>, TextMapSetter<Message> {

  public static final MessageTraceContextGetter INSTANCE = new MessageTraceContextGetter();

  @Override
  public Iterable<String> keys(Message message) {
    List<String> keys = new ArrayList<>();

    Enumeration<Object> userProperties = message.getUserProperties().keys();
    while (userProperties.hasMoreElements()) {
      keys.add(String.valueOf(userProperties.nextElement()));
    }

    return keys;
  }

  @Nullable
  @Override
  public String get(@Nullable Message message, String s) {
    Properties userProperties = message.getUserProperties();
    if (userProperties == null) {
      return "";
    }

    return userProperties.getProperty(s, "");
  }

  @Override
  public void set(@Nullable Message message, String s, String s1) {
    Properties userProperties = message.getUserProperties();
    if (userProperties == null) {
      message.setUserProperties(new Properties());
      userProperties = message.getUserProperties();
    }

    userProperties.setProperty(s, s1);
  }
}
