package cn.cjpw.infra.spec.json;

import cn.cjpw.infra.spec.base.type.BaseType;

import java.util.Objects;
import java.util.Optional;

/**
 * @author jun.chen1
 * @since 2024/9/13 14:17
 **/
public class NotJsonTypeConverter {

    public String toJsonStr(Object val) {
        return Optional.ofNullable(val).map(Object::toString).orElse(null);
    }

    public <T> Optional<T> toBean(Object val, Class<T> z) {
        if (Objects.isNull(val)) {
            return Optional.empty();
        } else if (z.isAssignableFrom(val.getClass())) {
            return Optional.of(z.cast(val));
        } else if (BaseType.isBaseType(z)) {
            return Optional.of(val).map(item -> BaseTypeConverter.convertTo(item, z));
        }
        return Optional.empty();
    }
}
