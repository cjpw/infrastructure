package cn.cjpw.infra.spec.json;

import cn.cjpw.infra.spec.base.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author jun.chen1
 * @since 2024/9/13 14:10
 **/
public class JacksonOperation implements JsonOperation {

    private final static ObjectMapper DEFAULT_MAPPER;
    private final ObjectMapper mapper;

    private NotJsonTypeConverter baseOperation = new NotJsonTypeConverter();

    static {
        DEFAULT_MAPPER = new ObjectMapper();
    }

    public JacksonOperation() {
        this.mapper = DEFAULT_MAPPER;
    }

    public JacksonOperation(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    @Override
    public String toJsonStr(Object val) {
        try {
            return mapper.writeValueAsString(val);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("can't write json", e);
        }
    }

    @Override
    public <T> T toBean(Object val, Class<T> z) {
        return baseOperation.toBean(val, z).orElseGet(() -> {
            try {
                if (Objects.isNull(val)) {
                    return null;
                }
                if (val instanceof String) {
                    return mapper.readValue(val.toString(), z);
                } else {
                    return mapper.readValue(mapper.writeValueAsString(val), z);
                }
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("can't write json", e);
            }
        });
    }

    @Override
    public <T> T toBean(Object val, TypeReference<T> z) {
        try {
            if (Objects.isNull(val)) {
                return null;
            }
            if (val instanceof String) {
                return mapper.readValue(val.toString(), mapper.constructType(z.getType()));
            } else {
                return mapper.readValue(mapper.writeValueAsString(val), mapper.constructType(z.getType()));
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("can't write json", e);
        }

    }

    @Override
    public <K, V, M extends Map<K, V>> M toMap(Object val, TypeReference<M> z) {
        try {
            if (Objects.isNull(val)) {
                return null;
            }
            if (val instanceof String) {
                return mapper.readValue(val.toString(), mapper.constructType(z.getType()));
            } else {
                return mapper.readValue(mapper.writeValueAsString(val), mapper.constructType(z.getType()));
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("can't write json", e);
        }
    }

    @Override
    public <T> List<T> toList(Object val, Class<T> z) {
        if (Objects.isNull(val)) {
            return null;
        }
        if (val instanceof String) {
            try {
                return mapper.readValue(val.toString(), new com.fasterxml.jackson.core.type.TypeReference<List<T>>() {
                    @Override
                    public Type getType() {
                        return mapper.getTypeFactory().constructCollectionType(List.class, z);
                    }
                });
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("can't write json", e);
            }
        } else {
            try {
                return toList(mapper.writeValueAsString(val), z);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("can't write json", e);
            }
        }
    }

    @Override
    public <T> List<T> toList(Object val, TypeReference<T> z) {
        if (Objects.isNull(val)) {
            return null;
        }
        if (val instanceof String) {
            try {
                return mapper.readValue(val.toString(), new com.fasterxml.jackson.core.type.TypeReference<List<T>>() {
                    @Override
                    public Type getType() {
                        return mapper.getTypeFactory().constructCollectionType(List.class, mapper.constructType(z.getType()));
                    }
                });
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("can't write json", e);
            }
        } else {
            try {
                return toList(mapper.writeValueAsString(val), z);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("can't write json", e);
            }
        }
    }
}
