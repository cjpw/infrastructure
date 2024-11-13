package cn.cjpw.infra.spec.dao.mybatis;


import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.List;

/**
 * 基础的表与实体元数据抽象
 *
 * @author jun.chen1
 * @since 2022/10/13 14:24
 **/
public interface TableEntityMetaData {

    /**
     * 获取表实体信息
     *
     * @param context
     * @return
     */
    Class getEntityType(ProviderContext context);

    /**
     * 获取实体主键字段名称
     *
     * @param clz
     * @return
     */
    String getIdField(Class clz);

    /**
     * 获取表主键字段名称
     *
     * @param clz
     * @return
     */
    String getIdColumn(Class clz);

    /**
     * 获取表名
     *
     * @param clz
     * @return
     */
    String tableName(Class clz);

    /**
     * 获取所有表字段名
     *
     * @param clz
     * @return
     */
    List<String> getAllColumns(Class clz);

    /**
     * 获取所有实体字段名
     *
     * @param clz
     * @return
     */
    List<String> getAllFields(Class clz);

}
