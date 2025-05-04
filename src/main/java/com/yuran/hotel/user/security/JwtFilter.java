package com.yuran.hotel.user.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuran.hotel.user.repository.TokenBlacklistRepository;
import com.yuran.hotel.user.service.impl.UserDetailsServiceImpl;
import com.yuran.hotel.user.utils.JwtUtil;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.utils.ResultCodeEnum;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter extends OncePerRequestFilter {
	@Resource
	private ObjectMapper objectMapper;
	
	@Resource
	private JwtUtil jwtUtil;
	
	@Resource
	private UserDetailsServiceImpl userDetailsService;
	
	@Resource
	private TokenBlacklistRepository tokenBlacklistRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//取出前端請求Header中的Authorization屬性
		String authHeader = request.getHeader("Authorization");
		
		//Token會以Bearer 開頭
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7); //刪去Bearer ，取出token
			
			if(tokenBlacklistRepository.existsByToken(token)) {
			    response.setStatus(HttpStatus.UNAUTHORIZED.value());
			    response.setCharacterEncoding("UTF-8");
			    response.setContentType("application/json");
			    String json = objectMapper.writeValueAsString(Result.build(null, ResultCodeEnum.JWT_INVALID));
			    response.getWriter().write(json);
			    return;
			}
			
			try {
				
				String username = jwtUtil.extractUsername(token); //透過token提取username，若token過期會拋出ExpiredJwtException
				
				
				if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					if(jwtUtil.validateToken(token, userDetails)) {
						
						UsernamePasswordAuthenticationToken authToken = 
								new UsernamePasswordAuthenticationToken(
										userDetails, 
										null, 
										userDetails.getAuthorities()
										);
						
						
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						
						/* 下面這行設定之後，就可以直接在controller方法的參數裡使用
						 * @AuthenticationPrincipal CustomUserDetails user
						 * 來獲取登入者資料
						 */
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
				
			} catch(ExpiredJwtException e) {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			    response.setCharacterEncoding("UTF-8");
			    response.setContentType("application/json");
			    String json = objectMapper.writeValueAsString(Result.build(null, ResultCodeEnum.JWT_EXPIRED));
			    response.getWriter().write(json);
			    return;
			} catch (JwtException e) {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			    response.setCharacterEncoding("UTF-8");
			    response.setContentType("application/json");
			    String json = objectMapper.writeValueAsString(Result.build(null, ResultCodeEnum.JWT_INVALID));
			    response.getWriter().write(json);
			    return;
			}
			
		}
		
		filterChain.doFilter(request, response);
	}
	
	
}
