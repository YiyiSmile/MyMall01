package indi.tom.mymall01.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//autoRedirect 有意义么？
public @interface LoginRequired {
    @AliasFor("autoRedirect")
    boolean value() default true;
    @AliasFor("value")
    boolean autoRedirect() default true;
}
