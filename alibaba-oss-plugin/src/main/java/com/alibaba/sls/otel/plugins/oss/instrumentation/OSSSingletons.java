package com.alibaba.sls.otel.plugins.oss.instrumentation;

import com.aliyun.oss.common.comm.RequestMessage;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;

public class OSSSingletons {

  private static final Instrumenter<RequestMessage, Void> INSTRUMENTER;

  static {
    InstrumenterBuilder<RequestMessage, Void> builder =
        Instrumenter.builder(
            GlobalOpenTelemetry.get(),
            "io.opentelemetry.aliyun-oss",
            OSSSpanNameExtractor.INSTANCE);

    INSTRUMENTER =
        builder
            .addAttributesExtractor(OSSAttributeExtractor.INSTANCE)
            .buildClientInstrumenter(DoOperationTraceContextSetter.INSTANCE);
  }

  public static Instrumenter<RequestMessage, Void> Instrumenter() {
    return INSTRUMENTER;
  }
}
