package com.yudong.sensitive.annotation;

import com.yudong.sensitive.enummodel.SensitiveType;

import java.lang.annotation.*;

/**
 * 敏感信息注解标记
 * user:zyd
 * date:2016/8/29
 * time:10:49
 * version:1.0.0
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SENSITIVEField {
    public SensitiveType type();
}
