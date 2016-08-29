package com.yudong.sensitive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yudong.sensitive.util.SensitiveUtil;

import java.lang.reflect.Array;
import java.util.*;

/**
 * user:zyd
 * date:2016/8/29
 * time:10:43
 * version:1.0.0
 */
public class SENSITIVE {
    /**
     * 获取脱敏json串 仅仅支持String类型<注意：递归引用会导致java.lang.StackOverflowError>
     *
     * @param javaBean
     * @return
     */
    public static String sensitiveToJson(Object javaBean) {
        String json = null;
        if (null != javaBean) {
            Class<? extends Object> raw = javaBean.getClass();
            try {
                if (raw.isInterface())
                    return json;

                Object clone = JSON.parseObject(JSON.toJSONString(javaBean), javaBean.getClass());
                Set<Integer> referenceCounter = new HashSet<Integer>();
                SensitiveUtil.replace(SensitiveUtil.findAllField(raw), clone, referenceCounter);
//                json = JSON.toJSONString(clone);
                json = clone.toString();
                referenceCounter.clear();
            } catch (Throwable e) {
                return e.getMessage();
            }
        }
        return json;
    }

    /**
     * 获取脱敏json串,所有类型 <注意：递归引用会导致java.lang.StackOverflowError>
     *
     * @param javaBean
     * @return
     */
    public static String sensitiveToJsonAllType(Object javaBean) {
        String json = null;
        if (null != javaBean) {
            Class<? extends Object> raw = javaBean.getClass();
            try {
                Object clone = JSON.parseObject(JSON.toJSONString(javaBean), javaBean.getClass());
                Set<Integer> referenceCounter = new HashSet<Integer>();
                if (raw.isArray() && !raw.getComponentType().isPrimitive()) {
                    int len = Array.getLength(clone);
                    for (int i = 0; i < len; i++) {
                        Object arrayObject = Array.get(clone, i);
                        SensitiveUtil.replace(arrayObject, referenceCounter);
                    }
                } else if (clone instanceof Collection<?>) {
                    Collection<?> c = (Collection<?>) clone;
                    Iterator<?> it = c.iterator();
                    while (it.hasNext()) {
                        Object collectionObj = it.next();
                        Class<? extends Object> collectionObjClass = collectionObj.getClass();
                        if (collectionObjClass.isPrimitive()
                                || String.class.equals(collectionObjClass))
                            break;
                        SensitiveUtil.replace(collectionObj, referenceCounter);
                    }
                } else if (clone instanceof Map<?, ?>) {
                    Collection<?> c = (Collection<?>) clone;
                    Iterator<?> it = c.iterator();
                    while (it.hasNext()) {
                        Object collectionObj = it.next();
                        Class<? extends Object> collectionObjClass = collectionObj.getClass();
                        if (collectionObjClass.isPrimitive()
                                || String.class.equals(collectionObjClass))
                            break;
                        SensitiveUtil.replace(collectionObj, referenceCounter);
                    }
                } else {
                    SensitiveUtil.replace(clone, referenceCounter);
                }
                json = JSON.toJSONString(clone, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
                referenceCounter.clear();
            } catch (Throwable e) {
                return e.getMessage();
            }
        }
        return json;
    }
}
