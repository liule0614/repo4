package com.aliu.dao;

import com.aliu.entity.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface ListMapper {

    @Select("select name,id from datamessage where type='1'")
    List<Map> showDataBase();

    @Select("select * from datamessage where type=1 and id=#{id}")
    Map queryIpSource(@RequestParam(value = "id") Integer id);

    @Insert("insert into task (sorceurl, sorcetablename,targeturl, targettablename, fields, start) values (#{sorceurl}, #{sorcetablename}, #{targeturl}, #{targettablename}, #{fields}, #{start})")
    int addTask(Task task);

    @Select("select * from task")
    List<Task> queryTask();

    @Select("select * from task where id=#{id}")
    Task getTask(@RequestParam(value = "id")Integer id);

}
