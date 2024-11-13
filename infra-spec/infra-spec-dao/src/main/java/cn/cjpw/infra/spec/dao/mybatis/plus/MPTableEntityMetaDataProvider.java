package cn.cjpw.infra.spec.dao.mybatis.plus;

import cn.cjpw.infra.spec.dao.mybatis.TableEntityMetaData;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jun.chen1
 * @since 2022/10/13 14:47
 **/
public class MPTableEntityMetaDataProvider implements TableEntityMetaData {

    @Override
    public Class getEntityType(ProviderContext context) {
        Class mClass = context.getMapperType();
        if (Mapper.class.isAssignableFrom(mClass)) {
            return ClassUtil.getTypeArgument(mClass);
        } else {
            return (Class) ((ParameterizedType) (mClass.getGenericInterfaces()[0])).getActualTypeArguments()[0];
        }
    }

    @Override
    public String getIdField(Class clz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clz);
        return tableInfo.getKeyProperty();
    }

    @Override
    public String getIdColumn(Class clz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clz);
        return tableInfo.getKeyColumn();
    }

    @Override
    public String tableName(Class clz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clz);
        return tableInfo.getTableName();
    }

    @Override
    public List<String> getAllColumns(Class clz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clz);
        List<String> collect = tableInfo.getFieldList().stream().map(TableFieldInfo::getColumn).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> getAllFields(Class clz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clz);
        List<String> collect = tableInfo.getFieldList().stream().map(TableFieldInfo::getProperty).collect(Collectors.toList());
        return collect;
    }
}
