package com.caohao.filepan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caohao.filepan.entity.File;
import com.caohao.filepan.entity.User;
import com.caohao.filepan.service.FileService;
import com.caohao.filepan.util.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    FileService fileService;
    private  String fileBasePath;
    @Value("${MyProperties.MyFileBasePath}")
    public void getFileBasePath(String MyFileBasePath){
        fileBasePath = MyFileBasePath;
    }

    /**
     * 转到index页面，并传入参数files
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpSession session, Model model){
        //取出已经登陆的用户信息
        User user = (User) session.getAttribute("user");
        //设置查询条件，按照userid查询主文件夹
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId()).isNull("father_file_id");
        //查出主文件夹，这边用list来储存是想要和后面的子文件list形式一样这样前端页面更容易遍历
        List<File> files = fileService.list(queryWrapper);
        model.addAttribute("files",files);
        //跳到index页面
        return "index";
    }

    /**
     * 当在前端页面点击了某个文件夹时，就会进入这个请求，传递目标文件夹的id作为父文件夹id
     * 查找所以以此文件夹id作为父文件夹的所有文件和文件夹存在list中传递
     * @param fatherFileId
     * @param model
     * @return
     */
    @RequestMapping("/intoFile/{fatherFileId}")
    public String intoFile(@PathVariable Integer fatherFileId, Model model,HttpSession session){
        //设置查询条件，按照父文件夹id来查
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("father_file_id",fatherFileId);
        List<File> files = fileService.list(queryWrapper);
        model.addAttribute("files",files);
        //将当前所在文件夹的id存入session
        session.setAttribute("nowFileId",fatherFileId);
        //跳转到index页面
        return "index";
    }

    /**
     * 上传文件到当前级别目录中
     * 传入的参数：当前目录文件对象的id，
     * 新的文件对象的信息（名字，类型，url，父文件夹id）
     */
    @RequestMapping("/upload")
    public String upload( HttpSession session, @RequestParam("uploadFile") MultipartFile file){
        //从session中取得当前目录的id
        Integer fatherFileId = (Integer) session.getAttribute("nowFileId");
        //通过id找到当前所在文件夹啊的路径
        String srcend = fileService.getPathByFileId(fatherFileId);
        String src = ""+fileBasePath+"\\"+srcend;
        System.out.println("***********************************************"+src);
        //新建一个文件对象
        File newFile = MyFileUtil.newFileToSaveInDataBase(session,file,srcend);
        System.out.println(newFile);
        //将multipartfile文件对象存到指定目录下
        try {
            MyFileUtil.transmitMultipartFileInToTargetSrc(file,newFile.getFileName(),src);
        } catch (IOException e) {
            String message = e.getMessage();
            System.out.println(message);
            return "error";
        }
        //将新建的文件对象存到数据库中
        boolean save = fileService.save(newFile);
        //跳转到当前目录文件展示页面
        return "redirect:/intoFile/"+fatherFileId;
    }
    /**
     * 下载所选文件或者文件夹，如果选择文件夹则下载文件夹下的所有文件
     */
    @RequestMapping("/downLoadFile/{targetFileId}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Integer targetFileId) throws IOException {
        String srcend = fileService.getPathByFileId(targetFileId);
        String src= fileBasePath+"\\"+srcend;
        ResponseEntity<InputStreamResource> inputStreamResourceResponseEntity = MyFileUtil.downloadFile(src);

        return inputStreamResourceResponseEntity;
    }
    /**
     * 删除文件或者文件夹，如果是文件夹则删除目录下的所有文件
     * 这里的删除只是逻辑上的删除真正的删除会通过定时任务，这是为了给人们误删解决问题
     *
     */
    @RequestMapping("/deleteFile/{targetFileId}")
    public String deleteFile(@PathVariable Integer targetFileId){
//        //获取当前数据库存储的文件路径
//        String srcend = fileService.getPathByFileId(targetFileId);
//        //物理路径
//        String src = fileBasePath+"\\"+srcend;
//        String deleteFileByURIStatus = MyFileUtil.deleteFileByURI(src);
        boolean removeById = fileService.removeById(targetFileId);
        if(removeById){
            return "redirect:/index";
        }else {
            return "error";
        }
    }
    /**
     * 新建文件夹
     */
    @RequestMapping("/newFileDIR")
    public String newFileDIR(String fileDirName,HttpSession session,Model model){
        //从session中取得当前目录的id
        Integer fatherFileId = (Integer) session.getAttribute("nowFileId");
        //获取当前数据库存储的文件路径
        String srcend = fileService.getPathByFileId(fatherFileId);
        //物理路径
        String src = fileBasePath+"\\"+srcend;
        System.out.println("------------------------>"+src);
        //通过新的文件夹名称在当前目录创建一个新的文件夹
        String createRealFileDIRString = MyFileUtil.createRealFileDIR(src, fileDirName);
        //通过createRealFileDIRString判断创建新的文件夹的情况
        if ("exist".equals(createRealFileDIRString)){//文件夹已存在
            model.addAttribute("message","文件夹已存在");
        }else if ("false".equals(createRealFileDIRString)){//创建文件夹失败
            model.addAttribute("message","创建文件夹失败");
        }else if ("success".equals(createRealFileDIRString)){//创建文件夹成功
            model.addAttribute("message","创建文件夹成功");
        }
        //将文件夹信息注入到一个新建的file对象中存储到数据库
        File newFileDIR = MyFileUtil.cerateNewFileDIR(session, srcend, fileDirName);
        //将file对象存到数据库
        fileService.save(newFileDIR);
        //回到主页面
        return "redirect:/index";
    }
}
