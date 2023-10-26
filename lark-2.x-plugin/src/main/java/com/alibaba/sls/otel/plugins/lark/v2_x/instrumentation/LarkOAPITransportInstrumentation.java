package com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation;

import static com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation.LarkSingletons.instrumenter;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.named;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class LarkOAPITransportInstrumentation implements TypeInstrumentation {
  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return named("com.lark.oapi.core.Transport");
  }

  @Override
  public void transform(TypeTransformer transformer) {
    transformer.applyAdviceToMethod(
        named("send").and(isStatic()), this.getClass().getName() + "$SendAdvice");
  }

  @SuppressWarnings("unused")
  public static class SendAdvice {
    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter(
        @Advice.Argument(value = 3) String path,
        @Advice.Argument(value = 2) String method,
        @Advice.Local("otelContext") Context context,
        @Advice.Local("otelScope") Scope scope) {
      Context parentContext = currentContext();

      context = instrumenter().start(parentContext, String.format("%s %s", method, path));
      scope = context.makeCurrent();
    }

    @Advice.OnMethodExit(onThrowable = Exception.class)
    public static void onExit(
        @Advice.Thrown Exception exception,
        @Advice.Argument(value = 3) String path,
        @Advice.Argument(value = 2) String method,
        @Advice.Local("otelContext") Context context,
        @Advice.Local("otelScope") Scope scope) {
      if (scope == null) {
        return;
      }

      scope.close();
      instrumenter().end(context, String.format("%s %s", method, path), null, null);
    }
  }
}
