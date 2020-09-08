package com.baizhi.showsystem.entity;

import java.util.Date;

public class SystemStatus {

    private Integer id;
    private Integer statusCode;
    private Integer num;
    private String accessDate;

    public SystemStatus() {
    }

    public SystemStatus(Integer id, Integer statusCode, Integer num,String accessDate) {
        this.id = id;
        this.statusCode = statusCode;
        this.num = num;
        this.accessDate = accessDate;
    }

    @Override
    public String toString() {
        return "SystemStatus{" +
                "id=" + id +
                ", statusCode=" + statusCode +
                ", num=" + num +
                ", accessDate='" + accessDate + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(String accessDate) {
        this.accessDate = accessDate;
    }
}
