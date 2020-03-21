package com.caohao.filepan.util;

import com.caohao.filepan.entity.Enum.FileType;
import com.caohao.filepan.entity.File;
import com.caohao.filepan.entity.User;
import com.caohao.filepan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
@Component
public class MyFileUtil {
    @Autowired
    private static FileService fileService;


    /**
     *通过传递过来的multipartyfile和session对象构建一个新的file对象存在数据库中
     */
    public static File newFileToSaveInDataBase(HttpSession session, MultipartFile targetFile,String src){
        User user = (User) session.getAttribute("user");
        Integer nowFileId = (Integer) session.getAttribute("nowFileId");
        System.out.println("newFileToSaveInDataBase"+nowFileId);
        File finalFile = new File();
        finalFile.setUserId(user.getId());
        finalFile.setFatherFileId(nowFileId);
        finalFile.setFileType(FileType.FILE);
        String originalFileName = targetFile.getOriginalFilename();
        int index = originalFileName.lastIndexOf("\\");
        String fileName="";
        //这里的逻辑是判断获取到的文件名是否带路径，如果有就去掉前面的路径信息
        //因为在测试过程中发现ie浏览器获取到的originalFileName是带有路径的而谷歌的没有路径
        if (index>0){
            fileName = originalFileName.substring(index+1);
        }else {
            fileName = originalFileName;
        }
        if (fileName.indexOf(".")>=0){//说明文件有后缀名

        }else {//说明文件没有后缀名那么就是个文件夹
            finalFile.setFileType(FileType.FILEDIR);
        }
        finalFile.setFileName(fileName);
        finalFile.setUrl(src);

        return finalFile;
    }
    /**
     * 将传递过来的multipartfile存储到传递到指定路径下
     */
    public static void transmitMultipartFileInToTargetSrc(MultipartFile file,String fileName,String targetSrc) throws IOException {
        java.io.File filedir = new java.io.File(targetSrc);
        if (!filedir.exists()){
            filedir.mkdirs();
        }
        java.io.File path = new java.io.File(targetSrc+"//"+fileName);
        file.transferTo(path);
    }
    /**
     * 创建一个新的数据库文件夹对象
     */
    public static File cerateNewFileDIR(HttpSession session,String src,String fileName){
        User user = (User) session.getAttribute("user");
        Integer nowFileId = (Integer) session.getAttribute("nowFileId");
        File finalFile = new File();
        finalFile.setUserId(user.getId());
        finalFile.setFatherFileId(nowFileId);
        finalFile.setFileType(FileType.FILEDIR);
        finalFile.setUrl(src);
        if (fileName==null||fileName.equals("")){
            finalFile.setFileName("新建文件夹");
        }else
            finalFile.setFileName(fileName);
        return finalFile;
    }
    /**
     * 在指定的路径下创建真正的物理文件夹
     */
    public static String createRealFileDIR(String src,String fileName){
        java.io.File fileDir = new java.io.File(src+"\\"+fileName);
        if (fileDir.exists()){
            return "exist";
        }
        boolean mkdirs = fileDir.mkdirs();
        if (mkdirs){
            return "success";
        }else
            return "false";
    }
    /**
     * 文件删除
     */
    public static boolean deleteFileByURI(String targetFileUri){
        boolean flag = false;
        java.io.File targetFile = new java.io.File(targetFileUri);
        if (!targetFile.exists()){
            return flag;
        }
        if (targetFile.isFile()){
            flag = deleteFile(targetFileUri);
        }else {
            flag = deleteFileDirector(targetFileUri);
        }
        return flag;
    }
    /**
     * 删除文件
     */
    public static boolean deleteFile(String path){
        boolean flag = false;
        java.io.File file = new java.io.File(path);
        if (file.isFile()&&file.exists()){
            flag = file.delete();
        }
        return flag;
    }
    /**
     * 删除文件夹包括文件夹下的文件
     * 先判断这个文件是不是文件夹和是否存在
     * 满足以上条件后，获取这个目录下的所有文件和文件夹
     * 遍历文件链表，对每一个文件都判断如果是文件就调用文件删除方法，如果是文件夹就在进入这个文件夹重复上述行为
     */
    public static boolean deleteFileDirector(String path){
        boolean flag = true;
        java.io.File file = new java.io.File(path);
        if (!file.exists()||!file.isDirectory()){
            return false;
        }
        java.io.File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            java.io.File nowFile = files[i];
            if (nowFile.isFile()){//如果是文件
                flag = deleteFile(nowFile.getAbsolutePath());
            }else {//如果是文件夹
                flag = deleteFileDirector(nowFile.getAbsolutePath());//递归
                if (!flag){
                    break;
                }
            }
        }
        if (!flag){
            return false;
        }
        //删除当前目录
        if (file.delete()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 文件下载
     */
    public static ResponseEntity<InputStreamResource> downloadFile(String path) throws IOException {
        java.io.File fileDir = new java.io.File(path);
        if (!fileDir.exists()){
            return null;
        }
        FileSystemResource fileSystemResource = new FileSystemResource(fileDir);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cache-Control","no-cache,no-store,must-revalidate");
        httpHeaders.add("Content-Disposition",String.format("attachment;filename=\"%s\"",fileSystemResource.getFilename()));
        httpHeaders.add("Pragma","no-cache");
        httpHeaders.add("Expires","0");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(fileSystemResource.getInputStream()));

    }

}
