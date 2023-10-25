package com.alibaba.sls.otel.plugins.ons.instrumentation;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static com.alibaba.sls.otel.plugins.ons.instrumentation.OnsSingletons.syncProducerInstrumenter;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class ProducerSyncSendInstrumentation implements TypeInstrumentation {
    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return named("com.aliyun.openservices.ons.api.impl.rocketmq.ProducerImpl");
    }

    @Override
    public void transform(TypeTransformer typeTransformer) {
        typeTransformer.applyAdviceToMethod(named("send").and(ElementMatchers.isPublic()).and(takesArguments(1)).and(takesArgument(0, Message.class)), this.getClass().getName() + "$SyncSendAdvice");
    }

    public static class SyncSendAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.Argument(value = 0) Message message, @Advice.Local("otelScope") Scope scope, @Advice.Local("otelContext") Context context) {
            Context parentContext = currentContext();
            context = syncProducerInstrumenter().start(parentContext, message);
            scope = context.makeCurrent();
        }

        @Advice.OnMethodExit(onThrowable = Exception.class)
        public static void onExit(@Advice.Thrown Exception exception, @Advice.Argument(value = 0) Message message, @Advice.Return SendResult sendResult, @Advice.Local("otelContext") Context context, @Advice.Local("otelScope") Scope scope) {
            if (scope == null) {
                return;
            }

            scope.close();
            syncProducerInstrumenter().end(context, message, sendResult, exception);
        }
    }
}
