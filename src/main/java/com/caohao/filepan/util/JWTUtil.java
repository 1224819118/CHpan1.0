package com.caohao.filepan.util;

import com.caohao.filepan.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.util.Date;

/**
 * jwt工具类
 */
public class JWTUtil {
private static final String SUBJECT = "CHpan";
private static final long EXPIRE = 1000*60*60*24*7;//一周
private static final String APPSECRET = "caohao";
private static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * jwt加密方法
     */
    public static String getJSONWebToken(User user){
        if (user.getId()==null||user.getUserName()==null||user.getNickName()==null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id",user.getId())
                .claim("name",user.getUserName())
                .claim("nickName",user.getNickName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
        return token;
    }
    /**
     * jwt解密方法
     */
    public static Claims checkJwtToClaims(String token){
        final Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            String message = e.getMessage();
            logger.info(message);
        }
        return null;
    }
    /**
     * 验证是否过期的方法
     */
    public static boolean isBeforeExpriation(Date expriation){
        boolean before = expriation.before(new Date());
        return before;
    }
    /**
     * 判断是否为合格的token
     */
    public static boolean checkToken(Claims claims){
        if (claims==null){//解密失败才会返回null
            return false;
        }
        if (!claims.getExpiration().before(new Date())){//如果token过时也返回false
            return false;
        }
        if (claims.get("id")==null){//如果这个用户不存在
            return false;
        }
        return true;
    }
}
