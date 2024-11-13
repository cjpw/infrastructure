package cn.cjpw.infra.spec.dao.mybatis.plus;

import cn.cjpw.infra.spec.dao.mybatis.CommonSqlBuildUtil;
import cn.cjpw.infra.spec.dao.mybatis.DaoFactory;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.util.Collection;

/**
 * mybatis plus mapper功能扩展
 * <p>
 * 当前设计方案不依赖mp的具体架构版本，只使用少量通用功能
 *
 * @author jun.chen1
 * @since 2022/10/13 13:45
 **/
public interface ExtendMapper<T> extends BaseMapper<T> {

    /**
     * 物理删除
     */
    @DeleteProvider(value = InnerSqlProvider.class, method = "deleteByWrapper")
    int physicalDeleteByWrapper(@Param(Constants.WRAPPER) Wrapper<T> where);

    /**
     * 物理删除
     */
    @DeleteProvider(value = InnerSqlProvider.class, method = "deleteByPk")
    int physicalDeleteByPk(Serializable pk);

    /**
     * 物理删除
     */
    @DeleteProvider(value = InnerSqlProvider.class, method = "deleteInPks")
    int physicalDeleteInPks(Collection<? extends Serializable> pks);

    class InnerSqlProvider {

        public String deleteInPks(ProviderContext providerContext) {
            return CommonSqlBuildUtil.deleteByIds(providerContext);
        }

        public String deleteByPk(ProviderContext providerContext) {
            return CommonSqlBuildUtil.deleteById(providerContext);
        }

        public String deleteByWrapper(@Param(Constants.WRAPPER) Wrapper ew, ProviderContext providerContext) {
            Class<?> typeArgument = ClassUtil.getTypeArgument(providerContext.getMapperType());
//            String where = ((Wrapper) param.get(Constants.WRAPPER)).getSqlSegment();
            return "delete from "+ DaoFactory.getTableEntityMetaData().tableName(typeArgument) + " where " + ew.getSqlSegment();
        }
    }
}
