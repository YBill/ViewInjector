package com.bill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Bill on 2018/8/17.
 */

@Retention(CLASS)
@Target(FIELD)
public @interface BindView {
    int value();
}
