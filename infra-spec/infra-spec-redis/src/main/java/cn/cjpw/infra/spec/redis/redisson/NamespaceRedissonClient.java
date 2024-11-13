package cn.cjpw.infra.spec.redis.redisson;

import cn.hutool.core.util.StrUtil;
import org.redisson.api.*;
import org.redisson.api.LockOptions.BackOff;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonCodec;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * RedissonClient 包装类，增加key的前缀
 *
 * @author chenjun
 * @since 2020/3/18 14:13
 */
public class NamespaceRedissonClient implements RedissonClient {

    //    private final String dataNamespace;
    private final String lockNamespace;
    private RedissonClient redissonClient;

    //    public NamespaceRedissonClient(RedissonClient redissonClient, String dataNamespace, String lockNamespace) {
    //        this.redissonClient = redissonClient;
    //        this.dataNamespace = checkAndConvertNamespace(dataNamespace);
    //        this.lockNamespace = checkAndConvertNamespace(lockNamespace);
    //    }

    public NamespaceRedissonClient(RedissonClient redissonClient, String lockNamespace) {
        this.redissonClient = redissonClient;
        this.lockNamespace = checkAndConvertNamespace(lockNamespace);
    }

    private String checkAndConvertNamespace(String namespace) {
        String rt = namespace;
        if (StrUtil.isNotBlank(rt)) {
            if (rt.lastIndexOf(":") != rt.length() - 1) {
                rt += ":";
            }
        } else {
            rt = "";
        }
        return rt;
    }

    private String toRealKey(String key) {
        //        return this.dataNamespace + key;
        return key;
    }

    private String toLockRealKey(String key) {
        return this.lockNamespace + key;
    }

    @Override
    public <V, L> RTimeSeries<V, L> getTimeSeries(String s) {
        return redissonClient.getTimeSeries(toRealKey(s));
    }

    @Override
    public <V, L> RTimeSeries<V, L> getTimeSeries(String s, Codec codec) {
        return redissonClient.getTimeSeries(toRealKey(s), codec);
    }

    @Override
    public <K, V> RStream<K, V> getStream(String name) {
        return redissonClient.getStream(toRealKey(name));
    }

    @Override
    public <K, V> RStream<K, V> getStream(String name, Codec codec) {
        return redissonClient.getStream(toRealKey(name), codec);
    }

    @Override
    public RRateLimiter getRateLimiter(String name) {
        return redissonClient.getRateLimiter(toRealKey(name));
    }

    @Override
    public RBinaryStream getBinaryStream(String name) {
        return redissonClient.getBinaryStream(toRealKey(name));
    }

    @Override
    public <V> RGeo<V> getGeo(String name) {
        return redissonClient.getGeo(toRealKey(name));
    }

    @Override
    public <V> RGeo<V> getGeo(String name, Codec codec) {
        return redissonClient.getGeo(toRealKey(name), codec);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String name) {
        return redissonClient.getSetCache(toRealKey(name));
    }

