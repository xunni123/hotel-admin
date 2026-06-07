package com.xunni.hotel.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xunni.hotel.serializer.SensitiveJsonSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感字段脱敏注解
 * 
 * 标注在实体类的字段上，返回JSON时会自动脱敏
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive {

    SensitiveType type() default SensitiveType.DEFAULT;

    int prefix() default 3;

    int suffix() default 4;

    enum SensitiveType {
        DEFAULT,
        PHONE,
        EMAIL,
        ID_CARD,
        BANK_CARD,
        NAME,
        ADDRESS,
        CUSTOM
    }
}