package cn.cjpw.infra.spec.redis.template;

import java.util.List;
import java.util.Set;

/**
 * 对redistemplate 公共操作进行扩展声明
 *
 * @author chenjun
 * @since 2019/11/5 9:19
 */
public interface TemplateOperationExtend<K, V> {

    /**
     * 将序列化过的缓存值转为指定的java对象 无内容返回null
     *
     * @param val
     * @param z
     */
    <T> T toJavaBean(Object val, Class<T> z);

//    /**
//     * 将序列化过的缓存值转为指定的java对象 无内容返回null
//     *
//     * @param val
//     * @param
//     */
//    default V toJavaBean(Object val) {
//        return toJavaBean(val, getValueType());
//    }

    /**
     * 将序列化过的缓存值转为指定的java对象 默认返回arrayList 无内容返回null
     *
     * @param val
     * @param z
     */
    <T> List<T> toJavaList(Object val, Class<T> z);

//    /**
//     * 将序列化过的缓存值转为指定的java对象 默认返回arrayList 无内容返回null
//     *
//     * @param val
//     */
//    default List<V> toJavaList(Object val) {
//        return toJavaList(val, getValueType());
//    }

    /**
     * 将序列化过的缓存值转为指定的java对象 默认返回hashSet 无内容返回null
     *
     * @param val
     * @param z
     */
    <T> Set<T> toJavaSet(Object val, Class<T> z);

//    /**
//     * 将序列化过的缓存值转为指定的java对象 默认返回arrayList 无内容返回null
//     *
//     * @param val
//     */
//    default Set<V> toJavaSet(Object val) {
//        return toJavaSet(val, getValueType());
//    }

    /**
     * 使用scan命令，模糊匹配key 在key特别多的情况下，可以提供redis吞吐
     *
     * @param key
     * @return
     */
    Set<K> scanKeys(K key);

//    /**
//     * 使用scan命令，模糊匹配key 在key特别多的情况下，可以提供redis吞吐
//     *
//     * @param key
//     * @return
//     */
//    Set<K> scanKeys(K key, int pageLimit, DataType dataType);

    /**
     * 使用scan命令，模糊匹配key 在key特别多的情况下，可以提供redis吞吐 key 已完成序列化
     *
     * @param key
     * @return
     */
    Set<K> scanKeysByByte(byte[] key);

//    /**
//     * 使用scan命令，模糊匹配key 在key特别多的情况下，可以提供redis吞吐 key 已完成序列化
//     *
//     * @param key
//     * @return
//     */
//    Set<K> scanKeysByByte(byte[] key, int pageLimit, DataType dataType);

    /**
     * 使用scan命令，模糊匹配key个数 在key特别多的情况下，可以提供redis吞吐
     *
     * @param key
     * @return
     */
    long scanKeySize(K key);

//    /**
//     * 使用scan命令，模糊匹配key个数 在key特别多的情况下，可以提供redis吞吐
//     *
//     * @param key
//     * @return
//     */
//    long scanKeySize(K key, int pageLimit, DataType dataType);

    /**
     * 使用scan命令，模糊匹配key个数 在key特别多的情况下，可以提供redis吞吐 key 已完成序列化
     *
     * @param key
     * @return
     */
    long scanKeySizeByByte(byte[] key);

//    /**
//     * 使用scan命令，模糊匹配key个数 在key特别多的情况下，可以提供redis吞吐 key 已完成序列化
//     *
//     * @param key
//     * @return
//     */
//    long scanKeySizeByByte(byte[] key, int pageLimit, DataType dataType);

//    /**
//     * 获取 k 的class,k在实例中类型必须明确
//     *
//     * @return
//     */
//    default Class<K> getKeyType() {
//        return (Class<K>) ClassUtil.getTypeArgument(getClass(), 0);
//    }
//
//    /**
//     * 获取 v 的class,v在实例中类型必须明确
//     *
//     * @return
//     */
//    default Class<V> getValueType() {
//        return (Class<V>) ClassUtil.getTypeArgument(getClass(), 1);
//    }

}
