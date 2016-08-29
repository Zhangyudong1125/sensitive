package com.yudong.sensitive.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.yudong.sensitive.annotation.SENSITIVEField;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 敏感信息屏蔽工具
 * user:zyd
 * date:2016/8/29
 * time:10:57
 * version:1.0.0
 */
public class SensitiveUtil {
    public static void replace(Field[] fields, Object javaBean, Set<Integer> referenceCounter) throws IllegalArgumentException, IllegalAccessException {
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                if (null != javaBean) {
                    Object value = field.get(javaBean);
                    if (null != value) {
                        Class<?> type = value.getClass();
                        // 1.处理子属性，包括集合中的
                        if (type.isArray()) {
                            int len = Array.getLength(value);
                            for (int i = 0; i < len; i++) {
                                Object arrayObject = Array.get(value, i);
                                SensitiveUtil.replace(SensitiveUtil.findAllField(arrayObject.getClass()), arrayObject, referenceCounter);
                            }
                        } else if (value instanceof Collection<?>) {
                            Collection<?> c = (Collection<?>) value;
                            Iterator<?> it = c.iterator();
                            while (it.hasNext()) {
                                Object collectionObj = it.next();
                                SensitiveUtil.replace(SensitiveUtil.findAllField(collectionObj.getClass()), collectionObj, referenceCounter);
                            }
                        } else if (value instanceof Map<?, ?>) {
                            Map<?, ?> m = (Map<?, ?>) value;
                            Set<?> set = m.entrySet();
                            for (Object o : set) {
                                Entry<?, ?> entry = (Entry<?, ?>) o;
                                Object mapVal = entry.getValue();
                                SensitiveUtil.replace(SensitiveUtil.findAllField(mapVal.getClass()), mapVal, referenceCounter);
                            }
                        } else if (!type.isPrimitive()
                                && !StringUtils.startsWith(type.getPackage().getName(), "javax.")
                                && !StringUtils.startsWith(type.getPackage().getName(), "java.")
                                && !StringUtils.startsWith(field.getType().getName(), "javax.")
                                && !StringUtils.startsWith(field.getName(), "java.")
                                && referenceCounter.add(value.hashCode())) {
                            SensitiveUtil.replace(SensitiveUtil.findAllField(type), value, referenceCounter);
                        }
                    }
                    // 2. 处理自身的属性
                    SENSITIVEField annotation = field.getAnnotation(SENSITIVEField.class);
                    if (field.getType().equals(String.class) && null != annotation) {
                        String valueStr = (String) value;
                        if (StringUtils.isNotBlank(valueStr)) {
                            switch (annotation.type()) {
                                case CHINESE_NAME: {
                                    field.set(javaBean, EncryptStringUtil.certificateName(valueStr));
                                    break;
                                }
                                case ID_CARD: {
                                    field.set(javaBean, EncryptStringUtil.certificateNo(valueStr));
                                    break;
                                }
                                case FIXED_PHONE: {
                                    field.set(javaBean, EncryptStringUtil.fixedPhone(valueStr));
                                    break;
                                }
                                case MOBILE_PHONE: {
                                    field.set(javaBean, EncryptStringUtil.phone(valueStr));
                                    break;
                                }
                                case ADDRESS: {
                                    field.set(javaBean, EncryptStringUtil.address(valueStr, 4));
                                    break;
                                }
                                case EMAIL: {
                                    field.set(javaBean, EncryptStringUtil.email(valueStr));
                                    break;
                                }
                                case BANK_CARD: {
                                    field.set(javaBean, EncryptStringUtil.bankCardNo(valueStr));
                                    break;
                                }
                                case CNAPS_CODE: {
                                    field.set(javaBean, EncryptStringUtil.cnapsCode(valueStr));
                                    break;
                                }
                                case CVV2: {
                                    field.set(javaBean, EncryptStringUtil.cvv2(valueStr));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void replace(Object javaBean, Set<Integer> referenceCounter) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz;
        if (null != javaBean && null != (clazz = javaBean.getClass())
                && referenceCounter.add(javaBean.hashCode())) {
            Field[] fields = SensitiveUtil.findAllField(clazz);
            if (null != fields && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                        Object value = field.get(javaBean);
                        if (null != value) {
                            Class<?> type = value.getClass();
                            // 1.处理子属性，包括集合中的
                            if (type.isArray() && !type.getComponentType().isPrimitive()) {
                                int len = Array.getLength(value);
                                for (int i = 0; i < len; i++) {
                                    Object arrayObject = Array.get(value, i);
                                    SensitiveUtil.replace(arrayObject, referenceCounter);
                                }
                            } else if (value instanceof Collection<?>) {
                                Collection<?> c = (Collection<?>) value;
                                Iterator<?> it = c.iterator();
                                while (it.hasNext()) {
                                    Object collectionObj = it.next();
                                    Class<? extends Object> collectionObjClass = collectionObj.getClass();
                                    if (collectionObjClass.isPrimitive()
                                            || String.class.equals(collectionObjClass))
                                        break;
                                    SensitiveUtil.replace(collectionObj, referenceCounter);
                                }
                            } else if (value instanceof Map<?, ?>) {
                                Map<?, ?> m = (Map<?, ?>) value;
                                Set<?> set = m.entrySet();
                                for (Object o : set) {
                                    Entry<?, ?> entry = (Entry<?, ?>) o;
                                    Object mapVal = entry.getValue();
                                    SensitiveUtil.replace(mapVal, referenceCounter);
                                }
                            } else if (!type.isPrimitive()
                                    && null != type.getPackage()
                                    && !StringUtils.startsWith(type.getPackage().getName(), "javax.")
                                    && !StringUtils.startsWith(type.getPackage().getName(), "java.")
                                    && !StringUtils.startsWith(field.getType().getName(), "javax.")
                                    && !StringUtils.startsWith(field.getName(), "java.")) {
                                SensitiveUtil.replace(value, referenceCounter);
                            }
                        }
                        // 2. 处理自身的属性
                        SENSITIVEField annotation = field.getAnnotation(SENSITIVEField.class);
                        if (null != value && null != annotation) {
                            switch (annotation.type()) {
                                case CHINESE_NAME: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.certificateName((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.certificateName((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case ID_CARD: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.certificateNo((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.certificateNo((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case FIXED_PHONE: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.fixedPhone((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.fixedPhone((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case MOBILE_PHONE: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.phone((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.phone((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case ADDRESS: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.address((String) value, 4));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.address((String) v, 4));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case EMAIL: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.email((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.email((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case BANK_CARD: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.bankCardNo((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.bankCardNo((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case CNAPS_CODE: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.cnapsCode((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.cnapsCode((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case BANK_CARD_DATE: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.cardValidDate((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.cardValidDate((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                                case ALL: {
                                    if (field.getType().equals(String.class)) {
                                        field.set(javaBean, EncryptStringUtil.all((String) value));
                                    } else if (value instanceof Collection<?>) {
                                        Collection<Object> valueCollection = (Collection<Object>) value;
                                        if (!valueCollection.isEmpty()) {
                                            Object[] vlist = valueCollection.toArray();
                                            for (Object v : vlist) {
                                                if (v instanceof String) {
                                                    valueCollection.remove(v);
                                                    valueCollection.add(EncryptStringUtil.all((String) v));
                                                }
                                            }
                                            field.set(javaBean, valueCollection);
                                        }
                                    } else {
                                        continue;
                                    }
                                    break;
                                }
                            }
                        }
                    }
            }
        }
    }

    public static Field[] findAllField(Class<?> clazz) {
        Field[] fileds = clazz.getDeclaredFields();
        while (null != clazz.getSuperclass() && !Object.class.equals(clazz.getSuperclass())) {
            fileds = (Field[]) ArrayUtils.addAll(fileds, clazz.getSuperclass().getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fileds;
    }

    public static Method[] findAllMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        return methods;
    }
}
