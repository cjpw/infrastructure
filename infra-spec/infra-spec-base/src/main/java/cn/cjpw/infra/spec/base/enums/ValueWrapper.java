package cn.cjpw.infra.spec.base.enums;

import java.io.Serializable;

/**
 * 描述一个实际值的枚举
 *
 * @author  jun.chen1
 * @since 2020/6/4 15:41
 */
public interface ValueWrapper<T extends Serializable> {

    /**
     * 枚举实际使用值
     * @return
     */
    T getValue();

    /**
     * 通过value值来判断是否是该枚举类
     *
     * @param value
     * @return
     */
    default boolean eqValue(T value) {
        return getValue().equals(value);
    }

    /**
     * 通过value值来判断是否不是该枚举类
     *
     * @param value
     * @return
     */
    default boolean neValue(T value){return !eqValue(value);}

}
