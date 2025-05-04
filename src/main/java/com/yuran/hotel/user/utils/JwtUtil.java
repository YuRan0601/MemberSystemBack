package com.yuran.hotel.user.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
    private String secretKeyString;
    
	@Value("${jwt.expiration-ms}")
    private long jwtExpirationInMs;
    
    
    private SecretKey secretKey;
    
    /**
     * 初始化方法：把secretKeyString透過Keys的方法轉成SecretKey物件
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }
    
    
    /**
     * 用username生成token(登入時使用)
     * @param username
     * @return
     */
    public String generateToken(String username) {
    	
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())	//設定token獲取日期
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) //設定token失效日期(現在+1天)
                .signWith(secretKey) //設定密鑰(傳入Keys物件)
                .compact();
    }
    
    /**
     * 用token提取username
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    
    /**
     * 用token提取token過期時間
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    
    /**
     * 驗證token是否過期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
    	 try {
             return extractExpiration(token).before(new Date());
         } catch (ExpiredJwtException e) {
             return true;
         }
    }
    

    /**
     * 驗證token裡的用戶名與token是否過期
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    
    
    
    /**
     * 提取Claims裡的內容(如username、expiryDate等等)
     * @param <T>
     * @param token
     * @param claimsResolver 調apply(claims)回傳claims.getXxx的內容
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 把token解析成Claims物件
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
