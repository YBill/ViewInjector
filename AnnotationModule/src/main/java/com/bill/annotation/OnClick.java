package com.bill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Bill on 2018/8/24.
 */

@Target(METHOD)
@Retention(CLASS)
public @interface OnClick {
    int value();
}
