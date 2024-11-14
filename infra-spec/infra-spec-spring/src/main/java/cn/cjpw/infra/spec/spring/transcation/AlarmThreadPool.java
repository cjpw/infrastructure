package cn.cjpw.infra.spec.spring.transcation;

import cn.cjpw.infra.spec.base.alarm.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * 一个带监控和告警功能的线程池
 * @author jun.chen1
 * @since 2022/10/25 10:23
 **/
public class AlarmThreadPool extends ThreadPoolExecutor {

    private static final Logger log = LoggerFactory.getLogger(AlarmThreadPool.class);

    private final static int WARNING_QUEUE_SIZE = 1000;
    private final static int MAX_POOL_SIZE = 10;
    private final static int CORE_ACTIVE_SIZE = 10;
    private final static int QUEUE_SIZE = 5000;
    private final static int KEEP_ALIVE_TIME = 0;

    /**
     * 告警类
     */
    private Alarm alarm;
    /**
     * 等待任务积压告警阈值
     */
    private int warningQueueSize;

    private String poolName = "alarmPoolDefault";


    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public AlarmThreadPool(Alarm alarm) {
        super(CORE_ACTIVE_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(QUEUE_SIZE));
        this.setRejectedExecutionHandler(new AlarmPolicy(new CallerRunsPolicy()));
        this.alarm=alarm;
        this.warningQueueSize = WARNING_QUEUE_SIZE;
    }

    public AlarmThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                           BlockingQueue<Runnable> workQueue, Alarm alarm, int warningQueueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.setRejectedExecutionHandler(new AlarmPolicy(new CallerRunsPolicy()));
        this.alarm = alarm;
        this.warningQueueSize = warningQueueSize;
    }

    @Override
    public Future<?> submit(Runnable task) {
        alarmOnExiguous(task);
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        alarmOnExiguous(task);
        return super.submit(task, result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        alarmOnExiguous(task);
        return super.submit(task);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    private static class AlarmPolicy implements RejectedExecutionHandler {

        private RejectedExecutionHandler inner;

        public AlarmPolicy(RejectedExecutionHandler inner) {
            this.inner = inner;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            alarmOnRejected(r, (AlarmThreadPool) executor);
            this.inner.rejectedExecution(r, executor);
        }

        private void alarmOnRejected(Runnable r, AlarmThreadPool executor) {
            int active = executor.getActiveCount();
            int queueCount = executor.getQueue().size();
            StringBuilder sb = new StringBuilder();
            sb.append("pool rejected wait:[").append(queueCount)
                    .append("],active:[").append(active).append("],maxPool:[")
                    .append(executor.getMaximumPoolSize()).append("]");
            String content = sb.toString();
            log.warn(content);
            alarmByPool(r, executor, content);
        }
    }

    private static void alarmByPool(Object r, AlarmThreadPool pool, String content) {
        AlarmTag origin = getOrigin(r);
        if (origin != null) {
            pool.alarm.alarm(origin.getRunnerTag(), origin.getSourceTid(), content);
        } else {
            pool.alarm.alarm(pool.getPoolName(), null, content);
        }
    }

    private void alarmOnExiguous(Object runnable) {
        int active = getActiveCount();
        int queueCount = getQueue().size();
        log.trace("pool exiguous wait:[" + queueCount + "],active:[" + active + "],maxPool:["
                + getMaximumPoolSize() + "]");
        //判断线程池队列  报警
        if (queueCount > warningQueueSize) {
            StringBuilder sb = new StringBuilder();
            sb.append("pool exiguous queueSize:[").append(queueCount)
                    .append("],activeCount:[").append(active).append("],maximumPoolSize:[")
                    .append(getMaximumPoolSize());
            String content = sb.toString();
            log.warn(content);
            alarmByPool(runnable, this, content);
        }
    }

    /**
     * 从代理或者继承中获取被原始目标runnable
     *
     * @param runnable
     * @return
     */
    private static AlarmTag getOrigin(Object runnable) {
        if (runnable instanceof AlarmTag) {
            return (AlarmTag) runnable;
        }
        //从包装对象里面拿AlarmTag
        AlarmTag alarmTag = Stream.of(runnable.getClass().getDeclaredFields())
                .filter(item -> AlarmTag.class.isAssignableFrom(item.getType()))
                .map(item -> {
                    try {
                        item.setAccessible(true);
                        return (AlarmTag) item.get(runnable);
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                }).findAny().orElse(null);
        return alarmTag;
    }

}
