package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.constant.ExceptionConstant;

/**
 * @author jun.chen1
 * @since 2020/8/24 13:36
 **/
public class ModuleExceptionRegistry {

    private static ModuleExceptionRegistry instance;

    /**
     * 应用短码
     */
    private String moduleId = ExceptionConstant.SYS_ID_BASE;

    /**
     * 应用名称获取的配置key模板,String.format()可转
     */
    private String moduleNameTemplate;

    /**
     * 系统级别（jvm）异常时，直接报警的key
     */
    private String sysErrorAlarmKey;

    static {
        instance = new ModuleExceptionRegistry();
    }

    private ModuleExceptionRegistry() {
    }

    public static ModuleExceptionRegistry instance() {
        return instance;
    }


    /**
     * 注册应用短码
     *
     * @param moduleCode
     */
    public void registerModuleCode(String moduleCode) {
        moduleId = moduleCode;
    }

    /**
     * 注册应用名称获取的配置key模板,String.format()可转
     *
     * @param template
     */
    public void registerModuleNameTemplate(String template) {
        this.moduleNameTemplate = template;
    }

    /**
     * 注册系统级别（jvm）异常时，直接报警的key
     *
     * @param sysErrorAlarmKey
     */
    public void registerSysErrorAlarmKey(String sysErrorAlarmKey) {
        this.sysErrorAlarmKey = sysErrorAlarmKey;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getModuleNameKeyTemplate() {
        return this.moduleNameTemplate;
    }

    public String getDefaultSysErrorAlarmKey() {
        return sysErrorAlarmKey;
    }


}
