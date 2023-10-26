package com.alibaba.sls.otel.plugins.lark.v1_x.instrumentation;

import com.larksuite.oapi.core.api.request.Request;
import com.larksuite.oapi.core.api.response.Response;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;

public class LarkSingletons {
  private static final String INSTRUMENTATION_NAME = "com.aliyun.sls.larkoapi-1.0";
  private static final Instrumenter<Request, Response> INSTRUMENTER;

  static {
    InstrumenterBuilder<Request, Response> builder =
        Instrumenter.builder(
            GlobalOpenTelemetry.get(), INSTRUMENTATION_NAME, LarkAPISpanNameExtractor.create());
    INSTRUMENTER = builder.buildInstrumenter();
  }

  public static Instrumenter<Request, Response> instrumenter() {
    return INSTRUMENTER;
  }

  private LarkSingletons() {}
}
