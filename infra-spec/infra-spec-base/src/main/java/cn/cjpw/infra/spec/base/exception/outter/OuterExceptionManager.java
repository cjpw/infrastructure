package cn.cjpw.infra.spec.base.exception.outter;

import cn.cjpw.infra.spec.base.exception.enums.IError;
import cn.hutool.core.util.ObjectUtil;
import cn.cjpw.infra.spec.base.exception.ExceptionTypeMatcher;

import java.util.LinkedList;

/**
 * 针对外部异常的针对性识别包装适配器,基于有序队列
 *
 * @author jun.chen1
 * @since 2021/8/24 15:14
 **/
public class OuterExceptionManager {
    private LinkedList<ExceptionTypeMatcher> innerList = new LinkedList<>();

    private static final OuterExceptionManager ins = new OuterExceptionManager();

    public static OuterExceptionManager instance() {
        return ins;
    }

    /**
     * 添加适配器到队尾
     *
     * @param matcher
     */
    public void register(ExceptionTypeMatcher matcher) {
        innerList.addLast(matcher);
    }

    /**
     * 添加适配器到队尾
     *
     * @param matcher
     */
    public void registerToTop(ExceptionTypeMatcher matcher) {
        innerList.addFirst(matcher);
    }

    /**
     * 插入到某个识别器前面,没找到就插入到末尾
     *
     * @param matcher
     */
    public void registerBefore(ExceptionTypeMatcher matcher, ExceptionTypeMatcher tag) {
        int i = innerList.indexOf(tag);
        if (i >= 0) {
            innerList.add(i, matcher);
        } else {
            innerList.addLast(matcher);
        }
    }

    /**
     * 插入到某个识别器后面,没找到就插入到末尾
     *
     * @param matcher
     */
    public void registerAfter(ExceptionTypeMatcher matcher, ExceptionTypeMatcher tag) {
        int i = innerList.indexOf(tag);
        if (i >= 0) {
            innerList.add(i + 1, matcher);
        } else {
            innerList.addLast(matcher);
        }
    }

    /**
     * 将plugin已经预置的异常映射配置进来
     */
    public void registerDefault() {
        instance().register(ExceptionTypeMatcher.OOR_DATA_MATCHER);
        instance().register(ExceptionTypeMatcher.JDBC_DB_MATCHER);
        instance().register(ExceptionTypeMatcher.MYBATIS_DB_MATCHER);
        instance().register(ExceptionTypeMatcher.SPRING_DB_MATCHER);
        instance().register(ExceptionTypeMatcher.NPE_DATA_MATCHER);
    }

    /**
     * 移除一个配置
     *
     * @param matcher
     * @return
     */
    public boolean remove(ExceptionTypeMatcher matcher) {
        return innerList.remove(matcher);
    }

    /**
     * 清除已配置
     */
    public void clear() {
        this.innerList.clear();
    }

    /**
     * 根据已配置的转化器转化成通用内部异常码
     *
     * @param e
     * @return
     */
    public IError convert(Throwable e) {
        return convert(e, null);
    }

    public IError convert(Throwable e, IError defaultErrorResult) {
        defaultErrorResult = ObjectUtil.defaultIfNull(defaultErrorResult, IError.SYS_ERROR);
        IError iError = this.innerList.stream()
                .filter(item -> item.test(e))
                .map(item -> item.getIErrorResult(e))
                .findFirst().orElse(defaultErrorResult);
        return iError;
    }

}
