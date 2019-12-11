package com.test.main;

import com.test.thread.DomeThread;
import com.test.util.JdbcUtils;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Dome1 extends Thread{

    public static Map<String,String> sourceDateBase(){
        Map map=new HashMap();
        map.put("driver", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://10.1.73.24:3390/five?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        map.put("username", "root");
        map.put("pass", "root");
        return map;
    }

    public static Map<String,String> targetDataBase(){
        Map maps=new HashMap();
        maps.put("driver", "com.mysql.jdbc.Driver");
        maps.put("url", "jdbc:mysql://127.0.0.1:3306/shixun?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        maps.put("username", "root");
        maps.put("pass", "root");
        return maps;
    }


    public void recursive(int num) throws SQLException {
        String tableName="t_user";
        //查询总条数
        int count = JdbcUtils.getCount(sourceDateBase(),tableName );
        //一条线程一万条
        int pageSize = 20000;
        //共多少页
        int pageCount = count/pageSize;
        if(count%pageSize>0){
            pageCount=pageCount+1;
        }
        if(pageCount<10){
            for (int i =0;i<pageCount;i++){
                DomeThread dome = new DomeThread();
                String sql = " "+tableName +" limit "+num*pageSize+","+pageSize;
                num++;
                dome.database(tableName,sql);
                Thread thread = new Thread(dome);
                thread.start();
            }
        }
        if (pageCount>=10){
            //启动10个线程
            for (int i = 0;i<10;i++){
                DomeThread dome = new DomeThread();
                String sql = " "+tableName +" limit "+num*pageSize+","+pageSize;
                num++;
                dome.database(tableName,sql);
                Thread thread = new Thread(dome);
                thread.start();

            }
        }


        if(num<pageCount){
            try {
                Thread.sleep(10000);
                System.gc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recursive(num);
        }
        if(num>=pageCount){
            return ;
        }
    }

    public static void main(String[] args) throws SQLException {
       /* //查询
        List<Map> list = JdbcUtils.queryData(Dome1.sourceDateBase(), "user limit 0,5000");
        //主键
        String primary = JdbcUtils.getPrimary(Dome1.sourceDateBase(), "user");
        //查询字段名及类型
        Map user1 = JdbcUtils.getMap("user limit 0,5000", Dome1.sourceDateBase());
        //生成建表语句
        String s = JdbcUtils.creatTable(user1, "user", primary);
        //生成添加语句
        List<String> checkSql = JdbcUtils.checkSql(list, "user");
        JdbcUtils.thing(Dome1.targetDataBase(),s);
        JdbcUtils.thing2(Dome1.targetDataBase(),checkSql);
        System.out.println("执行成功了");*/


        /*List<String> user = JdbcUtils.getColumnNames("miaosha_user", sourceDateBase());
        System.out.println(user);*/

        /*boolean user = JdbcUtils.validateTableNameExist(targetDataBase(), "user");
        System.out.println(user);*/


        Dome1 dome1 = new Dome1();
        dome1.d();
        System.out.println("执行完了");
    }

    public void d() throws SQLException {
        Dome1 dome1 = new Dome1();
        String primary = JdbcUtils.getPrimary(sourceDateBase(),"t_user");
        Map miaosha_user = JdbcUtils.getMap("t_user", sourceDateBase());
        String user = JdbcUtils.creatTable(miaosha_user, "t_user", primary);
        JdbcUtils.thing(targetDataBase(),user);
        dome1.recursive(0);
    }


}
