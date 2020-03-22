package com.caohao.filepan.interceptor;

import com.caohao.filepan.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
//        return true;
        if (request.getSession().getAttribute("user")==null){
            response.sendRedirect("/login");
            return false;
        }
        return true;

    }
}
