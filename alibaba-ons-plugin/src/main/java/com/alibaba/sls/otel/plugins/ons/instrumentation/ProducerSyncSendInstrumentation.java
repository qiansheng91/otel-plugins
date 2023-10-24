package com.alibaba.sls.otel.plugins.ons.instrumentation;

import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ProducerSyncSendInstrumentation implements TypeInstrumentation {
    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return null;
    }

    @Override
    public void transform(TypeTransformer typeTransformer) {
        typeTransformer.applyAdviceToMethod(named("publish").and(ElementMatchers.isPublic()),
                this.getClass().getName() + "$PublishMethodAdvice");
    }
}
