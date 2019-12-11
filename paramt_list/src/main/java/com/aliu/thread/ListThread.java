package com.aliu.thread;

import com.aliu.util.JdbcUtils;
import com.aliu.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ListThread implements Runnable{

    public String SQL;
    public Map soucreData;
    public Map targetData;
    public String fields;
    public String targetTableName;

    public void database(String targetTableName,String sql,Map soucreData,Map targetData,String fields){
        this.targetTableName = targetTableName;
        this.SQL = sql;
        this.soucreData = soucreData;
        this.targetData = targetData;
        this.fields = fields;
    }

    @Autowired
    RedisService redisService;

    public void run() {
        try {
            List<Map> list = JdbcUtils.queryData(soucreData, SQL,fields);
            List<String> sql = JdbcUtils.checkSql(list, targetTableName);
            JdbcUtils.thing2(targetData,sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
