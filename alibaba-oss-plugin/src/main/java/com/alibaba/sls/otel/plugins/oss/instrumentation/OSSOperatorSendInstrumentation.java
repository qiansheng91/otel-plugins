package com.alibaba.sls.otel.plugins.oss.instrumentation;

import static com.alibaba.sls.otel.plugins.oss.instrumentation.OSSSingletons.Instrumenter;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static net.bytebuddy.matcher.ElementMatchers.*;

import com.aliyun.oss.common.comm.RequestMessage;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class OSSOperatorSendInstrumentation implements TypeInstrumentation {
  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return hasSuperType(named("com.aliyun.oss.internal.OSSOperation"));
  }

  @Override
  public void transform(TypeTransformer transformer) {
    transformer.applyAdviceToMethod(
        named("doOperation").and(takesArguments(7)),
        this.getClass().getName() + "$DoOperationAdvice");
  }

  public static class DoOperationAdvice {
    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter(
        @Advice.This Object thiz,
        @Advice.Argument(value = 0) RequestMessage message,
        @Advice.Local("otelScope") Scope scope,
        @Advice.Local("otelContext") Context context) {
      System.out.println(thiz.getClass().getName());
      Context parentContext = currentContext();
      context = Instrumenter().start(parentContext, message);
      scope = context.makeCurrent();
    }

    @Advice.OnMethodExit(onThrowable = Exception.class)
    public static void onExit(
        @Advice.Thrown Exception exception,
        @Advice.Argument(value = 0) RequestMessage message,
        @Advice.Local("otelContext") Context context,
        @Advice.Local("otelScope") Scope scope) {
      if (scope == null) {
        return;
      }

      scope.close();
      Instrumenter().end(context, message, null, exception);
    }
  }
}
