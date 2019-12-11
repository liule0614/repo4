package com.migration.BI.service;

import com.migration.BI.dao.BIDao;
import com.migration.util.JdbcUtils;
import com.migration.util.JsonUtils;
import com.migration.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class BIService {

    @Autowired
    private BIDao bdao;
    @Autowired
    private RedisService redisa;
    public List<Map> showDataBase() {
        List<Map> list = bdao.showDataBase();
        return list;
    }

    public Map queryIpSourceBi(Integer id) {
        Map map = bdao.queryIpSource(id);
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
