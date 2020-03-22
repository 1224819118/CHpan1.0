package com.caohao.filepan.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.caohao.filepan.entity.User;
import com.caohao.filepan.service.UserService;
import com.caohao.filepan.util.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    private  String fileBasePath;
    @Value("${MyProperties.MyFileBasePath}")
    public void getFileBasePath(String MyFileBasePath){
        fileBasePath = MyFileBasePath;
    }

    /**
     * 跳转登陆页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 登录请求检查 成功：跳到index请求  失败：重定向回login页面
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("/loginCheck")
    public String loginCheck(String userName , String password, HttpSession session,Model model){
        User login = userService.login(userName, password, session);
        if (login==null){
            model.addAttribute("message","用户名或密码错误");
            return "redirect:/login";
        }else {
            User user = (User) session.getAttribute("user");
            return "redirect:/index";
        }
    }
    /**
     * 注销
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        userService.logout(session);
        return "redirect:/login";
    }
    /**
     * 注册新用户
     */
    @RequestMapping("/register")
    public String register(User user){
        User register = userService.register(user);
        //创建该用户所对应的最外层文件夹
        MyFileUtil.createRealFileDIR(fileBasePath,user.getUserName());
        return "redirect:/login";
    }
    /**
     * 修改邮箱
     */
    @RequestMapping("/changeEmial")
    public String changeEmail(String newEmial,HttpSession session){
        User user = (User) session.getAttribute("user");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("emial",newEmial).eq("id",user.getId());
        userService.update(updateWrapper);
        return "redirect:/login";
    }
    /**
     * 修改密码
     */
    @RequestMapping("/changePassword")
    public String changePassword(String newPassword,HttpSession session){
        User user = (User) session.getAttribute("user");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password",newPassword).eq("id",user.getId());
        userService.update(updateWrapper);
        return "redirect:/login";
    }

    /**
     * 跳转到关于本网盘页面
     */
    @RequestMapping("/about")
    public String aboutPan(){
        return "about";
    }
    /**
     * 跳转到个人中心页面
     */
    @RequestMapping("/myInfo")
    public String myInfo(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "myinfo";
    }
    /**
     * 调整转到注册页面
     */
    @RequestMapping("/registerHtml")
    public String registerHtml(){
        return "register";
    }
}
