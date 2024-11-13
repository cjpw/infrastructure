package cn.cjpw.infra.spec.redis.template;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashSet;
import java.util.Set;

/**
 * 对RedisTemplate做了一些基础扩展
 *
 * @author chenjun
 * @since 2019/11/4 17:52
 */
public abstract class AbstractRedisTemplate<K, V> extends RedisTemplate<K, V> implements TemplateOperationExtend<K, V> {
    /**
     * key命名中分隔符
     */
    static final String DELIMITER = ":";

    private StringRedisSerializer stringRedisSerializer;

    public AbstractRedisTemplate() {
        this.stringRedisSerializer = new StringRedisSerializer();
    }

    @Override
    public Set<K> scanKeysByByte(byte[] key) {
        return scan(key);
    }

    @Override
    public Set<K> scanKeys(K key) {
        final String fKey = keyToString(key);
        return scan(fKey);
    }

    private Set<K> scan(String key) {
        return execute(connection -> {
            Set<K> keySet = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(KeyScanOptions.scanOptions().match(key).count(200).build());
            while (cursor.hasNext()) {
                byte[] bKey = cursor.next();
//                String strKey=new String(bKey);
                K curKey = (K) this.getKeySerializer().deserialize(bKey);
                keySet.add(curKey);
            }
            return keySet;
        }, isExposeConnection());
    }

    private Set<K> scan(byte[] key) {
        return execute(connection -> {
            Set<K> keySet = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(KeyScanOptions.scanOptions().match(key).count(200).build());
            while (cursor.hasNext()) {
                byte[] bKey = cursor.next();
//                String strKey=new String(bKey);
                K curKey = (K) this.getKeySerializer().deserialize(bKey);
                keySet.add(curKey);
            }
            return keySet;
        }, isExposeConnection());
    }


    @Override
    public long scanKeySizeByByte(byte[] key) {
        return scanSize(key);
    }

    @Override
    public long scanKeySize(K key) {
        final String fKey = keyToString(key);
        return scanSize(fKey);
    }

    private long scanSize(String key) {
        return execute(connection -> {
            long count = 0;
            Cursor<byte[]> cursor = connection.scan(KeyScanOptions.scanOptions().match(key).count(200).build());
            while (cursor.hasNext()) {
                count++;
            }
            return count;
        }, isExposeConnection());
    }

    private long scanSize(byte[] key) {
        return execute(connection -> {
            long count = 0;
            Cursor<byte[]> cursor = connection.scan(KeyScanOptions.scanOptions().match(key).count(200).build());
            while (cursor.hasNext()) {
                count++;
            }
            return count;
        }, isExposeConnection());
    }

    /**
     * 把key转换为string
     *
     * @param key
     * @return
     */
    protected String keyToString(K key) {
        byte[] realKeyBytes = ((RedisSerializer<K>) getKeySerializer()).serialize(key);
        return keyToStringByByte(realKeyBytes);
    }

    /**
     * 把key转换为string
     *
     * @param key
     * @return
     */
    protected String keyToStringByByte(byte[] key) {
        return stringRedisSerializer.deserialize(key);
    }

    /**
     * 设置key的通用前缀，用于应用级隔离
     *
     * @param namespace
     */
    abstract void setKeyPrefix(K namespace);
}
