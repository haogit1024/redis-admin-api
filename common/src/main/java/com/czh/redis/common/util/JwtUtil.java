package com.czh.redis.common.util;

import com.czh.redis.common.constants.CommonConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * jwt 工具类
 * @author czh
 * @date 2020/6/12
 */
@Component
@Slf4j
@Data
public class JwtUtil {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.expirationDate}")
    private Integer expirationDate;
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.minRefreshDate}")
    private Integer minRefreshDate;
    @Value("${jwt.headerName}")
    private String headerName;

    public String createJwt(String userId, String digest) {
        // jwt payload 部分
        Map<String, Object> claims = new HashMap<>(1);
        claims.put(CommonConstants.Jwt.JWT_DIGEST, digest);
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusMinutes(expirationDate);
        Date date = LocalDateTimeUtil.convertToDate(localDateTime);
        // jwt 签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setId(userId)
                .setExpiration(date)
                .setIssuedAt(new Date())
                .signWith(generalKey(), signatureAlgorithm)
                .compact();
        // 设置redis
        String redisKey = getRedisKey(userId, digest);
        redisUtil.set(redisKey, jwt, expirationDate, TimeUnit.MINUTES);
        return jwt;
    }

    /**
     * 生成签名key
     * @return
     */
    public SecretKey generalKey() {
        //本地配置文件中加密的密文
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) {
        //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            // 备用方式, 从过期的数据里取得 claims
            log.error("token已过期");
//            claims = ex.getClaims();
            return null;
        } catch (Exception ex) {
            log.error("token解析失败");
            return null;
        }
        return claims;
    }

    public boolean verifyJwtAndRefresh() {
        String jwt = SpringUtil.getRequest().getHeader(this.headerName).toString();
        return this.verifyJwtAndRefresh(jwt);
    }

    /**
     * 验证 jwt 和续签 jwt
     * @param jwt 可以从方法传入, 也可以从request中获取, 方便单元测试
     * @return
     */
    public boolean verifyJwtAndRefresh(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            return false;
        }
        Claims claims = this.parseJWT(jwt);
        log.info("claims: {}", claims);
        if (claims == null) {
            return false;
        }
        String userId = claims.getId();
        String digest = claims.get(CommonConstants.Jwt.JWT_DIGEST, String.class);
        String key = getRedisKey(userId, digest);
        String redisJwt = redisUtil.get(key).toString();
        if (StringUtils.isBlank(redisJwt) || !jwt.equals(redisJwt)) {
            return false;
        }
        long now = System.currentTimeMillis();
        Date expirationDate = claims.getExpiration();
        if ((expirationDate.getTime() - now) / 1000 / 60 < this.minRefreshDate) {
            // 续签 jwt
            String token = createJwt(userId, digest);
            if (SpringUtil.getResponse() != null) {
                SpringUtil.getResponse().addHeader(this.headerName, token);
            }
        }
        if (SpringUtil.getRequest() != null) {
            SpringUtil.getRequest().setAttribute(CommonConstants.Request.REQ_USER_ID, userId);
            SpringUtil.getRequest().setAttribute(CommonConstants.Request.REQ_DIGEST, digest);
        }
        return true;
    }

    public String getRedisKey(String userId, String userUuid) {
        return CommonConstants.RedisKey.REDIS_TOKEN + userId + CommonConstants.Symbol.DOT + userUuid;
    }
}
