package com.baizhi.showsystem.service;

import com.baizhi.showsystem.dao.SystemStatusDAO;
import com.baizhi.showsystem.entity.SystemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemStatusServiceImpl implements SystemStatusService {

    @Autowired
    private SystemStatusDAO systemStatusDAO;

    @Override
    @Transactional
    public List<SystemStatus> queryAll(String accessDate) {
        return systemStatusDAO.findAll(accessDate);
    }
}
