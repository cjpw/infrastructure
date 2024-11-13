package cn.cjpw.infra.spec.base.enums.common;


import cn.cjpw.infra.spec.base.enums.ValueDescWrapper;

/**
 * 数字类型映射布尔的枚举
 *
 * @author  jun.chen1
 * @since 2020/6/4 15:41
 */
public enum BoolEnum implements ValueDescWrapper<Integer> {

    NO(0, "否"),
    YES(1, "是");

    BoolEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;


    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

}