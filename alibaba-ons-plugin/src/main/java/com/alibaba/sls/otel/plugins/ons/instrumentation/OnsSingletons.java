package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;

public class OnsSingletons {

    private static final Instrumenter<Message, SendResult> SYNC_PRODUCER_INSTRUMENTER;
    private static final Instrumenter<Message, SendResult> SEND_ONEWAY_PRODUCER_INSTRUMENTER;
    private static final Instrumenter<Message, SendCallback> ASYNC_PRODUCER_INSTRUMENTER;
    private static final Instrumenter<Message, Action> CONSUMER_INSTRUMENTER;

    static {
        SYNC_PRODUCER_INSTRUMENTER = null;
        CONSUMER_INSTRUMENTER = null;
        ASYNC_PRODUCER_INSTRUMENTER = null;
        SEND_ONEWAY_PRODUCER_INSTRUMENTER = null;
    }

    public static Instrumenter<Message, SendCallback> asyncProducerInstrumenter() {
        return ASYNC_PRODUCER_INSTRUMENTER;
    }

    public static Instrumenter<Message, SendResult> syncProducerInstrumenter() {
        return SYNC_PRODUCER_INSTRUMENTER;
    }

    public static Instrumenter<Message, SendResult> sendOneWayProducerInstrumenter() {
        return SEND_ONEWAY_PRODUCER_INSTRUMENTER;
    }

    public static Instrumenter<Message, Action> producerInstrumenter() {
        return CONSUMER_INSTRUMENTER;
    }

}
