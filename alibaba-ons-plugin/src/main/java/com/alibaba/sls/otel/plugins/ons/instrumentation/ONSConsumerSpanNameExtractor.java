package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Message;
import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

public class ONSConsumerSpanNameExtractor implements SpanNameExtractor<Message> {

    public static final ONSConsumerSpanNameExtractor INSTANCE = new ONSConsumerSpanNameExtractor();

    @Override
    public String extract(Message message) {
        return message.getTopic() + " process";
    }
}
