package com.caohao.filepan;

import com.caohao.filepan.entity.File;
import com.caohao.filepan.service.FileService;
import com.caohao.filepan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FilepanApplicationTests {

    @Autowired
    FileService fileService;

    @Test
    void contextLoads() {
        File byId = fileService.getById(1);
        System.out.println(byId);
    }

}
