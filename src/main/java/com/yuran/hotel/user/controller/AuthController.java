package com.yuran.hotel.user.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.security.CustomUserDetails;
import com.yuran.hotel.user.service.AuthService;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.utils.ResultCodeEnum;
import com.yuran.hotel.user.vo.LoginRequest;
import com.yuran.hotel.user.vo.ResetPasswordRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	
	/**
	 * 確認username是否已被使用
	 * @param username
	 * @return
	 */
	@GetMapping("name/{username}") 
	public Result checkUsername(@PathVariable @NotBlank String username) {
		if(authService.checkUsername(username)) {
			return Result.build(null, ResultCodeEnum.USERNAME_USED);
		}
		
		return Result.ok(null);
	}
	
	/**
	 * 註冊用戶
	 * @param userDto
	 * @return
	 */
	@PostMapping("regist")
	public Result regist(@RequestBody @Valid UserDto userDto) {
		if(authService.checkUsername(userDto.getUsername())) {
			return Result.build(null, ResultCodeEnum.USERNAME_USED);
		}
		
		UserDto registUser = authService.regist(userDto);
		
		return Result.ok(registUser);
	}
	
	/**
	 * 用戶登入
	 * @param userDto
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PostMapping("login")
	public Result<Map<String, Object>> login(@RequestBody @Valid LoginRequest loginRequest) throws JsonProcessingException {
		
		try {
			Map<String, Object> response = authService.login(loginRequest);
			System.out.println(response);
			Result<Map<String, Object>> result = Result.ok(response);
			return result;
		} catch (BadCredentialsException  e) {
			return Result.build(null, ResultCodeEnum.USERNAME_PASSWORD_ERROR);
		}
	}
	
	/**
	 * 獲取目前登入的用戶
	 * @param authentication
	 * @return
	 */
	@GetMapping("me")
	public Result getCurrentUser(Authentication authentication) {
		if(authentication == null) {
			return Result.build(null, ResultCodeEnum.NOT_LOGIN);
		}
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		UserDto userDto = new UserDto();
		
		userDto.setUsername(userDetails.getUsername());
		userDto.setRole(userDetails.getUser().getRole());
		userDto.setEmail(userDetails.getUser().getEmail());
		userDto.setGender(userDetails.getUser().getGender());
		
		return Result.ok(userDto);
	}
	
	
	/**
	 * 用戶登出
	 * @param authHeader
	 * @return
	 */
	@GetMapping("logout")
	public Result logout(@RequestHeader("Authorization") String authHeader) {
		
		String token = authHeader.substring(7);
		
		authService.logout(token); //把token加入黑名單表
		
		return Result.ok(null);
	}
	
	/**
	 * 透過email找尋用戶，生成驗證token，寄出重設密碼信件
	 * @param email
	 * @return
	 */
	@PostMapping("forgot-password/{email}")
	public Result forgotPassword(@PathVariable String email) {
		return authService.forgotPassword(email);
	}
	
	/**
	 * 通過攜帶的驗證token，修改密碼
	 * @param req 內含token、新密碼
	 * @return
	 */
	@PostMapping("reset-password")
	public Result resetPassword(@RequestBody ResetPasswordRequest req) {
		return authService.resetPassword(req);
	}
}
