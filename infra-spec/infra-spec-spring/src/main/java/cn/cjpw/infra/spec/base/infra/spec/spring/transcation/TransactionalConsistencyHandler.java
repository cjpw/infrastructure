package cn.cjpw.infra.spec.base.infra.spec.spring.transcation;

import cn.cjpw.infra.spec.base.alarm.Alarm;
import cn.cjpw.infra.spec.base.exception.util.ExceptionCatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 本地事务最终一致性处理器 用于执行需要异步处理，又需要保证主流程事务执行成功
 *
 * @author jun.chen1
 * @since 2020/5/27 13:09
 **/
public class TransactionalConsistencyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalConsistencyHandler.class);

    private ApplicationEventPublisher applicationEventPublisher;

    private ExecutorService transactionalConsistencyThreadPool;

    private Alarm alarm;

    public TransactionalConsistencyHandler(ApplicationEventPublisher applicationEventPublisher, Alarm alarm) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.transactionalConsistencyThreadPool = new AlarmThreadPool(alarm);
        this.alarm = alarm;
    }

    public TransactionalConsistencyHandler(ApplicationEventPublisher applicationEventPublisher, Alarm alarm,
        ExecutorService executorService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.transactionalConsistencyThreadPool = executorService;
        this.alarm = alarm;
    }


    /**
     * 将需要等事务执行成功后的执行的方法注册进来
     *
     * @param tag            执行场景标签，方便跟踪问题
     * @param consumer       待执行逻辑
     * @param predicate      自定义判断规则，是否执行consumer，判断触发事务通知后，该事务是否是你需要执行对应逻辑的事务
     * @param consumerParam  待执行逻辑参数
     * @param predicateParam 判断条件参数
     * @param async          是否异步处理
     */
    public <T, R> void publishEvent(String tag, Predicate<T> predicate, T predicateParam, Consumer<R> consumer, R consumerParam,
        boolean async) {
        PostPositivelyEvent<T, R> payloadApplicationEvent = new PostPositivelyEvent<>(tag, predicate, predicateParam, consumer,
            consumerParam, async);
        applicationEventPublisher.publishEvent(payloadApplicationEvent);
    }

    /**
     * 将需要等事务执行成功后的执行的方法注册进来，异步的
     *
     * @param runnable 需要执行的逻辑
     */
    public void publishEvent(Runnable runnable, String tag) {
        PostPositivelyEvent payloadApplicationEvent = new PostPositivelyEvent<>(tag, t -> true, null, t -> runnable.run(), null, true);
        applicationEventPublisher.publishEvent(payloadApplicationEvent);
    }

    /**
     * 事务成功后统一调用入口
     *
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = PostPositivelyEvent.class)
    public void subscribeEvent(PostPositivelyEvent event) {
        boolean next = event.predicate.test(event.predicateParam);
        if (next) {
            if (event.async) {
                AlarmTagRunnable wrappedRunnable = new AlarmTagRunnable(() -> safeExec(event), event.tag,
                    String.valueOf(event.getSource()));
                transactionalConsistencyThreadPool.submit(wrappedRunnable);
            } else {
                safeExec(event);
            }
        }
    }

    private void safeExec(PostPositivelyEvent event) {
        try {
            event.consumer.accept(event.consumerParam);
        } catch (Exception e) {
            String tmpTip = ExceptionCatcher.errorTraceCodeGen();
            alarm.alarm(event.tag, event.getSource() + "", "事务最终一致接口失败,尽快处理,tmpTid=" + tmpTip);
            LOGGER.error("事务最终一致性接口调用失败,sourceTid={},param={}", event.getSource(), event.consumerParam.toString(), e);
        }
    }

    /**
     * 内部用于发布通知的对象，接受相关执行逻辑和判断是否执行逻辑的参数
     *
     * @param <T>
     * @param <R>
     */
    private static class PostPositivelyEvent<T, R> extends ApplicationEvent {

        private String tag;
        private T predicateParam;
        private R consumerParam;
        private Predicate<T> predicate;
        private Consumer<R> consumer;
        private boolean async;

        /**
         * @param predicate
         * @param predicateParam
         * @param consumer
         * @param consumerParam
         */
        public PostPositivelyEvent(String tag, Predicate<T> predicate, T predicateParam, Consumer<R> consumer, R consumerParam,
            boolean async) {
            super(ExceptionCatcher.errorTraceCodeGen());
            this.tag = tag;
            this.consumer = consumer;
            this.predicateParam = predicateParam;
            this.consumerParam = consumerParam;
            this.predicate = predicate;
            this.async = async;
        }
    }

}
