package com.caohao.filepan;

import com.caohao.filepan.service.FileService;
import com.caohao.filepan.service.UserService;
import com.caohao.filepan.util.MyFileUtil;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class FilepanApplicationTests {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Test
    public void RedisTest(){

        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        opsForValue.set("k1","v1");
        Set<String> keys = redisTemplate.keys("*");
        System.err.println(keys.size());
//        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
//        ListOperations<String, String> stringStringListOperations = stringRedisTemplate.opsForList();
//        stringStringValueOperations.set("test","v1",Duration.ofSeconds(30));
//        String s = stringStringValueOperations.get("test", 0, -1);
//        System.out.println(s);
    }

//    @Autowired
//    FileService fileService;
//
//    FastFileStorageClient fastFileStorageClient;

//    @Test
//    void contextLoads() {
//        boolean b = MyFileUtil.deleteFileByURI("F:\\FTPFile\\caohao\\11");
//        System.out.println(b);
//    }

//    @Test
//    public void testFastdfs() throws FileNotFoundException {
//        Set<MataData> mataDataSet = new HashSet<MataData>();
//        mataDataSet.add(new MataData("orginFilename","test.html"));
//        mataDataSet.add(new MataData("author","小曹"));
//        File file = new File("C:\\Users\\ASUS\\Desktop\\test.html");
//        FileInputStream fileInputStream = new FileInputStream(file);
//        StorePath storePath = fastFileStorageClient.uploadFile(fileInputStream, file.length(), "html", mataDataSet);
//        System.out.println(storePath);
//    }

}
