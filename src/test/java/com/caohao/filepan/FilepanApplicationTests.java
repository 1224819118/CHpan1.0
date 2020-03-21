package com.caohao.filepan;

import com.caohao.filepan.service.FileService;
import com.caohao.filepan.service.UserService;
import com.caohao.filepan.util.MyFileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class FilepanApplicationTests {

    @Autowired
    FileService fileService;

    @Test
    void contextLoads() {
        boolean b = MyFileUtil.deleteFileByURI("F:\\FTPFile\\caohao\\11");
        System.out.println(b);
    }

}
