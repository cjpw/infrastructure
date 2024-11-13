package cn.cjpw.infra.spec.base.mock;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 三方接口挡板，用于三方环境不支持时，模拟调用返回
 *
 * @author jun.chen1
 * @since 2022/12/5 15:48
 **/
@Aspect
@Slf4j
public class RemoteApiMockAspect {

    private RemoteApiMockConfiguration remoteApiMockConfiguration;

    public RemoteApiMockAspect(RemoteApiMockConfiguration remoteApiMockConfiguration) {
        this.remoteApiMockConfiguration = remoteApiMockConfiguration;
    }

    @Pointcut("@annotation(com.dmall.fit.yygl.caiwu.spec.mock.RemoteApiMock)")
    public void pointCut() {
    }

    @Around("pointCut()&& @annotation(mockAnnotation)")
    public Object mockService(ProceedingJoinPoint joinPoint, RemoteApiMock mockAnnotation) throws Throwable {
        String s = mockAnnotation.mockName();
        if (StrUtil.isBlank(s)) {
            s = joinPoint.getTarget().getClass().getName() + "#" + mockAnnotation.mockMethod();
        }
        if (!remoteApiMockConfiguration.getOpenStatus(s)) {
            return joinPoint.proceed();
        }
        Object target = joinPoint.getTarget();
        Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        String mockMethod = mockAnnotation.mockMethod();
        if (StrUtil.isBlank(mockMethod)) {
            return joinPoint.proceed();
        }

        try {
            Method method = ReflectionUtils.findMethod(target.getClass(), mockMethod, targetMethod.getParameterTypes());
            if (Objects.isNull(method)) {
                log.warn("找不到对应的mock方法,class={},method={},paramTypes={}", target.getClass(), mockMethod,
                        targetMethod.getParameterTypes());
                return joinPoint.proceed();
            }
            method.setAccessible(true);
            Object result = method.invoke(target, joinPoint.getArgs());
            if (mockAnnotation.costTime() > 0) {
                Thread.sleep(mockAnnotation.costTime());
            }
            log.debug("执行mock方法,method={}", mockMethod);
            return result;
        } catch (Exception e) {
            log.error("执行mock方法异常", e);
            return e;
        }
    }

}
