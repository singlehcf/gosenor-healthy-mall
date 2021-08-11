package com.gosenor.utils;

import com.gosenor.model.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-27 13:59
 */
@Slf4j
public class JWTUtil {
    /**
     * token 过期时间，正常是30天
     */
    private static final long EXPIRE = 1000L * 60L * 60L * 24L * 3L * 10L;

    /**
     * 加密的秘钥
     */
    private static final String SECRET = "gonsenor.net666";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "healthymall";

    /**
     * subject
     */
    private static final String SUBJECT = "gonsenor";


    public static String geneJsonWebToken(LoginUser loginUser){
        if (loginUser == null){
            throw new NullPointerException("loginUser对象为空");
        }
        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("id",loginUser.getId())
                .claim("name",loginUser.getName())
                .claim("head_img",loginUser.getHeadImg())
                .claim("mail",loginUser.getMail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,SECRET).compact();

        token = TOKEN_PREFIX+token;
        return token;
    }

    public static Claims checkJWT(String token){
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
            return claims;
        } catch (Exception e) {
            log.info("jwt token解密失败");
            return null;
        }
    }
}
