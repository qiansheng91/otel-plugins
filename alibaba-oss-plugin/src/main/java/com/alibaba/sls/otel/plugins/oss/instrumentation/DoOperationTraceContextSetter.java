package com.alibaba.sls.otel.plugins.oss.instrumentation;

import com.aliyun.oss.common.comm.RequestMessage;
import io.opentelemetry.context.propagation.TextMapSetter;

import javax.annotation.Nullable;

public class DoOperationTraceContextSetter implements TextMapSetter<RequestMessage> {

  public static final DoOperationTraceContextSetter INSTANCE = new DoOperationTraceContextSetter();

  @Override
  public void set(@Nullable RequestMessage requestMessage, String s, String s1) {
    requestMessage.addHeader(s, s1);
  }
}
