package cn.cjpw.infra.spec.redis.lock;//package cn.cjpw.infra.spec.redis.lock;
//
//import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
//import cn.cjpw.infra.spec.redis.lock.callback.ExecErrorCallBack;
//import cn.cjpw.infra.spec.redis.lock.callback.LockFailCallBack;
//import cn.cjpw.infra.spec.redis.lock.callback.LockFinalCallBack;
//import cn.cjpw.infra.spec.redis.lock.extention.HolderDescription;
//
///**
// * @author jun.chen1
// * @since 2023/5/15 18:46
// **/
//public class LockExecutionConfig {
//
//    private LockExpire lockExpire;
//    private LockFinalCallBack lockFinalCallBack;
//    private LockFailCallBack lockFailCallBack;
//    private ExecErrorCallBack execErrorCallBack;
//    private HolderDescription holderDescription;
//
//    public LockExpire getLockExpire() {
//        return lockExpire;
//    }
//
//    public void setLockExpire(LockExpire lockExpire) {
//        this.lockExpire = lockExpire;
//    }
//
//    public LockFinalCallBack getLockFinalCallBack() {
//        return lockFinalCallBack;
//    }
//
//    public void setLockFinalCallBack(LockFinalCallBack lockFinalCallBack) {
//        this.lockFinalCallBack = lockFinalCallBack;
//    }
//
//    public LockFailCallBack getLockFailCallBack() {
//        return lockFailCallBack;
//    }
//
//    public void setLockFailCallBack(LockFailCallBack lockFailCallBack) {
//        this.lockFailCallBack = lockFailCallBack;
//    }
//
//    public ExecErrorCallBack getExecErrorCallBack() {
//        return execErrorCallBack;
//    }
//
//    public void setExecErrorCallBack(ExecErrorCallBack execErrorCallBack) {
//        this.execErrorCallBack = execErrorCallBack;
//    }
//
//    public HolderDescription getHolderDescription() {
//        return holderDescription;
//    }
//
//    public void setHolderDescription(HolderDescription holderDescription) {
//        this.holderDescription = holderDescription;
//    }
//}
