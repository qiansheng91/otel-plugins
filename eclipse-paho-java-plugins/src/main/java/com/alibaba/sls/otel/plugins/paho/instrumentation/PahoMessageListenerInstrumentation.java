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
import static com.alibaba.sls.otel.plugins.paho.instrumentation.PahoSpanNameExtractor.consumerSpanName;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class PahoMessageListenerInstrumentation implements TypeInstrumentation {
    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return hasSuperType(named("org.eclipse.paho.client.mqttv3.IMqttMessageListener"));
    }

    @Override
    public void transform(TypeTransformer transformer) {
        transformer.applyAdviceToMethod(named("messageArrived").and(ElementMatchers.isPublic()),
                this.getClass().getName() + "$MessageArrivedMethodAdvice");
    }

    @SuppressWarnings("unused")
    public static class MessageArrivedMethodAdvice {

        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This MqttClient client, @Advice.Argument(value = 0) String topic,
                @Advice.Local("otelContext") Context context, @Advice.Local("otelScope") Scope scope) {
            Context parentContext = currentContext();
            context = instrumenter().start(parentContext, consumerSpanName(topic));
            scope = context.makeCurrent();
        }

        @Advice.OnMethodExit(onThrowable = Exception.class)
        public static void onExit(@Advice.Thrown Exception exception, @Advice.Argument(value = 0) String topic,
                @Advice.Local("otelContext") Context context, @Advice.Local("otelScope") Scope scope) {
            if (scope == null) {
                return;
            }

            scope.close();
            instrumenter().end(context, consumerSpanName(topic), null, exception);
        }
    }

    public PahoMessageListenerInstrumentation() {
    }
}
