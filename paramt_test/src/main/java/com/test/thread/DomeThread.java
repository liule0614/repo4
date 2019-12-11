package com.test.thread;

import ch.qos.logback.classic.db.names.TableName;
import com.test.main.Dome1;
import com.test.util.JdbcUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DomeThread implements Runnable {

    public String SQL;
    public String TableName;
    public void database(String tableName,String sql){
        SQL = sql;
        TableName=tableName;
    }


    public void run() {
        try {
            List<Map> list = JdbcUtils.queryData(Dome1.sourceDateBase(), SQL,null);
            List<String> sql = JdbcUtils.checkSql(list, TableName);
            JdbcUtils.thing2(Dome1.targetDataBase(),sql);
            list.clear();
            sql.clear();
            System.gc();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
