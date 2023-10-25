package com.alibaba.sls.otel.plugins.ons.instrumentation;

import static com.alibaba.sls.otel.plugins.ons.instrumentation.OnsSingletons.sendOneWayProducerInstrumenter;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.named;

import com.aliyun.openservices.ons.api.Message;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class ProducerSendIOnewayInstrumentation implements TypeInstrumentation {
  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return named("com.aliyun.openservices.ons.api.impl.rocketmq.ProducerImpl");
  }

  @Override
  public void transform(TypeTransformer typeTransformer) {
    typeTransformer.applyAdviceToMethod(
        named("sendOneway").and(ElementMatchers.isPublic()),
        this.getClass().getName() + "$SendOnewayAdvice");
  }

  public static class SendOnewayAdvice {
    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter(
        @Advice.Argument(value = 0) Message message,
        @Advice.Local("otelScope") Scope scope,
        @Advice.Local("otelContext") Context context) {
      Context parentContext = currentContext();
      context = sendOneWayProducerInstrumenter().start(parentContext, message);
      scope = context.makeCurrent();
    }

    @Advice.OnMethodExit(onThrowable = Exception.class)
    public static void onExit(
        @Advice.Thrown Exception exception,
        @Advice.Argument(value = 0) Message message,
        @Advice.Local("otelContext") Context context,
        @Advice.Local("otelScope") Scope scope) {
      if (scope == null) {
        return;
      }

      scope.close();
      sendOneWayProducerInstrumenter().end(context, message, null, exception);
    }
  }
}
