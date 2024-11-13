package cn.cjpw.infra.spec.redis.redisson.callback.impl;//package cn.cjpw.infra.spec.redis.lock.callback.impl;
//
//import cn.cjpw.infra.spec.redis.lock.callback.LockFailCallBack;
//import cn.cjpw.infra.spec.redis.lock.result.LockResult;
//import org.redisson.api.RLock;
//import org.slf4j.Logger;
//
///**
// * 获取锁失败时，仅打印少量异常信息
// *
// * @author chenjun
// * @since 2019/7/23 16:48
// */
//public class LogFailCallBack<Result extends LockResult<RLock>> implements LockFailCallBack<RLock, Result> {
//
//    private Logger logger;
//
//    public LogFailCallBack(Logger logger) {
//        this.logger = logger;
//    }
//
//    @Override
//    public void accept(Result lockResult) {
//        String lockId = lockResult.getLock().getName();
//        String desc = null;
//        if (lockResult.getInterruptedException() != null) {
//            this.logger.warn("获取锁操作被中断,key={}" + lockId, lockResult.getInterruptedException());
//        } else {
//            this.logger.info("获取锁超时,key={}" + ,);
//        }
//    }
//
//}
