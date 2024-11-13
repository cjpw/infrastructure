package cn.cjpw.infra.spec.redis.lock.extention;

/**
 * 锁成功后添加的描述信息
 *
 * @author jun.chen1
 * @since 2022/1/25 17:04
 **/
public class HolderAdditional {

    /**
     * 锁住的操作描述
     */
    private String operation;
    /**
     * 调用链id
     */
    private String tid;

    /**
     * 获取 锁住的操作描述
     *
     * @return operation 锁住的操作描述
     */
    public String getOperation() {
        return this.operation;
    }

    /**
     * 设置 锁住的操作描述
     *
     * @param operation 锁住的操作描述
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * 获取 调用链id
     *
     * @return tid 调用链id
     */
    public String getTid() {
        return this.tid;
    }

    /**
     * 设置 调用链id
     *
     * @param tid 调用链id
     */
    public void setTid(String tid) {
        this.tid = tid;
    }
}
