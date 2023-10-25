package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

import java.util.Arrays;
import java.util.List;

@AutoService(InstrumentationModule.class)
public class AlibabaONSInstrumentationModule extends InstrumentationModule {
    public AlibabaONSInstrumentationModule() {
        super("", "");
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return Arrays.asList(new ProducerAsyncSendInstrumentation(), new ProducerSendIOnewayInstrumentation(),
                new ProducerSyncSendInstrumentation(), new MessageListenerInstrumentation());
    }
}
