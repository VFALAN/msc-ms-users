package com.msc.ms.users.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class FieldsUtils {
    public static boolean validIfClassHasField(Class pClass, String pField) {
        var hasField = false;
        final var levels = pField.split("\\.");
        Class tempClass = pClass;
        for (var field : levels) {
            try {
                final var tempField = tempClass.getDeclaredField(field);
                if (isPrimitive(tempField)) {
                    hasField = true;
                } else {
                    tempClass = tempField.getType();
                }
            } catch (NoSuchFieldException ex) {
                log.error("No such field found: {} in class: {}", field, pClass.getCanonicalName());
                hasField = false;
            }
        }

        return hasField;
    }

    public static boolean isPrimitive(Field pField) {
        final var clazz = pField.getType();
        return clazz == String.class || clazz == Integer.class || clazz == Boolean.class || clazz == Date.class;
    }

    public static List<String> getFieldsNames(Class clazz){
        return Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toList();
    }
}
