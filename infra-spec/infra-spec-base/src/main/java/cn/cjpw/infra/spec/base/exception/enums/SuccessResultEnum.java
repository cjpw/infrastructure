package cn.cjpw.infra.spec.base.exception.enums;

/**
 * 返回状态码
 * ClassName: ResultCode 
 * @Description: TODO
 * @author hao.tang
 * @date 2019年10月28日 下午8:03:45
 */
public enum SuccessResultEnum {
	/* 成功状态码 */
    SUCCESS("0000", "操作成功");

    private String code;
    private String msg;

    SuccessResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}

