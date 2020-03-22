package com.caohao.filepan.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caohao.filepan.dao.FileDao;
import com.caohao.filepan.dao.UserDao;
import com.caohao.filepan.entity.File;
import com.caohao.filepan.util.MyCacheUtil;
import com.caohao.filepan.util.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class ScheduConfig {
    @Autowired
    FileDao fileDao;
    @Autowired
    UserDao userDao;
    private  String fileBasePath;
    @Value("${MyProperties.MyFileBasePath}")
    public void getFileBasePath(String MyFileBasePath){
        fileBasePath = MyFileBasePath;
    }
    /**
     * 定时任务，每个月的2号都会执行一次定时的清楚任务
     * 这个任务会将状态为已删除的文件删除掉，在这之前人们是可以恢复上个月删除的文件的
     */
    @Scheduled(cron = "0 0 2 2 * ?")//定义为每个月的二号的凌晨2点执行
    public void ScheduedDeleteFileTask(){
        fileDao.confrimDeleteFile();
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<File> files = fileDao.selectList(queryWrapper);
        for (File file:files) {
            String path = fileBasePath+"\\"+file.getUrl();
            MyFileUtil.deleteFileByURI(path);
        }

    }
    /**
     * 定时的更新缓存，并且在项目一开始启动时自动更新缓存
     */
    @Scheduled(cron = "0 0 0 */10 * *")//每十天在当天的0点更新一次
    public void ScheduedUpdateMyCache(){
        //初始化用户缓存
        MyCacheUtil.updateUserCache(userDao);
    }
}
