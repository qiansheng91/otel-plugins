package com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.bytebuddy.matcher.ElementMatcher;

@AutoService(InstrumentationModule.class)
public class LarkOAPIInstrumentationModule extends InstrumentationModule {

  public LarkOAPIInstrumentationModule() {
    super("lark", "lark-oapi-2.0");
  }

  @Override
  public ElementMatcher.Junction<ClassLoader> classLoaderMatcher() {
    return AgentElementMatchers.hasClassesNamed("com.lark.oapi.core.Transport");
  }

  @Override
  public List<String> getAdditionalHelperClassNames() {
    return Stream.of(
            "com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation.LarkSingletons",
            "com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation.Lark2xAPISpanNameExtractor")
        .collect(Collectors.toList());
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return Arrays.asList(new LarkOAPITransportInstrumentation());
  }
}
