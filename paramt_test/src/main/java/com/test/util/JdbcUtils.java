package com.test.util;

import java.sql.*;
import java.util.*;

import static javax.swing.UIManager.getInt;


public class JdbcUtils {

    private static String SQL = "select * from ";
    /**
     * 链接jdbc
     * @return
     */
    public static  Connection getConnection(Map dataSourceMap){

        String driver = (String) dataSourceMap.get("driver");
        String url = (String) dataSourceMap.get("url");
        String username = (String) dataSourceMap.get("username");
        String pass = (String) dataSourceMap.get("pass");

        Connection connection=null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,pass);
            DatabaseMetaData data = connection.getMetaData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }




    /*
     * 获取字段和类型得map
     *
     * **/
    public static Map getMap(String tabelName,Map map1) {
        Map<String,String> map=new HashMap<String,String>();
        List<String> columnNames = JdbcUtils.getColumnNames(tabelName, map1);
        List<String> columnTypes = JdbcUtils.getColumnTypes(tabelName,map1);
        for (int i = 0; i < columnNames.size(); i++) {
            map.put(columnNames.get(i), columnTypes.get(i));
        }
        return map;
    }



    //查询
    public static List<Map> queryData(Map dataSourceMap,String tableName,String fields) throws SQLException{
        Connection connection = getConnection(dataSourceMap);
        if (fields==null){
            fields="*";
        }
        String sql = "select " +fields+ " from "+tableName+" limit 0,500";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);

        ResultSet executeQuery = prepareStatement.executeQuery();
        ResultSetMetaData metaData = executeQuery.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map resultmap=null;

        List<Map> resullist=new ArrayList();
        while(executeQuery.next()){
            resultmap =new HashMap();
            for(int i = 1;i<=columnCount;i++){
                String key = metaData.getColumnLabel(i);
                resultmap.put(key, executeQuery.getObject(i));

            }
            resullist.add(resultmap);
        }


        close(executeQuery, prepareStatement, connection);



        return resullist;
    }



    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }


    public static void affair(Map dataSourceMap,String sql) throws SQLException {

        Connection connection = getConnection(dataSourceMap);
        connection.setAutoCommit(false);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        int executeUpdate = prepareStatement.executeUpdate();
        connection.commit();
        close(null, prepareStatement, connection);


    }

    //拼接insert语句
    public static List<String> checkSql(List<Map> list,String tableName){
        StringBuffer sbuf=new StringBuffer();
        List<String> sqllist =new ArrayList();
        for (Map map : list) {
            sbuf.append("INSERT INTO ");
            sbuf.append(tableName);
            sbuf.append("(");
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                sbuf.append(key);
                sbuf.append(",");
            }
            String newString = sbuf.substring(0, sbuf.length()-1);
            sbuf.setLength(0);
            sbuf.append(newString);
            sbuf.append(") values (");

            for (String key : keySet) {
                sbuf.append("'");
                sbuf.append(map.get(key));
                sbuf.append("'");
                sbuf.append(",");
            }
            String newStringVlues = sbuf.substring(0, sbuf.length()-1);
            sbuf.setLength(0);
            sbuf.append(newStringVlues);
            sbuf.append(")");
            sqllist.add(sbuf.toString());
            sbuf.setLength(0);
        }





        return sqllist;

    }

    //获取数据库名
    public static void getDateName(Map map) throws SQLException {
        Connection connection = getConnection(map);
        DatabaseMetaData data = connection.getMetaData();
        ResultSet catalogs = data.getCatalogs();
        while (catalogs.next()){
            String cat = catalogs.getString("TABLE_CAT");
        }
        JdbcUtils.close(catalogs,null,connection);
    }

    /*
    *
    * 查询所有条数
    *
    * */
    public static int getCount(Map map,String tableName) throws SQLException {
        Connection connection = getConnection(map);
        Statement st=connection.createStatement();
        ResultSet rs =st.executeQuery("select count(*) from "+tableName);
        //创建变量存取个数
        int count=0;
        while(rs.next())
        {
            count = Integer.parseInt(rs.getString(1));
        }
        JdbcUtils.close(rs,null,connection);
        return count;
    }





    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName ,Map map) {
        List<String> columnTypes = new ArrayList();
        //与数据库的连接
        Connection conn = getConnection(map);
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName +" limit 0,1";
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                }
            }
        }
        return columnTypes;
    }


    /**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName,Map map) {

        List<String> columnNames = new ArrayList();
        //与数据库的连接
        Connection conn = getConnection(map);
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName + " limit 0,1";
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {

                }
            }
        }
        return columnNames;
    }



    /*
     * 拼接建表语句(建表语句)
     * **/
    public static String creatTable(Map map,String tabelName,String primary) throws SQLException {
        StringBuffer tabelString=new StringBuffer();

        //System.out.println(primary);
        tabelString.append("CREATE TABLE ");
        tabelString.append(tabelName);
        tabelString.append("(");
        Set<String> keySet = map.keySet();
        for (String string : keySet) {
            tabelString.append(string+" ");
            tabelString.append(map.get(string));
            if(primary!=null){
                if(string.equals(primary)) {
                    tabelString.append(" PRIMARY KEY");
                }
            }

            if (map.get(string).equals("VARCHAR")) {
                tabelString.append("(200)");
            }
            tabelString.append(",");
        }
        String substring = tabelString.substring(0,tabelString.length()-1);

        return substring+")";
    }



    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames(Map map) {
        List<String> tableNames = new ArrayList();
        Connection conn = getConnection(map);
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
            }
        }
        return tableNames;
    }


    public static void affair2(Map dataSourceMap,List<String> sqlList) throws SQLException {

        Connection connection = getConnection(dataSourceMap);
        connection.setAutoCommit(false);
        Statement createStatement = connection.createStatement();
        for (String string : sqlList) {
            createStatement.addBatch(string);
        }
        int[] executeBatch = createStatement.executeBatch();

        connection.commit();
        createStatement.close();
        close(null, null, connection);


    }

    public static void main(String[] args) throws SQLException {
        Map map=new HashMap();
        map.put("driver", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://127.0.0.1:3306/shixun?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        map.put("username", "root");
        map.put("pass", "root");
        String primary = getPrimary(map, "datamessage");
        System.out.println(primary);
    }

    /*
     * jdbc 无事物执行(查询获取主键字段名字)
     *
     *
     * **/

    public static String  getPrimary(Map map,String tabelName) throws SQLException{
        Connection connection = JdbcUtils.getConnection(map);
        PreparedStatement prepareStatement = connection.prepareStatement("SHOW KEYS FROM "+tabelName+" WHERE Key_name = 'PRIMARY'");
        ResultSet executeQuery = prepareStatement.executeQuery();
        ResultSetMetaData metaData = executeQuery.getMetaData();
        int columnCount = metaData.getColumnCount();//得到查询的sql的列数
            Map map2= null;
            List<Map> list=new ArrayList();
            while (executeQuery.next()) {
            map2 = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                map2.put(columnLabel, executeQuery.getObject(i));
            }
            list.add(map2);
        }
        Map map3 = list.get(0);
        String object = (String) map3.get("Column_name");
        JdbcUtils.close(executeQuery, prepareStatement, connection);
        return object;
        }


    /*
     * jdbc 无事物执行(查询)
     *
     *
     * **/

    public static List<Map>  resultList(Map map,String tabelName) throws SQLException{
        Connection connection = JdbcUtils.getConnection(map);
        PreparedStatement prepareStatement = connection.prepareStatement(SQL+tabelName);
        ResultSet executeQuery = prepareStatement.executeQuery();
        ResultSetMetaData metaData = executeQuery.getMetaData();
        int columnCount = metaData.getColumnCount();//得到查询的sql的列数
        Map map2= null;
        List<Map> list=new ArrayList();
        while(executeQuery.next()){
            map2=new HashMap();
            for(int i = 1;i<=columnCount;i++){
                String columnLabel = metaData.getColumnLabel(i);//表字段名字
                //System.out.println(columnLabel);输出所有字段
                map2.put(columnLabel, executeQuery.getObject(i));
//					System.out.print(columnLabel+":");
//					System.out.println(rs.getObject(i));//表字段数据
            }
            list.add(map2);
        }
        JdbcUtils.close(executeQuery, prepareStatement, connection);
        return list;
    }

    /*
     * jdbc涉及事物得(增删改)
     *
     * **/
    public static void thing(Map map,String sql) throws SQLException {
        Connection connection = JdbcUtils.getConnection(map);
        connection.setAutoCommit(false);//手动提交事物
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        int executeUpdate = prepareStatement.executeUpdate();

        connection.commit();//提交事物

        JdbcUtils.close(null, prepareStatement, connection);
    }


    /*
     * 执行多个sql事物 (批量提交)
     *
     * **/
    public static void thing2(Map map,List<String> sqlList) throws SQLException {
        Connection connection = JdbcUtils.getConnection(map);
        connection.setAutoCommit(false);//手动提交事物
        Statement createStatement = connection.createStatement();
        for (String string : sqlList) {
            createStatement.addBatch(string);
        }
        int[] executeBatch = createStatement.executeBatch();
        connection.commit();//提交事物
        createStatement.close();
        JdbcUtils.close(null, null, connection);
    }


    /**
     * jdbc关流
     * @param ex
     * @param pr
     * @param co
     */
    public static void close(ResultSet ex,PreparedStatement pr,Connection co){
        try {
            if (ex!=null) ex.close();
            if (pr!=null) pr.close();
            if (co!=null) co.close();


        } catch (Exception e2) {
            // TODO: handle exception
        }

    }


}
