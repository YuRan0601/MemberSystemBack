package com.yuran.hotel.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.security.CustomUserDetails;
import com.yuran.hotel.user.service.UserService;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.vo.ChangePasswordRequest;
import com.yuran.hotel.user.vo.UpdateProfileRequest;

import jakarta.annotation.Resource;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@PutMapping("/profile")
	public Result updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody UpdateProfileRequest request) {
		userService.updateUserProfile(userDetails.getUsername(), request);
		return Result.ok(null);
	}
	
	@PutMapping("/password")
	public Result changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody ChangePasswordRequest request) {
		return userService.changePassword(userDetails.getUsername(), request);
	}
}
