package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import java.util.Arrays;
import java.util.List;

@AutoService(InstrumentationModule.class)
public class AlibabaONSInstrumentationModule extends InstrumentationModule {
  public AlibabaONSInstrumentationModule() {
    super("aliyun-ons", "com.alibaba.sls.otel.plugins.ons");
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return Arrays.asList(
        new ProducerAsyncSendInstrumentation(),
        new ProducerSendIOnewayInstrumentation(),
        new ProducerSyncSendInstrumentation(),
        new MessageListenerInstrumentation());
  }

  @Override
  public int order() {
    return 1;
  }

  @Override
  public List<String> getAdditionalHelperClassNames() {
    return Arrays.asList(
        "com.alibaba.sls.otel.plugins.ons.instrumentation.MessageTraceContextGetter",
        "com.alibaba.sls.otel.plugins.ons.instrumentation.ONSConsumerSpanNameExtractor",
        "com.alibaba.sls.otel.plugins.ons.instrumentation.ONSPublishSpanNameExtractor",
        "com.alibaba.sls.otel.plugins.ons.instrumentation.OnsSingletons",
        "com.alibaba.sls.otel.plugins.ons.instrumentation.MessageAttributeExtractor");
  }
}
