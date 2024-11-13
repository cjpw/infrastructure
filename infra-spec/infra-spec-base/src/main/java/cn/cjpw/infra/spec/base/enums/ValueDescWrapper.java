package cn.cjpw.infra.spec.base.enums;


import java.io.Serializable;

/**
 * 实际值与显示值映射enum
 * value 实际值
 * desc 显示值
 *
 * @author jun.chen1
 * @since 2020/6/4 15:41
 */
public interface ValueDescWrapper<T extends Serializable> extends ValueWrapper<T> {
    String getDesc();

}
