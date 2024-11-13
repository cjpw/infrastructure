package cn.cjpw.infra.spec.json;

import cn.cjpw.infra.spec.base.type.BaseType;

/**
 * @author jun.chen1
 * @since 2024/9/13 14:24
 **/
public class BaseTypeConverter {

    public static <T> T convertTo(Object val, Class<T> z) {
        if (BaseType.isBaseType(z)) {
            if (BaseType.isBaseType(val.getClass())) {
                return (T) val;
            } else {
                String s = val.toString();
                if (z == Boolean.TYPE) {
                    return (T) Boolean.valueOf(s);
                } else if (z == Byte.TYPE) {
                    return (T) Byte.valueOf(s);
                } else if (z == Short.TYPE) {
                    return (T) Short.valueOf(s);
                } else if (z == Character.TYPE) {
                    return (T) (Character) s.charAt(0);
                } else if (z == Integer.TYPE) {
                    return (T) Integer.valueOf(s);
                } else if (z == Long.TYPE) {
                    return (T) Long.valueOf(s);
                } else if (z == Float.TYPE) {
                    return (T) Float.valueOf(s);
                } else if (z == Double.TYPE) {
                    return (T) Double.valueOf(s);
                } else if (z == Void.TYPE) {
                    return null;
                } else {
                    throw new IllegalArgumentException("unrecognized primitive type:" + z.getName());
                }
            }
        } else {
            throw new UnsupportedOperationException("can't convert basic type :" + z.getName());
        }
    }
}
