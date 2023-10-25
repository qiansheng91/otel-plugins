package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;

public class OnsSingletons {

    private static final Instrumenter<Message, SendResult> SYNC_PRODUCER_INSTRUMENTER;
    private static final Instrumenter<Message, Void> SEND_ONEWAY_PRODUCER_INSTRUMENTER;
    private static final Instrumenter<Message, Action> CONSUMER_INSTRUMENTER;

    static {
        InstrumenterBuilder<Message, SendResult> builder = Instrumenter.builder(GlobalOpenTelemetry.get(),
                "io.opentelemetry.eclipse-paho", PahoSpanNameExtractor.Instance);

        InstrumenterBuilder<Message, Void> builder1 = Instrumenter.builder(GlobalOpenTelemetry.get(),
                "io.opentelemetry.eclipse-paho", PahoSpanNameExtractor.Instance);

        InstrumenterBuilder<Message, Action> builder2 = Instrumenter.builder(GlobalOpenTelemetry.get(),
                "io.opentelemetry.eclipse-paho", PahoSpanNameExtractor.Instance);


        SYNC_PRODUCER_INSTRUMENTER = builder.buildProducerInstrumenter(MessageTraceContextGetter.INSTANCE);
        SEND_ONEWAY_PRODUCER_INSTRUMENTER = builder1.buildProducerInstrumenter(MessageTraceContextGetter.INSTANCE);
        CONSUMER_INSTRUMENTER = builder2.buildConsumerInstrumenter(MessageTraceContextGetter.INSTANCE);
    }

    public static Instrumenter<Message, Void> asyncProducerInstrumenter() {
        return SEND_ONEWAY_PRODUCER_INSTRUMENTER;
    }

    public static Instrumenter<Message, SendResult> syncProducerInstrumenter() {
        return SYNC_PRODUCER_INSTRUMENTER;
    }

    public static Instrumenter<Message, Void> sendOneWayProducerInstrumenter() {
        return SEND_ONEWAY_PRODUCER_INSTRUMENTER;
    }

    public static Instrumenter<Message, Action> consumerInstrumenter() {
        return CONSUMER_INSTRUMENTER;
    }

}
