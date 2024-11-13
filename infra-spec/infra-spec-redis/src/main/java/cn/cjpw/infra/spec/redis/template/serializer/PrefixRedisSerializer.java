package cn.cjpw.infra.spec.redis.template.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Arrays;

/**
 * 加入指定前缀的序列化工具
 */
public class PrefixRedisSerializer<K> implements RedisSerializer<K> {
    /**
     * 前缀
     */
    private K prefix;
    /**
     * 被包装序列化工具
     */
    private RedisSerializer<K> delegate;

    public PrefixRedisSerializer(K prefix, RedisSerializer<K> redisSerializer) {
        this.prefix = prefix;
        this.delegate = redisSerializer;
    }

    public K getPrefix() {
        return prefix;
    }

    public RedisSerializer<K> getDelegate() {
        return delegate;
    }

    private byte[] serializePrefix() {
        return delegate.serialize(prefix);
    }

    private byte[] serializeKey(K k) {
        return delegate.serialize(k);
    }

    @Override
    public byte[] serialize(K k) throws SerializationException {
        return concat(serializePrefix(), serializeKey(k));
    }

    @Override
    public K deserialize(byte[] bytes) throws SerializationException {
        return delegate.deserialize(restore(serializePrefix(), bytes));
    }

    private byte[] concat(byte[] prefix, byte[] source) {
        byte[] result = Arrays.copyOf(prefix, prefix.length + source.length);
        System.arraycopy(source, 0, result, prefix.length, source.length);
        return result;
    }

    private byte[] restore(byte[] prefix, byte[] source) {
        return Arrays.copyOfRange(source, prefix.length, source.length);
    }
}