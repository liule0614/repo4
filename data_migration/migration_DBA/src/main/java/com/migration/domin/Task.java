package com.migration.domin;

public class Task {

    private int id;
    private String sorceurl;
    private String sorcetablename;
    private String targeturl;
    private String targettablename;
    private String fields;
    private String start;
    private String uname;

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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Task() {
    }

    public Task(int id, String sorceurl, String sorcetablename, String targeturl, String targettablename, String fields, String start, String uname) {
        this.id = id;
        this.sorceurl = sorceurl;
        this.sorcetablename = sorcetablename;
        this.targeturl = targeturl;
        this.targettablename = targettablename;
        this.fields = fields;
        this.start = start;
        this.uname = uname;
    }
}
