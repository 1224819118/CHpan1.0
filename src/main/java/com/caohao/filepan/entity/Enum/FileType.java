package com.caohao.filepan.entity.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 这个枚举类是文件类型的枚举类
 * 0：文件夹类   1：文件类
 */
@Getter
public enum FileType {
    FILEDIR(0,"文件夹"),FILE(1,"文件");

    FileType(int code, String descp){
        this.code=code;
        this.descp=descp;
    }

    @EnumValue
    private int code;
    private String descp;
}
