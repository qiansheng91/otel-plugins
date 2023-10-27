package com.alibaba.sls.otel.plugins.oss.instrumentation;

import com.aliyun.oss.common.comm.RequestMessage;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import javax.annotation.Nullable;

public class OSSAttributeExtractor implements AttributesExtractor<RequestMessage, Object> {

  public static final OSSAttributeExtractor INSTANCE = new OSSAttributeExtractor();

  @Override
  public void onStart(
      AttributesBuilder attributesBuilder, Context context, RequestMessage requestMessage) {
    attributesBuilder.put("oss.bucket", requestMessage.getBucket());
    attributesBuilder.put("oss.key", requestMessage.getKey());
    attributesBuilder.put(
        "oss.operationRequest", requestMessage.getOriginalRequest().getClass().getSimpleName());
  }

  @Override
  public void onEnd(
      AttributesBuilder attributesBuilder,
      Context context,
      RequestMessage requestMessage,
      @Nullable Object o,
      @Nullable Throwable throwable) {}
}
