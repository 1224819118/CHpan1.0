package com.caohao.filepan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caohao.filepan.dao.FileDao;
import com.caohao.filepan.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("fileService")
public class FileService extends ServiceImpl<FileDao, File> {
    @Autowired
    FileDao fileDao;


    /**
     * 查看自己的文件以树形结构展示在页面上
     */
    
    /**
     * 上传文件
     */
    /**
     * 下载文件
     */
    /**
     * 删除文件
     */
    /**
     * 通过文件对象的id找到他的路径
     * 这里的逻辑：
     *      因为传过来的id就是被创建的对象所在的文件夹，所有如果所在文件夹是最底层的文件夹的话直接加上所在文件夹url就可以
     *      如果不是最底层的文件夹的话因为除了最底层文件夹以外每个文件夹所储存的url都是他所在的目录，所以要加上这个文件夹的名字
     */
    public  String getPathByFileId(Integer fileId){
        //这里使用stringbuilder来储存文件路径，因为这个对象会重复多次后链接，如果是string则会产生很多无用的string对象浪费空间
        StringBuilder src = new StringBuilder();
        //通过id找到目标文件对象
        File file = fileDao.selectById(fileId);
        //加上所在文件夹的url
        src.append(file.getUrl());
        //判断是不是最外层文件夹
        if(file.getFatherFileId()!=null){//如果不是
            //加上文件夹的名字
            src.append("\\"+file.getFileName());
        }
        System.out.println("getPathByFileId----->"+src.toString());
        return src.toString();
    }
}
