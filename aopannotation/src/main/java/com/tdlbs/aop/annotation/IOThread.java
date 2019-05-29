package com.tdlbs.aop.annotation;

import com.tdlbs.aop.enums.ThreadType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IOThread {

    /**
     * @return 子线程的类型，默认是多线程池
     */
    ThreadType value() default ThreadType.Fixed;
}