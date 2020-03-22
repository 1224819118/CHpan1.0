package com.caohao.filepan.interceptor;

import com.caohao.filepan.entity.User;
import com.caohao.filepan.service.UserService;
import com.caohao.filepan.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Component("loginInterceptor")
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(userService);
        /**
         * 加入JWT的验证失败了，后续会继续学习并完善这部分内容
         */
//        String token = request.getHeader("token");
//        Claims claims = JWTUtil.checkJwtToClaims(token);
//        boolean checkToken = JWTUtil.checkToken(claims);
//        if (!checkToken){//如果token不合格
//            response.sendRedirect("/login");
//            return false;
//        }
//        User user = userService.getById((Integer) claims.get("id"));
//        user.setPassword("*********");
//        request.getSession().setAttribute("user",user);
//        return true;
        /**
         * 下面是session验证
         */
        if (request.getSession().getAttribute("user")==null){
            response.sendRedirect("/login");
            return false;
        }
        return true;

    }
}
