package com.baizhi.showsystem.controller;

import com.baizhi.showsystem.entity.SystemStatus;
import com.baizhi.showsystem.service.SystemStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SystemStatusController {

    @Autowired
    private SystemStatusService systemStatusService;

    @GetMapping("/status") // @RequestMapping  + GET请求方式
    public List<Map<String, Object>> querySystemStatus(){

        List<SystemStatus> systemStatuses = systemStatusService.queryAll("2019-10-10");

        List<Map<String,Object>> result = new ArrayList<>();
        for (SystemStatus systemStatus : systemStatuses) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name",systemStatus.getStatusCode());
            map.put("y",systemStatus.getNum());
            // 如果饼图的扇区为200，默认选中
            if(systemStatus.getStatusCode() == 200) {
                map.put("selected",true);
                map.put("sliced",true);
            }
            result.add(map);
        }
        return result;
    }

}
