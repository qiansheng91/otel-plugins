package com.alibaba.sls.otel.plugins.paho.instrumentation;

import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

public class PahoSpanNameExtractor implements SpanNameExtractor<String> {

    public static final SpanNameExtractor<String> Instance = new PahoSpanNameExtractor();

    @Override
    public String extract(String topic) {
        return topic;
    }

    private PahoSpanNameExtractor() {
        //
    }

    public static String publishSpanName(String topicName) {
        return String.format("Publish %s", topicName);
    }

    public static String consumerSpanName(String topicName) {
        return String.format("Consumer %s", topicName);
    }
}
