package com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;

public class LarkSingletons {
  private static final String INSTRUMENTATION_NAME = "com.aliyun.sls.larkoapi-2.0";
  private static final Instrumenter<String, Object> INSTRUMENTER;

  static {
    InstrumenterBuilder<String, Object> builder =
        Instrumenter.builder(
            GlobalOpenTelemetry.get(), INSTRUMENTATION_NAME, Lark2xAPISpanNameExtractor.create());
    INSTRUMENTER = builder.buildInstrumenter();
  }

  public static Instrumenter<String, Object> instrumenter() {
    return INSTRUMENTER;
  }

  private LarkSingletons() {}
}
