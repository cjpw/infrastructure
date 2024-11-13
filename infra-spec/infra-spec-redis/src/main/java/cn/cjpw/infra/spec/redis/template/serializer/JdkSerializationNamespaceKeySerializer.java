package cn.cjpw.infra.spec.redis.template.serializer;//package cn.cjpw.infra.spec.redis.template.serializer;
//
//import cn.hutool.core.util.StrUtil;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//
//import java.io.Serializable;
//
///**
// * @author jun.chen1
// * @since 2022/1/25 16:27
// **/
//public class JdkSerializationNamespaceKeySerializer extends PrefixRedisSerializer<Serializable> {
//
//    public JdkSerializationNamespaceKeySerializer(Serializable prefix) {
//        super(prefix, new JdkSerializationRedisSerializer());
//    }
//
//    /**
//     * 拼装前缀
//     * @param namespace
//     * @return
//     */
//    public Serializable checkAndConvertNamespace(Serializable namespace) {
//        Serializable rt = namespace;
//        if (StrUtil.isNotBlank(rt)) {
//            if (rt.lastIndexOf(":") != rt.length() - 1) {
//                rt += ":";
//            }
//        } else {
//            rt = "";
//        }
//        return rt;
//    }
//
//}
