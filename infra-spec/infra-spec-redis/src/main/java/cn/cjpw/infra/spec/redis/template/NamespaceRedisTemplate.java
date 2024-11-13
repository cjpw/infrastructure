package cn.cjpw.infra.spec.redis.template;//package cn.cjpw.infra.spec.redis.template;
//
//import cn.hutool.core.util.StrUtil;
//import cn.cjpw.infra.spec.redis.template.serializer.PrefixRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
///**
// * key中包含命名前缀的redis数据模板操作类
// *
// * @author chenjun
// * @since 2019/11/4 17:37
// */
//abstract class NamespaceRedisTemplate<K, V> extends AbstractRedisTemplate<K, V> {
//
//    NamespaceRedisTemplate(String namespace, RedisSerializer redisSerializer) {
//        String namespaceReal = checkAndConvertNamespace(namespace);
//        if (StrUtil.isNotBlank(namespaceReal)) {
//            PrefixRedisSerializer<K> keySerializer = new PrefixRedisSerializer<>(namespaceReal, redisSerializer);
//            setKeySerializer(keySerializer);
//        } else {
//            setKeySerializer(redisSerializer);
//        }
//    }
//
//    private String checkAndConvertNamespace(String namespace) {
//        String rt = namespace;
//        if (StrUtil.isNotBlank(rt)) {
//            if (rt.lastIndexOf(DELIMITER) != rt.length() - 1) {
//                rt += ":";
//            }
//        } else {
//            rt = "";
//        }
//        return rt;
//    }
//
////    protected PrefixRedisSerializerWrapper<K> getRealKeySerializer() {
////        PrefixRedisSerializerWrapper<K> keySerializer = (PrefixRedisSerializerWrapper<K>) getKeySerializer();
////        return keySerializer;
////    }
////
////    /**
////     * 获取redis中实际的key
////     *
////     * @param key
////     * @return
////     */
////    protected K getRealKey(K key) {
////        return getRealKeySerializer().deserializeFull(getRealKeySerializer().serialize(key));
////    }
//
//}
