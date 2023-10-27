package com.alibaba.sls.otel.plugins.oss.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.matcher.ElementMatcher;

@AutoService(InstrumentationModule.class)
public class AlibabaOSSInstrumentationModule extends InstrumentationModule {

  public static final String PACKAGE_NAME = "com.alibaba.sls.otel.plugins.oss";

  public AlibabaOSSInstrumentationModule() {
    super("aliyun-oss-client", "com.alibaba.oss-3.x");
  }

  @Override
  public ElementMatcher.Junction<ClassLoader> classLoaderMatcher() {
    return AgentElementMatchers.hasClassesNamed("com.aliyun.oss.internal.OSSOperation");
  }

  @Override
  public List<String> getAdditionalHelperClassNames() {

    return Arrays.asList(
        PACKAGE_NAME + ".instrumentation.OSSSingletons",
        PACKAGE_NAME + ".instrumentation.DoOperationTraceContextSetter",
        PACKAGE_NAME + ".instrumentation.OSSAttributeExtractor",
        PACKAGE_NAME + ".instrumentation.OSSSpanNameExtractor");
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return Arrays.asList(new OSSOperatorSendInstrumentation());
  }
}
