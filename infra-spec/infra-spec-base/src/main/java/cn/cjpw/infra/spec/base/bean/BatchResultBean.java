package cn.cjpw.infra.spec.base.bean;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 批量执行结果类
 *
 * @author jun.chen1
 * @since 2020/8/17 16:25
 **/
public class BatchResultBean<T, R> {

    /**
     * 成功信息
     */
    private Collection<T> successList;

    /**
     * 失败信息
     * R 返回对象
     * String 错误描述
     */
    private Map<R, String> failureList;

    /**
     * 忽略掉的信息
     */
    private Collection<T> ignoreList;

    public BatchResultBean() {
        this.successList = new LinkedBlockingQueue<>();
        this.ignoreList = new LinkedBlockingQueue<>();
        this.failureList = new ConcurrentHashMap<>();
    }

    /**
     * 参与处理总数据量
     *
     * @return
     */
    public int total() {
        return successList.size() + failureList.size() + ignoreList.size();
    }

    /**
     * 添加一条执行成功的记录
     *
     * @param t
     * @return 当前成功数量
     */
    public int addSuccess(T t) {
        this.successList.add(t);
        return this.successList.size();
    }

    /**
     * 添加一条可以忽略的记录
     *
     * @param t
     * @return 当前忽略数量
     */
    public int addIgnore(T t) {
        this.ignoreList.add(t);
        return this.ignoreList.size();
    }

    /**
     * 添加一条执行失败的记录
     *
     * @return 当前失败数量
     */
    public int addFailure(R r, String desc) {
        this.failureList.put(r, desc);
        return this.failureList.size();
    }


    /**
     * 获取 成功信息
     *
     * @return successList 成功信息
     */
    public Collection<T> getSuccessList() {
        return this.successList;
    }

    /**
     * 获取 失败信息      R 返回对象      String 错误描述
     *
     * @return failureList 失败信息      R 返回对象      String 错误描述
     */
    public Map<R, String> getFailureList() {
        return this.failureList;
    }

    /**
     * 获取 忽略掉的信息
     *
     * @return ignoreList 忽略掉的信息
     */
    public Collection<T> getIgnoreList() {
        return this.ignoreList;
    }

}
