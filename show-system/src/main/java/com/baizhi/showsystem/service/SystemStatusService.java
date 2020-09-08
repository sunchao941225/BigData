package com.baizhi.showsystem.service;

import com.baizhi.showsystem.entity.SystemStatus;

import java.util.List;

public interface SystemStatusService {

    public List<SystemStatus> queryAll(String accessDate);
}
