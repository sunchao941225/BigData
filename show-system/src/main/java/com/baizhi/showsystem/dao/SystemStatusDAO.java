package com.baizhi.showsystem.dao;

import com.baizhi.showsystem.entity.SystemStatus;

import java.util.List;

public interface SystemStatusDAO {

    public List<SystemStatus> findAll(String accessDate);
}
