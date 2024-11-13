package cn.cjpw.infra.spec.redis.template;

import cn.hutool.core.util.StrUtil;
import cn.cjpw.infra.spec.json.JacksonOperation;
import cn.cjpw.infra.spec.json.JsonOperation;
import cn.cjpw.infra.spec.redis.template.serializer.StringNamespaceKeySerializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Set;

/**
 * 基于json的redis数据模板操作类
 *
 * @author chenjun
 * @since 2019/11/4 17:27
 */
public class JsonRedisTemplate extends AbstractRedisTemplate<String, Object> {

    private JsonOperation jsonOperation;

    private RedisSerializer<String> defaultValueSerializer;

    public JsonRedisTemplate(String namespace) {
        this.jsonOperation = new JacksonOperation(new JsonMapper());
        /**
         * 不推荐使用 {@link com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer}
         * 这种虽然方便做反序列化，但是会强行写入class元数据信息，如果因为项目代码迁移发生不匹配，反序列化始终会失败
         */
        defaultValueSerializer = new Jackson2JsonRedisSerializer<>(String.class);
        serializerConfig(namespace);
    }

    private void serializerConfig(String namespace) {
        setKeyPrefix(namespace);
        setHashKeySerializer(new StringRedisSerializer());
        setHashValueSerializer(defaultValueSerializer);
        setValueSerializer(defaultValueSerializer);
    }

    @Override
    public HashOperations<String, String, Object> opsForHash() {
        return super.opsForHash();
    }

    @Override
    void setKeyPrefix(String namespace) {
        String realNamespace = StringNamespaceKeySerializer.checkAndConvertNamespace(namespace);
        if (StrUtil.isNotBlank(realNamespace)) {
            setKeySerializer(new StringNamespaceKeySerializer(realNamespace));
        } else {
            setKeySerializer(new StringRedisSerializer());
        }
    }

    @Override
    public <T> T toJavaBean(Object val, Class<T> z) {
        return jsonOperation.toBean(val, z);
    }


    @Override
    public <T> List<T> toJavaList(Object val, Class<T> z) {
        return jsonOperation.toList(val, z);
    }

    @Override
    public <T> Set<T> toJavaSet(Object val, Class<T> z) {
        return jsonOperation.toSet(val, z);
    }

}
