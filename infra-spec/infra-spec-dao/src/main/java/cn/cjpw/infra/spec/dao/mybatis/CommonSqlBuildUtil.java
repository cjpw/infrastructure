package cn.cjpw.infra.spec.dao.mybatis;

import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.List;

/**
 * @author jun.chen1
 * @since 2022/10/13 14:21
 **/
public class CommonSqlBuildUtil {

    private static TableEntityMetaData getTableEntityMetaData() {
        return DaoFactory.getTableEntityMetaData();
    }

    public static String getById(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        return "SELECT " + String.join(",", columns) + " FROM " + tableName + " WHERE " + DaoFactory.getTableEntityMetaData()
            .getIdColumn(eClass) + " = #{id}";
    }

    public static String listByEntity(Object object) {
        Class eClass = object.getClass();
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder("<script> SELECT ");
        sql.append(String.join(",", columns));
        sql.append(" FROM ").append(tableName);
        sql.append(" <where>");
        whereByEntity(fields, columns, sql);
        sql.append("</where></script>");
        return sql.toString();
    }

    public static String getByEntity(Object object) {
        return listByEntity(object) + " LIMIT 1";
    }

    public static String listByIds(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder("<script> SELECT ");
        sql.append(String.join(",", columns));
        sql.append(" FROM ").append(tableName);
        sql.append(" WHERE ").append(DaoFactory.getTableEntityMetaData().getIdColumn(eClass)).append(" IN ");
        sql.append("<foreach item=\"item\" collection=\"list\" separator=\",\" open=\"(\" close=\")\" index=\"index\">");
        sql.append("#{item}</foreach></script>");
        return sql.toString();
    }

    public static String listByColumn(String column, ProviderContext context) throws Exception {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        if (!fields.contains(column)) {
            throw new Exception("not exist column '" + column + "'");
        }
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        return "SELECT " + String.join(",", columns) + " FROM " + tableName + " WHERE " + DaoFactory.getTableEntityMetaData()
            + column + " = #{val}";
    }

    public static String getByColumn(String column, ProviderContext context) throws Exception {
        return listByColumn(column, context) + " LIMIT 1";
    }

    public static String insert(Object object) {
        Class eClass = object.getClass();
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder();
        sql.append("<script> INSERT INTO ").append(tableName);
        sql.append(" <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (int i = 0; i < fields.size(); i++) {
            sql.append("<if test=\"").append(fields.get(i)).append(" != null\">");
            sql.append(columns.get(i)).append(",").append("</if>");
        }
        sql.append("</trim><trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        for (int i = 0; i < fields.size(); i++) {
            sql.append("<if test=\"").append(fields.get(i)).append(" != null\">");
            sql.append("#{").append(fields.get(i)).append("},").append("</if>");
        }
        sql.append("</trim></script>");
        return sql.toString();
    }

    public static String insertBatch(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder();
        sql.append("<script> INSERT INTO ").append(tableName);
        sql.append("(").append(String.join(", ", columns)).append(") values ");
        sql.append("<foreach item=\"item\" collection=\"list\" separator=\",\" open=\"\" close=\"\" index=\"index\"> (");
        for (int i = 0; i < fields.size(); i++) {
            sql.append("#{item.").append(fields.get(i)).append("}");
            if (i < fields.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")</foreach></script>");
        return sql.toString();
    }

    public static String update(Object object) {
        Class eClass = object.getClass();
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder("<script> UPDATE ");
        sql.append(tableName).append(" <set>");
        for (int i = 1; i < fields.size(); i++) {
            sql.append("<if test=\"").append(fields.get(i)).append(" != null\">");
            sql.append(columns.get(i)).append(" = #{").append(fields.get(i)).append("},</if>");
        }
        sql.append("</set> WHERE ").append(DaoFactory.getTableEntityMetaData().getIdColumn(eClass));
        sql.append(" = #{").append(DaoFactory.getTableEntityMetaData().getIdField(eClass)).append("} </script>");
        return sql.toString();
    }

    public static String updateBatch(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder("<script> UPDATE ");
        sql.append(tableName).append(" <trim prefix=\"set\" suffixOverrides=\",\">");
        for (int i = 1; i < fields.size(); i++) {
            sql.append("<trim prefix=\"").append(columns.get(i)).append(" = case\" suffix=\"end,\">");
            sql.append("<foreach collection=\"list\" item=\"item\" index=\"index\">");
            sql.append("when ").append(DaoFactory.getTableEntityMetaData().getIdColumn(eClass));
            sql.append(" = #{item.").append(DaoFactory.getTableEntityMetaData().getIdField(eClass)).append("} then #{item.")
                .append(fields.get(i)).append("}");
            sql.append("</foreach></trim>");
        }
        sql.append("</trim> WHERE ").append(DaoFactory.getTableEntityMetaData().getIdColumn(eClass)).append(" IN ");
        sql.append("<foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\" open=\"(\" close=\")\">");
        sql.append("#{item.").append(DaoFactory.getTableEntityMetaData().getIdField(eClass)).append("} </foreach></script>");
        return sql.toString();
    }

    public static String deleteById(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        return "DELETE FROM " + tableName + " WHERE " + DaoFactory.getTableEntityMetaData().getIdColumn(eClass) + " = #{id}";
    }

    public static String deleteByEntity(Object object) {
        Class eClass = object.getClass();
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder("<script> DELETE FROM ");
        sql.append(tableName).append(" <where> ");
        whereByEntity(fields, columns, sql);
        sql.append("</where></script>");
        return sql.toString();
    }

    public static String deleteByIds(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        StringBuilder sql = new StringBuilder("<script> DELETE FROM ");
        sql.append(tableName).append(" WHERE ").append(DaoFactory.getTableEntityMetaData().getIdColumn(eClass)).append(" IN ");
        sql.append("<foreach item=\"item\" collection=\"list\" separator=\",\" open=\"(\" close=\")\" index=\"index\">");
        sql.append("#{item}</foreach></script>");
        return sql.toString();
    }

    public static String countAll(ProviderContext context) {
        Class eClass = DaoFactory.getTableEntityMetaData().getEntityType(context);
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        return "SELECT COUNT(*) FROM " + tableName;
    }

    public static String countByEntity(Object object) {
        Class eClass = object.getClass();
        String tableName = DaoFactory.getTableEntityMetaData().tableName(eClass);
        List<String> fields = DaoFactory.getTableEntityMetaData().getAllFields(eClass);
        List<String> columns = DaoFactory.getTableEntityMetaData().getAllColumns(eClass);
        StringBuilder sql = new StringBuilder("<script> SELECT COUNT(*) FROM ");
        sql.append(tableName).append(" <where> ");
        whereByEntity(fields, columns, sql);
        sql.append("</where></script>");
        return sql.toString();
    }

    private static void whereByEntity(List<String> fields, List<String> columns, StringBuilder sql) {
        for (int i = 0; i < fields.size(); i++) {
            sql.append("<if test=\"").append(fields.get(i)).append(" != null\">");
            sql.append("and ").append(columns.get(i));
            sql.append(" = #{").append(fields.get(i)).append("}</if>");
        }
    }
}

