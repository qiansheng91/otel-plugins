package com.alibaba.sls.otel.plugins.paho.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Arrays;
import java.util.List;

@AutoService(InstrumentationModule.class)
public class PahoInstrumentationModule extends InstrumentationModule {

    public PahoInstrumentationModule() {
        super("eclipse-paho", "com.eclipse.paho-1.x");
    }

    public int order() {
        return 1;
    }

    @Override
    public List<String> getAdditionalHelperClassNames() {
        return Arrays.asList("com.alibaba.sls.otel.plugins.paho.instrumentation.PahoSingletons",
                "com.alibaba.sls.otel.plugins.paho.instrumentation.PahoSpanNameExtractor");
    }

    @Override
    public ElementMatcher.Junction<ClassLoader> classLoaderMatcher() {
        return AgentElementMatchers.hasClassesNamed("org.eclipse.paho.client.mqttv3.MqttClient");
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return Arrays.asList(new PahoSendInstrumentation(), new PahoMessageListenerInstrumentation());
    }
}
