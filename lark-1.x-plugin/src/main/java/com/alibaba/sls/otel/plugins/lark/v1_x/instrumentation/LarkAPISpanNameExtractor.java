package com.alibaba.sls.otel.plugins.lark.v1_x.instrumentation;

import com.larksuite.oapi.core.api.request.Request;
import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

public class LarkAPISpanNameExtractor implements SpanNameExtractor<Request> {

    public static SpanNameExtractor<? super Request> create() {
        return new LarkAPISpanNameExtractor();
    }


    @Override
    public String extract(Request request) {
        return String.format("%s %s", request.getHttpMethod(), request.getHttpPath());
    }
}
