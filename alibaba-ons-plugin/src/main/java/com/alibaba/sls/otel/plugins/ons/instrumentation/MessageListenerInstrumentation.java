package com.alibaba.sls.otel.plugins.ons.instrumentation;

import static com.alibaba.sls.otel.plugins.ons.instrumentation.OnsSingletons.consumerInstrumenter;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
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

public class MessageListenerInstrumentation implements TypeInstrumentation {
  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return hasSuperType(named("com.aliyun.openservices.ons.api.MessageListener"));
  }

  @Override
  public void transform(TypeTransformer transformer) {
    transformer.applyAdviceToMethod(
        named("consume").and(ElementMatchers.isPublic()),
        this.getClass().getName() + "$ConsumeAdvice");
  }

  public static class ConsumeAdvice {
    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter(
        @Advice.Argument(value = 0) Message message,
        @Advice.Local("otelScope") Scope scope,
        @Advice.Local("otelContext") Context context) {
      Context parentContext = currentContext();
      context = consumerInstrumenter().start(parentContext, message);
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
      consumerInstrumenter().end(context, message, null, exception);
    }
  }
}
