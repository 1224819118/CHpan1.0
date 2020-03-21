package com.caohao.filepan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caohao.filepan.entity.File;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository("fileDao")
public interface FileDao extends BaseMapper<File> {
    @Delete("delete from tb_file where status=1")
    public void confrimDeleteFile();
}
