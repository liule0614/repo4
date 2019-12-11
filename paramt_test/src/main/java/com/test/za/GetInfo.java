package com.test.za;

import org.junit.Test;

import java.sql.*;

public class GetInfo {
    //获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ssm?useSSL=false";
            String user = "root";
            String pass = "root";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获取数据库信息
     */
    @Test
    public static void getDatebaseInfo() {
        Connection con = getConnection();
        DatabaseMetaData dbMetaData = null;
        try {
            dbMetaData = con.getMetaData();
            System.out.println(dbMetaData.getDriverName());
            System.out.println(dbMetaData.getURL());
            System.out.println(dbMetaData.getUserName());
            System.out.println("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某数据库所有表信息
     */
    @Test
    public static void getTablesInfo() throws SQLException {
        Connection conn = getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});
        int i = 0;
        while (rs.next()) {
//            i++;
//            if(i==10) {
//                break;
//            }
            System.out.println("表名：" + rs.getString("TABLE_NAME"));
            System.out.println("表类型：" + rs.getString("TABLE_TYPE"));
            System.out.println("表所属数据库：" + rs.getString("TABLE_CAT"));
            System.out.println("表备注：" + rs.getString("REMARKS"));
            //tables.add(rs.getString("TABLE_NAME"));
        }
    }

    /**
     * 获取某某表的字段信息
     */
    public static void getTableColumnInfo() throws SQLException {
        Connection conn = getConnection();
        String sql = "select  *  from  person_6_17";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData data = rs.getMetaData();
            for (int i = 1; i <= data.getColumnCount(); i++) {
                //  获得所有列的数目及实际列数
                int columnCount = data.getColumnCount();
                //  获得指定列的列名
                String columnName = data.getColumnName(i);
                //  获得指定列的数据类型名
                String columnTypeName = data.getColumnTypeName(i);
                //  对应数据类型的类
                String columnClassName = data.getColumnClassName(i);
                //  在数据库中类型的最大字符个数
                int columnDisplaySize = data.getColumnDisplaySize(i);
                //  某列类型的精确度(类型的长度)
                int precision = data.getPrecision(i);
                //  小数点后的位数
                int scale = data.getScale(i);
                //  获取某列对应的表名
                String tableName = data.getTableName(i);
                //  是否自动递增
                Boolean isAutoInctement = data.isAutoIncrement(i);
                //  是否为空
                int isNullable = data.isNullable(i);
                System.out.println(columnCount);
                System.out.println("获得列" + i + "的字段名称:" + columnName);
                System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
                System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
                System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
                System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
                System.out.println("获得列" + i + "小数点后的位数:" + scale);
                System.out.println("获得列" + i + "对应的表名:" + tableName);
                System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
                System.out.println("获得列" + i + "是否为空:" + isNullable);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            //DROP TABLE IF EXISTS `goods`;
            //CREATE TABLE `goods` (
            //  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
            //  `goods_name` varchar(16) DEFAULT NULL COMMENT '商品名称',
            //  `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
            //  `goods_img` varchar(64) DEFAULT NULL COMMENT '商品图片',
            //  `goods_detail` longtext COMMENT '商品详细介绍',
            //  `goods_price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
            //  `goods_stock` int(11) DEFAULT NULL COMMENT '商品库存，-1表示没有限制',
            //  PRIMARY KEY (`id`)
            //) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
        }
    }

    /**
     * 获取某表数据及信息
     *
     * @param args
     */
    public static void main(String[] args) {
        Connection conn = getConnection();
        String sql = "select  *  from  user";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData data = rs.getMetaData();
            for (int i = 1; i <= data.getColumnCount(); i++) {
                //  获得所有列的数目及实际列数
                int columnCount = data.getColumnCount();
                //  获得指定列的列名
                String columnName = data.getColumnName(i);
                //  获得指定列的列值
                int columnType = data.getColumnType(i);
                //  获得指定列的数据类型名
                String columnTypeName = data.getColumnTypeName(i);
                //  所在的Catalog名字
                String catalogName = data.getCatalogName(i);
                //  对应数据类型的类
                String columnClassName = data.getColumnClassName(i);
                //  在数据库中类型的最大字符个数
                int columnDisplaySize = data.getColumnDisplaySize(i);
                //  默认的列的标题
                String columnLabel = data.getColumnLabel(i);
                //  获得列的模式
                String schemaName = data.getSchemaName(i);
                //  某列类型的精确度(类型的长度)
                int precision = data.getPrecision(i);
                //  小数点后的位数
                int scale = data.getScale(i);
                //  获取某列对应的表名
                String tableName = data.getTableName(i);
                //  是否自动递增
                Boolean isAutoInctement = data.isAutoIncrement(i);
                //  在数据库中是否为货币型
                Boolean isCurrency = data.isCurrency(i);
                //  是否为空
                int isNullable = data.isNullable(i);
                //  是否为只读
                Boolean isReadOnly = data.isReadOnly(i);
                //  能否出现在where中
                Boolean isSearchable = data.isSearchable(i);
                System.out.println(columnCount);
                System.out.println("获得列" + i + "的字段名称:" + columnName);
                System.out.println("获得列" + i + "的类型,返回SqlType中的编号:" + columnType);
                System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
                System.out.println("获得列" + i + "所在的Catalog名字:" + catalogName);
                System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
                System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
                System.out.println("获得列" + i + "的默认的列的标题:" + columnLabel);
                System.out.println("获得列" + i + "的模式:" + schemaName);
                System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
                System.out.println("获得列" + i + "小数点后的位数:" + scale);
                System.out.println("获得列" + i + "对应的表名:" + tableName);
                System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
                System.out.println("获得列" + i + "在数据库中是否为货币型:" + isCurrency);
                System.out.println("获得列" + i + "是否为空:" + isNullable);
                System.out.println("获得列" + i + "是否为只读:" + isReadOnly);
                System.out.println("获得列" + i + "能否出现在where中:" + isSearchable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}