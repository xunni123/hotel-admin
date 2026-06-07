package com.xunni.hotel.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.xunni.hotel.annotation.Sensitive;

import java.io.IOException;

/**
 * 敏感字段JSON序列化器
 */
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private Sensitive.SensitiveType type;
    private int prefix;
    private int suffix;

    public SensitiveJsonSerializer() {
    }

    public SensitiveJsonSerializer(Sensitive.SensitiveType type, int prefix, int suffix) {
        this.type = type;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeString(value);
            return;
        }
        String maskedValue = maskValue(value, type, prefix, suffix);
        gen.writeString(maskedValue);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            Sensitive annotation = property.getAnnotation(Sensitive.class);
            if (annotation != null) {
                return new SensitiveJsonSerializer(annotation.type(), annotation.prefix(), annotation.suffix());
            }
        }
        return this;
    }

    private String maskValue(String value, Sensitive.SensitiveType type, int prefix, int suffix) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        switch (type) {
            case PHONE:
                if (value.length() >= 11) {
                    return value.substring(0, 3) + "****" + value.substring(value.length() - 4);
                }
                break;
            
            case EMAIL:
                int atIndex = value.indexOf("@");
                if (atIndex > 0) {
                    String username = value.substring(0, atIndex);
                    String domain = value.substring(atIndex);
                    if (username.length() > 1) {
                        return username.charAt(0) + "***" + domain;
                    }
                    return "*" + domain;
                }
                break;
            
            case ID_CARD:
                if (value.length() >= 18) {
                    return value.substring(0, 3) + "**********" + value.substring(value.length() - 4);
                } else if (value.length() >= 15) {
                    return value.substring(0, 3) + "****" + value.substring(value.length() - 4);
                }
                break;
            
            case BANK_CARD:
                if (value.length() >= 16) {
                    return value.substring(0, 4) + "****" + value.substring(value.length() - 4);
                }
                break;
            
            case NAME:
                if (value.length() == 1) {
                    return value;
                } else if (value.length() == 2) {
                    return value.charAt(0) + "*";
                } else {
                    return value.charAt(0) + "**";
                }
            
            case ADDRESS:
                if (value.length() > 6) {
                    return value.substring(0, 4) + "***" + value.substring(value.length() - 2);
                }
                break;
            
            case CUSTOM:
                if (value.length() <= prefix + suffix) {
                    return value;
                }
                return value.substring(0, prefix) + "****" + value.substring(value.length() - suffix);
            
            case DEFAULT:
            default:
                if (value.length() <= 7) {
                    return value;
                }
                return value.substring(0, 3) + "****" + value.substring(value.length() - 4);
        }
        
        return value;
    }
}