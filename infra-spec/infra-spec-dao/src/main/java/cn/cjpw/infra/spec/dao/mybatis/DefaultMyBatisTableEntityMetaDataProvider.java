package cn.cjpw.infra.spec.dao.mybatis;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于一些约定俗称项，获取的字段配置
 *
 * @author jun.chen1
 * @since 2022/10/13 14:47
 **/
public class DefaultMyBatisTableEntityMetaDataProvider implements TableEntityMetaData {

    @Override
    public Class getEntityType(ProviderContext context) {
        Class mClass = context.getMapperType();
        return (Class) ((ParameterizedType) (mClass.getGenericInterfaces()[0])).getActualTypeArguments()[0];
    }

    @Override
    public String getIdField(Class clz) {
        return "id";
    }

    @Override
    public String getIdColumn(Class clz) {
        return "id";
    }

    @Override
    public String tableName(Class clz) {
        String entityName = clz.getSimpleName();
        return StrUtil.toUnderlineCase(entityName);
    }

    @Override
    public List<String> getAllColumns(Class clz) {
        List<String> allFields = getAllFields(clz);
        List<String> collect = allFields.stream().map(item -> StrUtil.toUnderlineCase(item)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> getAllFields(Class clz) {
        Field[] fields = clz.getDeclaredFields();
        List<String> entityFields = new ArrayList<>(fields.length);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (name.equals(getIdField(clz))) {
                entityFields.add(0, name);
            } else {
                entityFields.add(name);
            }
        }
        return entityFields;
    }
}
