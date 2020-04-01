package com.caohao.filepan;

import com.caohao.filepan.util.MyCacheUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Import(FdfsClientConfig.class)
@SpringBootApplication
@EnableScheduling
@MapperScan("com.caohao.filepan.dao")
public class FilepanApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilepanApplication.class, args);
    }

}
