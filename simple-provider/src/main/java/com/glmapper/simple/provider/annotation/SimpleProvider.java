package com.glmapper.simple.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pre_fantasy
 * @create 2018-06-24 14:50
 * @desc
 **/

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component  /*标明可被 Spring 扫描*/
public @interface SimpleProvider {
    Class<?> value();
}
