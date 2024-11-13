package cn.cjpw.infra.spec.redis.lock.bean;

import java.util.concurrent.TimeUnit;

/**
 * 锁时间信息
 *
 * @author chenjun
 * @since 2019/7/23 15:31
 */
public class LockExpire {

    /**
     * 锁持有最大时长，防止死锁
     * <p>
     * redisson 锁方案可以忽略此配置
     */
    public final static long MAX_RELEASE_TIME = -1;
    /**
     * 等待时长
     */
    private long waitTime;
    /**
     * 等待时长单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    /**
     * 使用锁时默认最大锁存活时间(秒)。
     * <p>
     * 如果不能保证锁正确释放，建议保证此值有效 >0
     */
    private Long leaseTime;

    public LockExpire(long wait) {
        this.waitTime = wait;
    }

    public LockExpire(long wait, TimeUnit timeUnit) {
        this.waitTime = wait;
        this.timeUnit = timeUnit;
    }

    public LockExpire(long wait, TimeUnit timeUnit, long release) {
        this.waitTime = wait;
        this.timeUnit = timeUnit;
        if (release > 0) {
            this.leaseTime = release;
        }
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    /**
     * 2s
     *
     * @return
     */
    public static LockExpire waitTwo() {
        return new LockExpire(2L, TimeUnit.SECONDS);
    }

    /**
     * 5s
     *
     * @return
     */
    public static LockExpire waitFive() {
        return new LockExpire(5L, TimeUnit.SECONDS);
    }

    /**
     * 一直等，直到锁被超时释放
     *
     * @return
     */
    public static LockExpire forever() {
        LockExpire lockExpire = new LockExpire(-1);
        return lockExpire;
    }

    /**
     * 0s，不等待
     *
     * @return
     */
    public static LockExpire noWait() {
        return new LockExpire(0, TimeUnit.SECONDS);
    }
}
