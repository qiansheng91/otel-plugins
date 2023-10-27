package com.alibaba.sls.otel.plugins.oss.instrumentation;

import com.aliyun.oss.common.comm.RequestMessage;
import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

public class OSSSpanNameExtractor implements SpanNameExtractor<RequestMessage> {

  public static final OSSSpanNameExtractor INSTANCE = new OSSSpanNameExtractor();

  @Override
  public String extract(RequestMessage requestMessage) {
    return "Operation " + requestMessage.getBucket();
  }
}
