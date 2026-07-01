package net.pkhapps.dispatchsimulator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation put on <b>stateful</b> classes that are <b>not thread-safe</b>. This means thread safety must be handled
 * by the caller. The annotation exists for documentation purposes only and has no functional meaning.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface NotThreadSafe {
}
