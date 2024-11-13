package cn.cjpw.infra.spec.json;

import cn.cjpw.infra.spec.base.type.TypeReference;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json常用操作
 *
 * @author jun.chen1
 * @since 2024/9/13 13:57
 **/
public interface JsonOperation {

    String toJsonStr(Object val);

    <T> T toBean(Object val, Class<T> z);

    <T> T toBean(Object val, TypeReference<T> z);

    <K, V, M extends Map<K, V>> M toMap(Object val, TypeReference<M> z);

    <T> List<T> toList(Object val, Class<T> z);

    <T> List<T> toList(Object val, TypeReference<T> z);

    default <T> Set<T> toSet(Object val, Class<T> z) {
        return new HashSet<>(toList(val, z));
    }

    default <T> Set<T> toSet(Object val, TypeReference<T> z) {
        return new HashSet<>(toList(val, z));
    }
}
