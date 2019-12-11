package com.aliu.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("任务表")
public class Task {


    @ApiModelProperty("任务编号")
    private int id;
    @ApiModelProperty("源数据库")
    private String sorceurl;
    @ApiModelProperty("源表明")
    private String sorcetablename;
    @ApiModelProperty("目标数据库")
    private String targeturl;
    @ApiModelProperty("目标表名")
    private String targettablename;
    @ApiModelProperty("字段")
    private String fields;
    @ApiModelProperty("状态")
    private String start;
    @ApiModelProperty("执行任务用户")
    private String uname;
    @ApiModelProperty("执行任务用户id")
    private String uid;

    public Task(int id, String sorceurl, String sorcetablename, String targeturl, String targettablename, String fields, String start, String uname, String uid) {
        this.id = id;
        this.sorceurl = sorceurl;
        this.sorcetablename = sorcetablename;
        this.targeturl = targeturl;
        this.targettablename = targettablename;
        this.fields = fields;
        this.start = start;
        this.uname = uname;
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Task() {
    }

    public Task(int id, String sorceurl, String sorcetablename, String targeturl, String targettablename, String fields, String start) {
        this.id = id;
        this.sorceurl = sorceurl;
        this.sorcetablename = sorcetablename;
        this.targeturl = targeturl;
        this.targettablename = targettablename;
        this.fields = fields;
        this.start = start;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSorceurl() {
        return sorceurl;
    }

    public void setSorceurl(String sorceurl) {
        this.sorceurl = sorceurl;
    }

    public String getSorcetablename() {
        return sorcetablename;
    }

    public void setSorcetablename(String sorcetablename) {
        this.sorcetablename = sorcetablename;
    }

    public String getTargeturl() {
        return targeturl;
    }

    public void setTargeturl(String targeturl) {
        this.targeturl = targeturl;
    }

    public String getTargettablename() {
        return targettablename;
    }

    public void setTargettablename(String targettablename) {
        this.targettablename = targettablename;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
