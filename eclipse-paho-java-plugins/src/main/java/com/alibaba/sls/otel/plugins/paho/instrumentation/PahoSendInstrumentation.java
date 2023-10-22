package com.alibaba.sls.otel.plugins.paho.instrumentation;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.eclipse.paho.client.mqttv3.MqttClient;

import static com.alibaba.sls.otel.plugins.paho.instrumentation.PahoSingletons.instrumenter;
import static com.alibaba.sls.otel.plugins.paho.instrumentation.PahoSpanNameExtractor.publishSpanName;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class PahoSendInstrumentation implements TypeInstrumentation {
    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return named("org.eclipse.paho.client.mqttv3.MqttClient");
    }

    @Override
    public void transform(TypeTransformer typeTransformer) {
        typeTransformer.applyAdviceToMethod(named("publish").and(ElementMatchers.isPublic()),
                this.getClass().getName() + "$PublishMethodAdvice");
    }

    @SuppressWarnings("unused")
    public static class PublishMethodAdvice {

        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This MqttClient client, @Advice.Argument(value = 0) String topic,
                @Advice.Local("otelScope") Scope scope, @Advice.Local("otelContext") Context context) {
            Context parentContext = currentContext();
            context = instrumenter().start(parentContext, publishSpanName(topic));
            scope = context.makeCurrent();
        }

        @Advice.OnMethodExit(onThrowable = Exception.class)
        public static void onExit(@Advice.Thrown Exception exception, @Advice.Argument(value = 0) String topic,
                @Advice.Local("otelContext") Context context, @Advice.Local("otelScope") Scope scope) {
            if (scope == null) {
                return;
            }

            scope.close();
            if (exception != null) {
                instrumenter().end(context, publishSpanName(topic), null, null);
            } else {
                instrumenter().end(context, publishSpanName(topic), null, exception);
            }
        }
    }
}
