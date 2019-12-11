package com.migration.BI.controller;

import com.migration.BI.service.BIService;
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
@RequestMapping("biController")
public class BIController {

    @Autowired
    private BIService service;
    @Autowired
    private RedisService redisService;
    //选择连接ip
    @GetMapping("list")
    public String showDataBase(ModelMap map){
        List<Map> list = service.showDataBase();
        map.put("list",list);
        return "list";
    }

    //选择数据库
    @PostMapping("showDataBaseBi/{id}")
    @ResponseBody
    public List<Map> showDataBase(@PathVariable Integer id){
        Map ipSource = service.queryIpSourceBi(id);
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
    @PostMapping("queryTableNameBi/{dataName}")
    @ResponseBody
    public List<Map> queryTableName(@PathVariable String dataName) throws SQLException {
        //获取源数据库驱动信息
        String source = redisService.get("biQuerySource_");
        Map map = JsonUtils.jsonToPojo(source, Map.class);
        Map sorceMap = JdbcUtils.checkDataSorceMap(map, dataName);
        String s = JsonUtils.objectToJson(sorceMap);
        redisService.set("biDataBase",s);
        List<Map> tableNames = JdbcUtils.getTableNames(sorceMap, dataName);
        return tableNames;
    }

    @PostMapping("queryTable/{tableName}")
    @ResponseBody
    public List<Map> queryTable(@PathVariable String tableName) throws SQLException {
        String s = redisService.get("biDataBase");
        Map sorceMap = JsonUtils.jsonToPojo(s, Map.class);
        List<Map> list = service.queryTable(sorceMap,tableName);
        return list;
    }

}
