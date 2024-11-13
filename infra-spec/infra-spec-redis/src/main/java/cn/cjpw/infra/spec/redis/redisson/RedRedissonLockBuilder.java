package cn.cjpw.infra.spec.redis.redisson;//package cn.cjpw.infra.spec.redis.redisson;
//
//import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
//import com.google.gson.JsonObject;
//import com.google.gson.internal.LinkedHashTreeMap;
//import java.util.Map;
//import java.util.Objects;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//
///**
// * 红锁构造器
// * @author jun.chen1
// * @since 2023/5/19 11:20
// **/
//public class RedRedissonLockBuilder extends AbstractRedissonLockBuilder<RedRedissonLockBuilder>{
//
//    private Map<String, Integer> lockNameMap;
//    RLock rLock;
//
//
//    public RedRedissonLockBuilder() {
//        defaultStrategy();
//    }
//
//    public RedRedissonLockBuilder(RedissonClient redissonClient) {
//        this.defaultStrategy();
//        this.redissonClient = redissonClient;
//    }
//
//    public static RedRedissonLockBuilder of(String lockName) {
//        return of(lockName, null);
//    }
//
//    public static RedRedissonLockBuilder of(String lockName, LockExpire lockExpire) {
//        RedRedissonLockBuilder locker = new RedRedissonLockBuilder();
//        locker.lockNameMap.put(lockName, NORMAL_LOCK);
//        locker.lockExpire = lockExpire;
//        return locker;
//    }
//
//    /**
//     * 构造红锁
//     * @return
//     */
//   @Override
//    public RLock build() {
//       if (Objects.isNull(this.rLock)) {
//
//       }
//    }
//
//
//}
