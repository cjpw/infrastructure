package cn.cjpw.infra.spec.redis.lock.result;

import cn.cjpw.infra.spec.redis.lock.extention.HolderAdditional;
import org.redisson.api.RedissonClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * MultiLock 加锁结果信息扩展
 *
 * @author jun.chen1
 * @since 2023/6/26 11:17
 **/
public class MultiLockResult<T> extends LockResult<T> {

    private final List<String> lockNames;

    private final RedissonClient redissonClient;

//    private final Function<Collection<String>,Map<String,HolderAdditional>> holderAdditionalFunction;

    public MultiLockResult(List<String> lockNames, RedissonClient redissonClient) {
        this.lockNames = lockNames;
        this.redissonClient = redissonClient;
//        this.holderAdditionalFunction=holderAdditionalFunction;
    }

    /**
     * 获取任意一个有效的锁扩展信息
     *
     * @return
     */
    public HolderAdditional getAnyValidHolderAdditional() {
        long l = redissonClient.getKeys().countExists(lockNames.toArray(new String[0]));
        if (l > 0) {
            // 使用IntStream来分组
            Collection<List<String>> collect = partition(lockNames);
            for (List<String> lrs : collect) {
                Map<String, HolderAdditional> map = redissonClient.getBuckets().get(lrs.toArray(new String[0]));
                if (!map.isEmpty()) {
                    return map.values().stream().findAny().orElse(null);
                }
            }
        }
        return null;
    }

    /**
     * 获取关联的所有锁扩展信息
     *
     * @return
     */
    public Map<String, HolderAdditional> getAllValidHolderAdditional() {
        Collection<List<String>> partition = partition(lockNames);
        Map<String, HolderAdditional> collect = partition.stream()
                .map(item -> {
                    Map<String, HolderAdditional> map = redissonClient.getBuckets().get(item.toArray(new String[0]));
                    return map;
                })
                .collect(Collectors.reducing(new HashMap<>(), (a, b) -> {
                    a.putAll(b);
                    return a;
                }));
        return collect;
    }

    /**
     * 分组
     *
     * @param from
     * @return
     */
    private Collection<List<String>> partition(List<String> from) {
        int groupSize = 500;
        Collection<List<String>> collect = IntStream.range(0, from.size())
                .boxed()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(i -> i / groupSize,
                        Collectors.mapping(i -> from.get(i), Collectors.toList())), item -> item.values()));
        return collect;
    }
}