    @Override
    public <V> RSetCache<V> getSetCache(String name, Codec codec) {
        return redissonClient.getSetCache(toRealKey(name), codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec) {
        return redissonClient.getMapCache(toRealKey(name), codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec, MapOptions<K, V> options) {
        return redissonClient.getMapCache(toRealKey(name), codec, options);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name) {
        return redissonClient.getMapCache(toRealKey(name));
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, MapOptions<K, V> options) {
        return redissonClient.getMapCache(toRealKey(name), options);
    }

    @Override
    public <V> RBucket<V> getBucket(String name) {
        return redissonClient.getBucket(toRealKey(name));
    }

    @Override
    public <V> RBucket<V> getBucket(String name, Codec codec) {
        return redissonClient.getBucket(toRealKey(name), codec);
    }

    @Override
    public RBuckets getBuckets() {
        return redissonClient.getBuckets();
    }

    @Override
    public RBuckets getBuckets(Codec codec) {
        return redissonClient.getBuckets(codec);
    }

    @Override
    public <V> RJsonBucket<V> getJsonBucket(String s, JsonCodec<V> jsonCodec) {
        return null;
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String name) {
        return redissonClient.getHyperLogLog(toRealKey(name));
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String name, Codec codec) {
        return redissonClient.getHyperLogLog(toRealKey(name), codec);
    }

    @Override
    public <V> RList<V> getList(String name) {
        return redissonClient.getList(toRealKey(name));
    }

    @Override
    public <V> RList<V> getList(String name, Codec codec) {
        return redissonClient.getList(toRealKey(name), codec);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String name) {
        return redissonClient.getListMultimap(toRealKey(name));
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String name, Codec codec) {
        return redissonClient.getListMultimap(toRealKey(name), codec);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name) {
        return redissonClient.getListMultimapCache(toRealKey(name));
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name, Codec codec) {
        return redissonClient.getListMultimapCache(toRealKey(name), codec);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, LocalCachedMapOptions<K, V> options) {
        return redissonClient.getLocalCachedMap(toRealKey(name), options);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, Codec codec, LocalCachedMapOptions<K, V> options) {
        return redissonClient.getLocalCachedMap(toRealKey(name), codec, options);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name) {
        return redissonClient.getMap(toRealKey(name));
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, MapOptions<K, V> options) {
        return redissonClient.getMap(toRealKey(name), options);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, Codec codec) {
        return redissonClient.getMap(toRealKey(name), codec);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, Codec codec, MapOptions<K, V> options) {
        return redissonClient.getMap(toRealKey(name), codec, options);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name) {
        return redissonClient.getSetMultimap(toRealKey(name));
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name, Codec codec) {
        return redissonClient.getSetMultimap(toRealKey(name), codec);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name) {
        return redissonClient.getSetMultimapCache(toRealKey(name));
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name, Codec codec) {
        return redissonClient.getSetMultimapCache(toRealKey(name), codec);
    }

    @Override
    public RSemaphore getSemaphore(String name) {
        return redissonClient.getSemaphore(toRealKey(name));
    }

    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String name) {
        return redissonClient.getPermitExpirableSemaphore(toRealKey(name));
    }

    @Override
    public RLock getLock(String name) {
        return redissonClient.getLock(toLockRealKey(name));
    }

    @Override
    public RLock getSpinLock(String name) {
        return redissonClient.getSpinLock(toLockRealKey(name));
    }

    @Override
    public RLock getSpinLock(String name, BackOff backOff) {
        return redissonClient.getSpinLock(toLockRealKey(name), backOff);
    }

    @Override
    public RLock getMultiLock(RLock... locks) {
        return redissonClient.getMultiLock(locks);
    }

    @Override
    public RLock getRedLock(RLock... locks) {
        return redissonClient.getRedLock(locks);
    }

    @Override
    public RLock getFairLock(String name) {
        return redissonClient.getFairLock(toLockRealKey(name));
    }

    @Override
    public RReadWriteLock getReadWriteLock(String name) {
        return redissonClient.getReadWriteLock(toLockRealKey(name));
    }

    @Override
    public <V> RSet<V> getSet(String name) {
        return redissonClient.getSet(toRealKey(name));
    }

    @Override
    public <V> RSet<V> getSet(String name, Codec codec) {
        return redissonClient.getSet(toRealKey(name), codec);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String name) {
        return redissonClient.getSortedSet(toRealKey(name));
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String name, Codec codec) {
        return redissonClient.getSortedSet(toRealKey(name), codec);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name) {
        return redissonClient.getScoredSortedSet(toRealKey(name));
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name, Codec codec) {
        return redissonClient.getScoredSortedSet(toRealKey(name), codec);
    }

    @Override
    public RLexSortedSet getLexSortedSet(String name) {
        return redissonClient.getLexSortedSet(toRealKey(name));
    }

    @Override
    public RShardedTopic getShardedTopic(String s) {
        return null;
    }

    @Override
    public RShardedTopic getShardedTopic(String s, Codec codec) {
        return null;
    }

    @Override
    public RTopic getTopic(String name) {
        return redissonClient.getTopic(name);
    }

    @Override
    public RTopic getTopic(String name, Codec codec) {
        return redissonClient.getTopic(name, codec);
    }

    @Override
    public RReliableTopic getReliableTopic(String name) {
        return redissonClient.getReliableTopic(name);
    }

    @Override
    public RReliableTopic getReliableTopic(String name, Codec codec) {
        return redissonClient.getReliableTopic(name, codec);
    }

    @Override
    public RPatternTopic getPatternTopic(String pattern) {
        return redissonClient.getPatternTopic(pattern);
    }

    @Override
    public RPatternTopic getPatternTopic(String pattern, Codec codec) {
        return redissonClient.getPatternTopic(pattern, codec);
    }

    @Override
    public <V> RQueue<V> getQueue(String name) {
        return redissonClient.getQueue(toRealKey(name));
    }

    @Override
    public <V> RTransferQueue<V> getTransferQueue(String name) {
        return redissonClient.getTransferQueue(toRealKey(name));
    }

    @Override
    public <V> RTransferQueue<V> getTransferQueue(String name, Codec codec) {
        return redissonClient.getTransferQueue(toRealKey(name), codec);
    }

    @Override
    public <V> RDelayedQueue<V> getDelayedQueue(RQueue<V> destinationQueue) {
        return redissonClient.getDelayedQueue(destinationQueue);
    }

    @Override
    public <V> RQueue<V> getQueue(String name, Codec codec) {
        return redissonClient.getQueue(toRealKey(name), codec);
    }

    @Override
    public <V> RRingBuffer<V> getRingBuffer(String name) {
        return redissonClient.getRingBuffer(toRealKey(name));
    }

    @Override
    public <V> RRingBuffer<V> getRingBuffer(String name, Codec codec) {
        return redissonClient.getRingBuffer(toRealKey(name), codec);
    }

    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String name) {
        return redissonClient.getPriorityQueue(toRealKey(name));
    }

    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String name, Codec codec) {
        return redissonClient.getPriorityQueue(toRealKey(name), codec);
    }

    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name) {
        return redissonClient.getPriorityBlockingQueue(toRealKey(name));
    }

    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name, Codec codec) {
        return redissonClient.getPriorityBlockingQueue(toRealKey(name), codec);
    }

    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name) {
        return redissonClient.getPriorityBlockingDeque(toRealKey(name));
    }

    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name, Codec codec) {
        return redissonClient.getPriorityBlockingDeque(toRealKey(name), codec);
    }

    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String name) {
        return redissonClient.getPriorityDeque(toRealKey(name));
    }

    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String name, Codec codec) {
        return redissonClient.getPriorityDeque(toRealKey(name), codec);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String name) {
        return redissonClient.getBlockingQueue(toRealKey(name));
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String name, Codec codec) {
        return redissonClient.getBlockingQueue(toRealKey(name), codec);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name) {
        return redissonClient.getBoundedBlockingQueue(toRealKey(name));
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name, Codec codec) {
        return redissonClient.getBoundedBlockingQueue(toRealKey(name), codec);
    }

    @Override
    public <V> RDeque<V> getDeque(String name) {
        return redissonClient.getDeque(toRealKey(name));
    }

    @Override
    public <V> RDeque<V> getDeque(String name, Codec codec) {
        return redissonClient.getDeque(toRealKey(name), codec);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String name) {
        return redissonClient.getBlockingDeque(toRealKey(name));
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String name, Codec codec) {
        return redissonClient.getBlockingDeque(toRealKey(name), codec);
    }

    @Override
    public RAtomicLong getAtomicLong(String name) {
        return redissonClient.getAtomicLong(toRealKey(name));
    }

    @Override
    public RAtomicDouble getAtomicDouble(String name) {
        return redissonClient.getAtomicDouble(toRealKey(name));
    }

    @Override
    public RLongAdder getLongAdder(String name) {
        return redissonClient.getLongAdder(toRealKey(name));
    }

    @Override
    public RDoubleAdder getDoubleAdder(String name) {
        return redissonClient.getDoubleAdder(toRealKey(name));
    }

    @Override
    public RCountDownLatch getCountDownLatch(String name) {
        return redissonClient.getCountDownLatch(toRealKey(name));
    }

    @Override
    public RBitSet getBitSet(String name) {
        return redissonClient.getBitSet(toRealKey(name));
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String name) {
        return redissonClient.getBloomFilter(toRealKey(name));
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String name, Codec codec) {
        return redissonClient.getBloomFilter(toRealKey(name), codec);
    }

    @Override
    public RIdGenerator getIdGenerator(String name) {
        return redissonClient.getIdGenerator(name);
    }

    @Override
    public RFunction getFunction() {
        return redissonClient.getFunction();
    }

    @Override
    public RFunction getFunction(Codec codec) {
        return redissonClient.getFunction(codec);
    }

    @Override
    public RScript getScript() {
        return redissonClient.getScript();
    }

    @Override
    public RScript getScript(Codec codec) {
        return redissonClient.getScript(codec);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name) {
        return redissonClient.getExecutorService(toRealKey(name));
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, ExecutorOptions options) {
        return redissonClient.getExecutorService(toRealKey(name), options);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, Codec codec) {
        return redissonClient.getExecutorService(toRealKey(name), codec);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, Codec codec, ExecutorOptions options) {
        return redissonClient.getExecutorService(toRealKey(name), codec, options);
    }

    @Override
    public RRemoteService getRemoteService() {
        return redissonClient.getRemoteService();
    }

    @Override
    public RRemoteService getRemoteService(Codec codec) {
        return redissonClient.getRemoteService(codec);
    }

    @Override
    public RRemoteService getRemoteService(String name) {
        return redissonClient.getRemoteService(toRealKey(name));
    }

    @Override
    public RRemoteService getRemoteService(String name, Codec codec) {
        return redissonClient.getRemoteService(toRealKey(name), codec);
    }

    @Override
    public RTransaction createTransaction(TransactionOptions options) {
        return redissonClient.createTransaction(options);
    }

    @Override
    public RBatch createBatch(BatchOptions options) {
        return redissonClient.createBatch(options);
    }

    @Override
    public RBatch createBatch() {
        return redissonClient.createBatch();
    }

    @Override
    public RKeys getKeys() {
        return redissonClient.getKeys();
    }

    @Override
    public RLiveObjectService getLiveObjectService() {
        return redissonClient.getLiveObjectService();
    }

    @Override
    public RedissonRxClient rxJava() {
        return redissonClient.rxJava();
    }

    @Override
    public RedissonReactiveClient reactive() {
        return redissonClient.reactive();
    }

    @Override
    public void shutdown() {
        redissonClient.shutdown();
    }

    @Override
    public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
        redissonClient.shutdown(quietPeriod, timeout, unit);
    }

    @Override
    public Config getConfig() {
        return redissonClient.getConfig();
    }

    @Override
    public <T extends BaseRedisNodes> T getRedisNodes(RedisNodes<T> nodes) {
        return redissonClient.getRedisNodes(nodes);
    }

    @Override
    public NodesGroup<Node> getNodesGroup() {
        return redissonClient.getNodesGroup();
    }

    @Override
    public ClusterNodesGroup getClusterNodesGroup() {
        return redissonClient.getClusterNodesGroup();
    }

    @Override
    public boolean isShutdown() {
        return redissonClient.isShutdown();
    }

    @Override
    public boolean isShuttingDown() {
        return redissonClient.isShuttingDown();
    }

    @Override
    public String getId() {
        return redissonClient.getId();
    }
}
