package cn.cjpw.infra.spec.base.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志打印扩展支持
 *
 * @author jun.chen1
 * @since 2023/11/13 15:50
 **/
public class LogExtSupporter {

    private static final Logger extBackLog;

    private static final Logger demotionLog;

    private static final LogExtSupporter instance;

    static {
        instance = new LogExtSupporter();
        extBackLog = new CustomAppenderNameLogger(LoggerFactory.getLogger("EXT_BACK_LOG"));
        demotionLog = new CustomAppenderNameLogger(LoggerFactory.getLogger("DEMOTION_LOG"));
    }

    public static LogExtSupporter instance() {
        return instance;
    }

    /**
     * 获取扩展数据日志，用于内容体较大且频繁的日志打印，避免过度污染常规业务日志
     *
     * @return
     */
    public Logger getExtBackLog() {
        return extBackLog;
    }

    /**
     * 降级日志，用于在所有依赖中间件的持久化失效的情况下，对需要的数据按日志方式进行暂存
     *
     * @return
     */
    public Logger getDemotionLog() {
        return demotionLog;
    }

}
