package com.yuran.hotel.user.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.security.CustomUserDetails;
import com.yuran.hotel.user.utils.LoginRequest;
import com.yuran.hotel.user.utils.ResetPasswordRequest;
import com.yuran.hotel.user.utils.Result;

import jakarta.validation.Valid;

public interface AuthService {
	
	boolean checkUsername(String username);

	UserDto regist(@Valid UserDto userDto);


	/**
	 * 登出的同時把token加入黑名單
	 * @param token
	 */
	void logout(String token);

	Map<String, Object> login(@Valid LoginRequest loginRequest);

	Result forgotPassword(String email);

	Result resetPassword(ResetPasswordRequest req);
	

}
