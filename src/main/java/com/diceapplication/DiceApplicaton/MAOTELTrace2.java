package com.diceapplication.DiceApplicaton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method should be traced using the custom OpenTelemetry instrumentation. When a method is
 * annotated with {@code @MAOTELTrace}, the instrumentation agent will automatically create a span at method entry and
 * close the span at method exit, capturing execution duration and any thrown exceptions.
 *
 * <p>
 * This annotation is useful for tracing business-critical or performance-sensitive methods without relying on
 * framework-level annotations such as {@code @Transactional}. It provides fine-grained control over where tracing
 * should be applied.
 *
 * <p>
 * Note: This annotation requires that the OpenTelemetry Java agent is running with the custom extension that recognizes
 * and instruments {@code @MAOTELTrace} annotated methods.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MAOTELTrace2
{
}
