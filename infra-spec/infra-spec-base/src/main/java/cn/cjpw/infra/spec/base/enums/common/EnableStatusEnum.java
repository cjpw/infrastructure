package cn.cjpw.infra.spec.base.enums.common;


import cn.cjpw.infra.spec.base.enums.ValueDescWrapper;

/**
 * 开关类型枚举
 *
 * @author  jun.chen1
 * @since 2020/6/4 15:41
 */
public enum EnableStatusEnum implements ValueDescWrapper<Integer> {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");


    private Integer code;
    private String desc;

    EnableStatusEnum(Integer code, String value) {
        this.code = code;
        this.desc = value;
    }


    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
