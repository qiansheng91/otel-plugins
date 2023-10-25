package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Message;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import javax.annotation.Nullable;

public class MessageAttributeExtractor implements AttributesExtractor<Message, Object> {

  public static final MessageAttributeExtractor INSTANCE = new MessageAttributeExtractor();

  @Override
  public void onStart(AttributesBuilder attributesBuilder, Context context, Message message) {
    attributesBuilder.put("messaging.topic", message.getTopic());
    attributesBuilder.put("messaging.message_id", message.getMsgID());
    attributesBuilder.put("messaging.message_key", message.getKey());
    attributesBuilder.put("messaging.message_tag", message.getTag());
  }

  @Override
  public void onEnd(
      AttributesBuilder attributesBuilder,
      Context context,
      Message message,
      @Nullable Object sendResult,
      @Nullable Throwable throwable) {}
}
