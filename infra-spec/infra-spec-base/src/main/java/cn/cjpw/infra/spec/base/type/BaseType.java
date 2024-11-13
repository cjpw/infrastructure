package cn.cjpw.infra.spec.base.type;

/**
 * java 中的基础类型
 * @author jun.chen1
 * @since 2024/9/13 14:19
 **/
public class BaseType {

    /**
     * 是否基础类型中对应的包装类
     * @param z
     * @return
     */
    public static boolean isWrapperType(Class<?> z){
        return Boolean.class==z||Byte.class==z||Character.class==z||Double.class==z||Float.class==z||
                Integer.class==z||Long.class==z||Short.class==z||Void.class==z;
    }

    /**
     * 判断是否不是基础类或者包装类
     * @param z
     * @return
     */
    public static boolean isBaseType(Class<?>z){
        return z.isPrimitive()||isWrapperType(z);
    }

}
