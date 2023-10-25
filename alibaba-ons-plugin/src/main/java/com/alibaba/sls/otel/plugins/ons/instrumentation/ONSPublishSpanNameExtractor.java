package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Message;
import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

public class ONSPublishSpanNameExtractor implements SpanNameExtractor<Message> {
  public static final ONSPublishSpanNameExtractor INSTANCE = new ONSPublishSpanNameExtractor();

  @Override
  public String extract(Message message) {
    return message.getTopic() + " publish";
  }
}
