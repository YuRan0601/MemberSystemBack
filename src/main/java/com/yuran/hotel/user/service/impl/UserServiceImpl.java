package com.yuran.hotel.user.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.entity.Users;
import com.yuran.hotel.user.repository.UserRepository;
import com.yuran.hotel.user.service.UserService;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.utils.ResultCodeEnum;
import com.yuran.hotel.user.vo.ChangePasswordRequest;
import com.yuran.hotel.user.vo.UpdateProfileRequest;

import jakarta.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto updateUserProfile(String username, UpdateProfileRequest request) {
		//透過username取得user
		Users dbUser = userRepository.findByUsername(username)
		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		//把user的個人資料更改為UpdateProfileRequest的內容
		request.applyUser(dbUser);
		
		Users savedUser = userRepository.save(dbUser);
		
		savedUser.setPassword(null);
		//最後回傳更改成功的user
		return UserDto.toUserDto(savedUser);
	}

	@Override
	public Result changePassword(String username, ChangePasswordRequest request) {
		//透過username取得user
		Users dbUser = userRepository.findByUsername(username)
		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		//PasswordEncoder的matches方法，要把未加密的密碼放在第一個參數才能正常配對
		//若request傳來的舊密碼與dbUser的密碼不一樣，就回傳503 PASSWORD_ERROR的結果
		if(!passwordEncoder.matches(request.getOldPassword(), dbUser.getPassword())) {
			return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
		}
		
		//dbUser的密碼設為新密碼
		dbUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
		
		userRepository.save(dbUser);
		
		//回傳200成功結果
		return Result.ok(null);
	}

	
}
