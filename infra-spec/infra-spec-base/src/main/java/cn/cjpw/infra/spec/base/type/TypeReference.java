package cn.cjpw.infra.spec.base.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具，用于复杂泛型对象序列化、反序列化公呢个
 *
 * like jackson {@link com.fasterxml.jackson.core.type.TypeReference}
 * @param <T>
 */
public abstract class TypeReference<T> implements Comparable<TypeReference<T>>
{
    protected final Type _type;
    
    protected TypeReference()
    {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) { // sanity check, should never happen
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() { return _type; }
    
    @Override
    public int compareTo(TypeReference<T> o) { return 0; }
}

