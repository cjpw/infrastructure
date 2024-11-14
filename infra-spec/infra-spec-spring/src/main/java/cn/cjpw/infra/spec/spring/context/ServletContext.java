package cn.cjpw.infra.spec.spring.context;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * http请求上下文工具
 *
 * @author jun.chen1
 * @since 2024/11/13 15:23
 **/
public class ServletContext {

    /**
     * 获取当前的request，注意是在当前线程 web支持
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取当前的response，注意是在当前线程 web支持
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 从http请求中获取协议字段内容
     *
     * @param key
     * @return
     */
    public static String getValueByServlet(String key) {
        HttpServletRequest request = getRequest();


        String value = Optional.ofNullable(request.getHeader(key))
                .filter(StrUtil::isNotBlank)
                .or(() -> Optional.ofNullable(request.getCookies())
                        .flatMap(cookies -> Arrays.stream(cookies).filter(item -> item.getName().equals(key)).findAny())
                        .map(Cookie::getValue))
                .orElse(null);

        return value;
    }
}
