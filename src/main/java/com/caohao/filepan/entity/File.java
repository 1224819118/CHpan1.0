package com.caohao.filepan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.caohao.filepan.entity.Enum.FileType;
import lombok.Data;

import java.io.Serializable;

/**
 * 这个对象是用于存储所储存的文件的树形结构
 * 储存的是文件的名字和是否是文件夹还有文件储存的uri
 * ，具体的文件存储还是通过文件上传组件来实现
 */
@Data
@TableName("tb_file")
public class File extends Model<File> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //文件类型 0：文件夹 1：文件
    private FileType fileType = FileType.FILEDIR;
    //文件名
    private String fileName = "我的文件";
    //用户id只有最外层的我的文件夹才有这个属性
    Integer userId;
    //存在状态,用于逻辑删除
    @TableLogic(value = "0",delval = "1")
    private int status = 0;
    //文件存储路径,这里可以加上默认的父路径所有的文件都存在这个路径之下
    //文件的树结构也是在这个路径下展现的
    private String url = "/File";
    //父文件夹
    //文件夹id
    private Integer fatherFileId;
    //文件夹对象
    @TableField(exist = false)
    private File fatherFile;
    //子文件
    @TableField(exist = false)
    private File[] chileFiles;

    /**
     * 这个方法是继承了Model类后重写的方法
     * 这个方法是为了mybatisplus的AR操作
     * @return
     */
    @Override
    protected Serializable pkVal() {
        return id;
    }
}
