package com.alibaba.sls.otel.plugins.paho.instrumentation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;

public class PahoSingletons {

    private static final Instrumenter<String, Void> INSTRUMENTER;

    static {
        InstrumenterBuilder<String, Void> builder = Instrumenter.builder(GlobalOpenTelemetry.get(),
                "io.opentelemetry.eclipse-paho", PahoSpanNameExtractor.Instance);

        INSTRUMENTER = builder.buildInstrumenter();
    }

    public static Instrumenter<String, Void> instrumenter() {
        return INSTRUMENTER;
    }

    private PahoSingletons() {
    }

}
