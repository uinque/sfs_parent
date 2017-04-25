package com.hk.commons.utils.json.annotation;

import java.lang.annotation.*;

/**
 * Created by linhy on 2017/4/19.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface JsonIgnore {
}
