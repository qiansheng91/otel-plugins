package com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation;

import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

public class Lark2xAPISpanNameExtractor implements SpanNameExtractor<String> {

  public static SpanNameExtractor<? super String> create() {
    return new Lark2xAPISpanNameExtractor();
  }

  @Override
  public String extract(String request) {
    return request;
  }
}
