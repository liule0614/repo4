package com.aliu.service;

import com.aliu.dao.ListMapper;
import com.aliu.entity.Task;
import com.aliu.thread.ListThread;
import com.aliu.util.JdbcUtils;
import com.aliu.util.JsonUtils;
import com.aliu.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class ListService {

    @Autowired
    private ListMapper listMapper;
    @Autowired
    private RedisService redisa;

    public List<Map> showDataBase() {
        List<Map> list = listMapper.showDataBase();
        return list;
    }

    public Map queryIpSource(Integer id) {
        Map map = listMapper.queryIpSource(id);
        String s = JsonUtils.objectToJson(map);
        //角色信息存入redis
        redisa.set("querySource_",s);
        return map;
    }

    public Map queryIpTarget(Integer id) {
        Map map = listMapper.queryIpSource(id);
        String s = JsonUtils.objectToJson(map);
        //角色信息存入redis
        redisa.set("queryTarget_",s);
        return map;
    }

    //存储任务表
    public int addTask() {
        Task task = new Task();
        task.setStart("0");
        task.setSorceurl(redisa.get("sorceDataBase"));
        task.setSorcetablename(redisa.get("sorceTableNames"));
        task.setTargeturl(redisa.get("targetDataBase"));
        task.setTargettablename(redisa.get("targetTableName"));
        task.setFields(redisa.get("fields"));
        int i = listMapper.addTask(task);
        return i;
    }


    //展示任务表信息
    public List<Map> queryTask() {
        List<Map> mapList =new ArrayList<Map>();
        Map<String,String> maps = null;
        List<Task> list = listMapper.queryTask();
        for (Task task : list){
            maps = new HashMap<>();
            String sorceurl = task.getSorceurl();
            String fields = task.getFields();
            String sorcetablename = task.getSorcetablename();
            String targettablename = task.getTargettablename();
            String targeturl = task.getTargeturl();
            Map map = JsonUtils.jsonToPojo(sorceurl, Map.class);
            Map tarmap = JsonUtils.jsonToPojo(targeturl, Map.class);
            String tarurl = (String) tarmap.get("url");
            String tarname = (String) tarmap.get("name");
            String url = (String) map.get("url");
            String name = (String) map.get("name");
            String urls = cutOut(url);
            String tarurls = cutOut(tarurl);
            maps.put("name",name);
            maps.put("url",urls);
            maps.put("tarurl",tarurls);
            maps.put("tarname",tarname);
            maps.put("sorcetablename",sorcetablename);
            maps.put("targettablename",targettablename);
            maps.put("fields",fields);
            int id = task.getId();
            maps.put("id",""+id);
            mapList.add(maps);
        }
        return mapList;
    }
    public String cutOut(String string){
        int indexOf = string.indexOf("?");
        String s = string.substring(0, indexOf);
        int i = s.lastIndexOf("/");
        String substring = s.substring(i + 1);
        return substring;
    }

    //执行任务
    public String getTask(Integer id) throws SQLException {
        Task task = listMapper.getTask(id);
        String sorceurl = task.getSorceurl();
        //源数据库连接信息
        Map sorceMap = JsonUtils.jsonToPojo(sorceurl, Map.class);
        //源数据库表名
        String sorcetablename = task.getSorcetablename();
        String targeturl = task.getTargeturl();
        //目标数据库连接信息
        Map targetMap = JsonUtils.jsonToPojo(targeturl, Map.class);
        //目标数据库表名
        String targettablename = task.getTargettablename();
        //要迁移的字段
        String fields = task.getFields();
        if("".equals(fields)||null==fields){
            fields=null;
        }
        //查询主键
        String primary = JdbcUtils.getPrimary(sorceMap, sorcetablename);
        Map map = JdbcUtils.getMap(fields, sorcetablename, sorceMap);
        //建表语句
        String s = JdbcUtils.creatTable(map, sorcetablename, primary);
        //提交
        System.out.println(targetMap+"333");
        System.out.println(s+"123");
        JdbcUtils.thing(targetMap,s);
        //调用迁移方法
        recursive(0,sorceMap,sorcetablename,targetMap,targettablename,fields);
        return "执行完成";
    }


    //调用线程方法
    public void recursive(int num, Map sorceMap, String sorcetablename, Map targetMap, String targettablename, String fields) throws SQLException {
        //查询总条数
        int count = JdbcUtils.getCount(sorceMap,sorcetablename );
        //一条线程一万条
        int pageSize = 20000;
        //共多少页
        int pageCount = count/pageSize;
        if(count%pageSize>0){
            pageCount=pageCount+1;
        }
        if(pageCount<10){
            for (int i =0;i<pageCount;i++){
                ListThread dome = new ListThread();
                String sql = " "+sorcetablename +" limit "+num*pageSize+","+pageSize;
                num++;
                dome.database(targettablename,sql,sorceMap,targetMap,fields);
                Thread thread = new Thread(dome);
                thread.start();
            }
        }
        if (pageCount>=10){
            //启动10个线程
            for (int i = 0;i<10;i++){
                ListThread dome = new ListThread();
                String sql = " "+sorcetablename +" limit "+num*pageSize+","+pageSize;
                num++;
                dome.database(targettablename,sql,sorceMap,targetMap,fields);
                Thread thread = new Thread(dome);
                thread.start();

            }
        }
        if(num<pageCount){
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recursive(num,sorceMap,sorcetablename,targetMap,targettablename,fields);
        }
        if(num>=pageCount){
            return ;
        }
    }

    public Map queryIpSourceBi(Integer id) {
        Map map = listMapper.queryIpSource(id);
        String s = JsonUtils.objectToJson(map);
        //角色信息存入redis
        redisa.set("biQuerySource_",s);
        return map;
    }

    public List queryTable(Map sorceMap, String tableName) throws SQLException {
        String tableNames = tableName+" limit 0,50";
        List<Map> mapList = JdbcUtils.queryData(sorceMap, tableNames, null);
        return mapList;
    }


}
