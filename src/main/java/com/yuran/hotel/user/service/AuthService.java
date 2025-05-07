package com.yuran.hotel.user.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.security.CustomUserDetails;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.vo.LoginRequest;
import com.yuran.hotel.user.vo.ResetPasswordRequest;

import jakarta.validation.Valid;

public interface AuthService {
	/**
	 * users表有找到此username的資料，就回傳true，否則回傳false
	 * @param username
	 * @return
	 */
	boolean checkUsername(String username);

	/**
	 * 註冊Service
	 * @param userDto
	 * @return
	 */
	UserDto regist(@Valid UserDto userDto);


	/**
	 * 登出的同時把token加入黑名單
	 * @param token
	 */
	void logout(String token);

	/**
	 * 登入Service
	 * @param loginRequest
	 * @return
	 */
	Map<String, Object> login(@Valid LoginRequest loginRequest);
	
	/**
	 * 忘記密碼Service
	 * @param email
	 * @return
	 */
	Result forgotPassword(String email);

	/**
	 * 重設密碼Service
	 * @param req
	 * @return
	 */
	Result resetPassword(ResetPasswordRequest req);
	

}
