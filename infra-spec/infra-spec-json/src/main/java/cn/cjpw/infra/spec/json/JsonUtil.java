package cn.cjpw.infra.spec.json;

import cn.cjpw.infra.spec.base.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json处理工具，不做高度定制
 *
 * @author jun.chen1
 * @since 2024/9/13 14:53
 **/
public class JsonUtil {

    private static JsonOperation DELEGATE = new JacksonOperation();

    public static void changeOperation(JsonOperation jsonOperation) {
        DELEGATE = jsonOperation;
    }

    public static String toJsonStr(Object val) {
        return DELEGATE.toJsonStr(val);
    }

    public static <T> T toBean(Object val, Class<T> z) {
        return DELEGATE.toBean(val, z);
    }

    public static <T> T toBean(Object val, TypeReference<T> z) {
        return DELEGATE.toBean(val, z);
    }

    public static <K, V, M extends Map<K, V>> M toMap(Object val, TypeReference<M> z) {
        return DELEGATE.toMap(val, z);
    }

    public static <T> List<T> toList(Object val, Class<T> z) {
        return DELEGATE.toList(val, z);
    }

    public static <T> List<T> toList(Object val, TypeReference<T> z) {
        return DELEGATE.toList(val, z);
    }

    public static <T> Set<T> toSet(Object val, Class<T> z) {
        return DELEGATE.toSet(val, z);
    }

    public static <T> Set<T> toSet(Object val, TypeReference<T> z) {
        return DELEGATE.toSet(val, z);
    }


}
