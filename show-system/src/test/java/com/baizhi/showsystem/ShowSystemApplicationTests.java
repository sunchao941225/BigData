package com.baizhi.showsystem;

import com.baizhi.showsystem.dao.SystemStatusDAO;
import com.baizhi.showsystem.entity.SystemStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShowSystemApplicationTests {

    @Autowired
    private SystemStatusDAO systemStatusDAO;

    @Test
    public void contextLoads() {
        List<SystemStatus> list = systemStatusDAO.findAll("2019-10-10");
        System.out.println(list.size());
    }

}
