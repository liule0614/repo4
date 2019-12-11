package com.migration.controller;

import com.migration.entity.CodeMsg;
import com.migration.entity.Result;
import com.migration.service.DBAService;
import com.migration.util.JdbcUtils;
import com.migration.util.JsonUtils;
import com.migration.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("dbaController")
public class DBAController {

    @Autowired
    private DBAService service;
    @Autowired
    RedisService redisService;

    //执行任务
    @PostMapping("execyteTheTask/{id}")
    @ResponseBody
    public String  execyteTheTask(@PathVariable Integer id) throws SQLException {
        String task = service.getTask(id);
        return task;
    }

    //展示任务列表
    @GetMapping("queryTask")
    @ResponseBody
    public List<Map> queryTask(){
        List<Map> list = service.queryTask();
        return list;
    }

    //选择连接ip
    @GetMapping("list")
    @ResponseBody
    public String showDataBase(ModelMap map){
        List<Map> list = service.showDataBase();
        map.put("list",list);
        return "list";
    }

    //选择数据库
    @PostMapping("showDataBase")
    @ResponseBody
    public List<Map> showDataBase(Integer id){
        Map ipSource = service.queryIpSource(id);
        //根据角色获取连接信息
        Map sorceMap = JdbcUtils.checkDataSorceMap(ipSource,null);
        List<Map> queryData = null;
        try {
            queryData = JdbcUtils.getDateName(sorceMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryData;
    }

    //选择数据库下的表
    @PostMapping("queryTableName")
    @ResponseBody
    public List<Map> queryTableName(String dataName) throws SQLException {
        //获取源数据库驱动信息
        String source = redisService.get("querySource_");
        Map map = JsonUtils.jsonToPojo(source, Map.class);
        Map sorceMap = JdbcUtils.checkDataSorceMap(map, dataName);
        String s = JsonUtils.objectToJson(sorceMap);
        //存储源数据库连接信息
        redisService.set("sorceDataBase",s);
        List<Map> tableNames = JdbcUtils.getTableNames(sorceMap, dataName);
        return tableNames;
    }

    //表字段
    @PostMapping("queryTableField")
    @ResponseBody
    public List<String> queryTableField( String tablename){
            //选择表之后存入redis
            String base = redisService.get("sorceDataBase");
            Map map = JsonUtils.jsonToPojo(base, Map.class);
            List<String> names = JdbcUtils.getColumnNames(tablename, map);
            //存储源数据库要迁移的表名
            redisService.set("sorceTableNames",tablename);
            return names;

    }

    //保存要迁移的字段
    @PostMapping("saveTableField/{fields}")
    @ResponseBody
    public void saveTableField(@PathVariable String fields) throws SQLException{
        redisService.set("fields",fields);
        //选择完字段点击迁移之后保存
    }


    //选择目标ip
    @GetMapping("showtargetDataBase")
    @ResponseBody
    public List<Map> showtargetDataBase(){
        List<Map> list = service.showDataBase();
        return list;
    }

    //目标数据库展示
    @PostMapping("targetDatabase/{id}")
    @ResponseBody
    public List<Map> targetDatabase(@PathVariable Integer id){
        Map ipSource = service.queryIpTarget(id);
        Map sorceMap = JdbcUtils.checkDataSorceMap(ipSource,null);
        List<Map> queryData = null;
        try {
            queryData = JdbcUtils.getDateName(sorceMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryData;
    }

    //选择目标数据库
    @PostMapping("targetData/{dataName}")
    @ResponseBody
    public void targetData(@PathVariable String dataName){
        redisService.set("targetDataName",dataName);
        String target = redisService.get("queryTarget_");
        Map map = JsonUtils.jsonToPojo(target, Map.class);
        Map targetMap = JdbcUtils.checkDataSorceMap(map, dataName);
        String s = JsonUtils.objectToJson(targetMap);
        //存储目标数据库连接信息
        redisService.set("targetDataBase",s);
    }

    //判断目标数据库是否有相同命名的表
    @GetMapping("estimateTable")
    @ResponseBody
    public boolean estimateTable() throws SQLException {
        String base = redisService.get("targetDataBase");
        Map isTarget = JsonUtils.jsonToPojo(base, Map.class);
        String tableName = redisService.get("sorceTableNames");
        System.out.println(tableName);
        List<Map> list = JdbcUtils.getTableNames(isTarget, redisService.get("targetDataName"));
        boolean table = true;
        for (Map map: list){
            table = tableName.equals(map.get("table"));
        }
        //true 有  false 无
        if(false==table){
            redisService.set("targetTableName",tableName);
        }
        return table;
    }

    //如果相同判断输入的表名是否存在
    @PostMapping("senseTableName/{tableName}")
    @ResponseBody
    public String senseTableName(@PathVariable String tableName) throws SQLException {
        String base = redisService.get("targetDataBase");
        Map isTarget = JsonUtils.jsonToPojo(base, Map.class);
        boolean b = JdbcUtils.validateTableNameExist(isTarget, tableName);
        //true 有  false 无
        if(b==false){
            redisService.set("targetTableName",tableName);
            return "成功了";
        }else {
            return "有这个表明";
        }
    }

    @GetMapping("submitTask")
    @ResponseBody
    public Result submitTask(){
        int i = service.addTask();
        if(i>0){
            return Result.success("提交任务成功");
        }else{
            return Result.error(CodeMsg.SUBMIT_ERROR);
        }
    }

}
