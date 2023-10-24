package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

import java.util.List;

@AutoService(InstrumentationModule.class)
public class AlibabaONSInstrumentationModule extends InstrumentationModule {
    public AlibabaONSInstrumentationModule(String mainInstrumentationName, String... additionalInstrumentationNames) {
        super(mainInstrumentationName, additionalInstrumentationNames);
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return null;
    }
}
