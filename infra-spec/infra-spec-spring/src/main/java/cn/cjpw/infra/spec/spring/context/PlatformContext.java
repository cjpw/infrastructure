package cn.cjpw.infra.spec.spring.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 业务平台级的上下文注入工具
 *
 * @author cjpower
 * @since 2024/11/13 14:47
 **/
public enum PlatformContext {
    LANGUAGE("platform-locale"),
    ZONE("platform-zone"),
    TRAFFIC_GROUP("platform-chain-flag"),
    SHARED("platform-chain-type"),
    TIME_ZONE("platform-timezone"),
    TENANT_ID("platform-tenant-id"),
    GROUP_ID("platform-group-id"),
    OPERATE_ID("platform-oper-id"),
    PRODUCT("platform-product");

    private String key;

    PlatformContext(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    private static AtomicBoolean reqExist = new AtomicBoolean(false);

    static {
        try {
            if (Class.forName("javax.servlet.http.HttpServletRequest") != null
                    && Class.forName("org.springframework.web.context.request.RequestContextHolder") != null) {
                reqExist.set(true);
            }
        } catch (Exception e) {

        }
    }

    private static boolean isReqExist() {
        return reqExist.get();
    }

    private static final ThreadLocal<Opaque> context = new ThreadLocal<>();
    private static boolean dswAgentExist;
    private static final int kvSizeLimit = 100;

    static {
        try {
            Class.forName("platform.arch.dsw.agent.DswAgent", false, ClassLoader.getSystemClassLoader());
            dswAgentExist = true;
        } catch (ClassNotFoundException e) {
            dswAgentExist = false;
        }
    }

    /**
     * bug fix:
     * if controller request thread reuse, no one clear ThreadLocal context after a former request,
     * that will cause a dirty context
     * <p>
     * so, In scenarios of http controller thread, thread local context always get from header first
     *
     * @return
     */
    public String get() {
        String val = getValueLocally(this.key());
        return val;
    }

    /**
     * set with compatible process
     *
     * @param val
     */
    public void set(String val) {
        setValueLocally(val);
    }

    /**
     * remove with compatible process
     */
    public void remove() {
        if (context.get() != null) {
            context.get().remove(this.key());
        }
    }

    /**
     * a method set value for {@link PlatformContext}
     *
     * @param val
     */
    private void setValueLocally(String val) {
        if (val == null) {
            this.remove();
            return;
        }

        PlatformContext.setValueLocally(this.key(), val);
    }

    /**
     * get locally
     *
     * @param key
     * @return
     */
    static String getValueLocally(String key) {
        return context.get() == null ? null : (String) context.get().get(key);
    }

    /**
     * set locally
     *
     * @param key
     * @param value
     */
    static void setValueLocally(String key, String value) {
        if (value == null && context.get() != null) {
            context.get().remove(key);
            return;
        }

        if (value == null || value.length() > kvSizeLimit)
            return;

        if (context.get() != null) {
            context.get().add(key, value);
        } else {
            PlatformContext.Opaque data = new PlatformContext.Opaque();
            data.add(key, value);
            context.set(data);
        }
    }


    /**
     * 专用场景：RocketMQ Consumer 从消息中获取透传属性
     * <p>
     * used for mq/queue batch consume
     * clear before obtain current thread context
     * used for mq/queue driven model, consume thread is immortal but thread context has to change as message/data pass in.
     *
     * @return
     */
    public static PlatformContext.Opaque resetAndCapture() {
        PlatformContext.clear();
        return capture();
    }

    /**
     * obtain current thread context
     * --update by yi.chen2 at 2022-09-28 15:31--↓
     * when header value is present multiple with controller and dswRunnable/dswCallable
     * Context.get need get value from header or cookie every time so add a condition !isReqExist
     *
     * @return
     */
    public static PlatformContext.Opaque capture() {
        for (PlatformContext e : PlatformContext.values()) {
            e.get();
        }
        return context.get().clone();
    }


    /**
     * restore data to current thread local context
     *
     * @param data
     */
    public static void restore(PlatformContext.Opaque data) {
        if (data == null) {
            clear();
            return;
        }

        PlatformContext.Opaque copy = data.clone();
        context.set(copy);
    }

    /**
     * set custom transmittable attachments
     * Attention:
     * key value size limit < 200
     *
     * @param key
     * @param val
     */
    public static void customSet(String key, String val) {
        if (key == null || key.length() > kvSizeLimit)
            return;

        setValueLocally(key, val);
    }

    /**
     * get custom transmittable attachments
     *
     * @param key
     * @return
     */
    public static String customGet(String key) {
        return context.get() == null ? null : (String) context.get().get(key);
    }

    /**
     * clear with compatible process
     */
    public static void clear() {
        if (context.get() != null) {
            context.remove();
        }
    }


    /**
     * jvm without dsw agent instrumentation
     *
     * @return
     */
    public static boolean dswAgentAbsent() {
        return !dswAgentExist;
    }

    @Deprecated
    public static void callOnContext(PlatformContext.Opaque data, Callable callable) throws Exception {
        PlatformContext.Opaque curContext = replaceContext(data);
        try {
            callable.call();
        } finally {
            replaceContext(curContext);
        }
    }

    @Deprecated
    static PlatformContext.Opaque replaceContext(PlatformContext.Opaque newData) {
        PlatformContext.Opaque backupContext = context.get();
        if (newData == null) {
            context.remove();
        } else {
            context.set(newData);
        }
        return backupContext;
    }

    public static Opaque create(Map<String, String> map) {
        PlatformContext.Opaque opaque = new PlatformContext.Opaque();
        if (map != null) {
            for (Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator(); itr.hasNext(); ) {
                Map.Entry<String, String> entry = itr.next();
                opaque.add(entry.getKey(), entry.getValue());
            }
        }
        return opaque;
    }

    public static class Opaque implements Cloneable {
        private final static int propCountThreshold = 30;
        private final Map<String, Object> data = new HashMap<>();

        private Opaque() {
        }

        public void add(String key, Object val) {
            if (data.size() < propCountThreshold) {
                data.put(key, val);
            }
        }

        public Object get(String key) {
            return data.get(key);
        }

        public Object getOrDefault(String key, Object defVal) {
            return data.get(key) == null ? defVal : data.get(key);
        }

        public <T> T get(String key, Class<T> clazz) {
            return data.get(key) == null ? null : clazz.cast(data.get(key));
        }

        public <T> T get(String key, T defVal, Class<T> clazz) {
            return data.get(key) == null ? defVal : clazz.cast(data.get(key));
        }

        public Object remove(String key) {
            return data.remove(key);
        }

        public Set<String> keySet() {
            return data.keySet();
        }

        @Override
        public Opaque clone() {
            Opaque copy = new Opaque();
            copy.data.putAll(data);
            return copy;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("PlatformContext.Opaque{");
            for (Iterator<Map.Entry<String, Object>> itr = data.entrySet().iterator(); itr.hasNext(); ) {
                Map.Entry<String, Object> entry = itr.next();
                builder.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("; ");
            }
            return builder.append("}").toString();
        }

    }
}
