package cn.cjpw.infra.spec.dao.mybatis;


import cn.cjpw.infra.spec.dao.mybatis.plus.MPTableEntityMetaDataProvider;

/**
 * @author jun.chen1
 * @since 2022/10/13 14:32
 **/
public class DaoFactory {

    private final static DaoFactory instance = new DaoFactory();

    private TableEntityMetaData tableEntityMetaData;

    private static DaoFactory getFactory() {
        return instance;
    }

    static {
        getFactory().register(new MPTableEntityMetaDataProvider());
    }

    public static void register(TableEntityMetaData tableEntityMetaData) {
        getFactory().tableEntityMetaData = tableEntityMetaData;
    }

    public static TableEntityMetaData getTableEntityMetaData() {
        return getFactory().tableEntityMetaData;
    }


}
