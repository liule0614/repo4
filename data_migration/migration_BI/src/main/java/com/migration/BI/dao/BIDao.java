package com.migration.BI.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface BIDao {

    @Select("select name,id from datamessage where type='1'")
    List<Map> showDataBase();

    @Select("select * from datamessage where type=1 and id=#{id}")
    Map queryIpSource(@RequestParam(value = "id") Integer id);
}
