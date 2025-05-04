package com.yuran.hotel.user.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.entity.PasswordResetToken;
import com.yuran.hotel.user.entity.TokenBlacklist;
import com.yuran.hotel.user.entity.Users;
import com.yuran.hotel.user.repository.PasswordResetTokenRepository;
import com.yuran.hotel.user.repository.TokenBlacklistRepository;
import com.yuran.hotel.user.repository.UserRepository;
import com.yuran.hotel.user.security.CustomUserDetails;
import com.yuran.hotel.user.service.AuthService;
import com.yuran.hotel.user.service.EmailService;
import com.yuran.hotel.user.utils.JwtUtil;
import com.yuran.hotel.user.utils.LoginRequest;
import com.yuran.hotel.user.utils.ResetPasswordRequest;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.utils.ResultCodeEnum;

import jakarta.annotation.Resource;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private TokenBlacklistRepository tokenBlacklistRepository;
	
	@Resource
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Resource
	private PasswordEncoder passwordEncoder;
	
	@Resource
	private AuthenticationManager authManager;
	
	@Resource
	private JwtUtil jwtUtil;
	
	@Resource
	private EmailService emailService;
	
	@Value("${front-end-url}")
	private String frontEndUrl;
	
	@Override
	public boolean checkUsername(String username) {
		
		return userRepository.existsByUsername(username);
		
	}

	@Override
	public UserDto regist(UserDto userDto) {
		
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		Users user = UserDto.toUsers(userDto);
		
		Users save = userRepository.save(user);
		
		UserDto savedUser = UserDto.toUserDto(save);
		
		savedUser.setPassword(null);
		
		return savedUser;
	}

	@Override
	public Map<String, Object> login(LoginRequest loginRequest) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		response.put("token", jwtUtil.generateToken(userDetails.getUsername()));
		
		response.put("username", userDetails.getUsername());
		
		response.put("role", userDetails.getUser().getRole());
		
		return response;
	}

	@Override
	public void logout(String token) {
		//把登出的用戶攜帶的token加入黑名單
		TokenBlacklist blacklist = new TokenBlacklist();
		
		LocalDateTime expiryDate = 
				jwtUtil.extractExpiration(token)
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		
		blacklist.setToken(token);
		blacklist.setExpiryDate(expiryDate);
		
		tokenBlacklistRepository.save(blacklist);
	}

	@Override
	public Result forgotPassword(String email) {
		//透過email取得User
		Optional<Users> dbUserOptional = userRepository.findByEmail(email);
		
		//如果找不到User就回傳502 USERNAME_ERROR
		if(!dbUserOptional.isPresent()) {
			return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
		}
		
		//取得的User本身
		Users users = dbUserOptional.get();
		
		//使用UUID生成token
		String token = UUID.randomUUID().toString();
		
		//token到期時間為token生成後30分鐘
		LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);
		
		//透過User取得Token
		Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByUser(users);
		
		
		if(optionalToken.isPresent()) {
			//如果User存在PasswordResetToken，就把PasswordResetToken的token與過期時間重設
			PasswordResetToken passwordResetToken = optionalToken.get();
			
			passwordResetToken.setToken(token);
			
			passwordResetToken.setExpiryDate(expiryDate);
			
			passwordResetTokenRepository.save(passwordResetToken);
			
		} else {
			//如果User不存在PasswordResetToken，就新增一個PasswordResetToken
			PasswordResetToken passwordResetToken = new PasswordResetToken();
			
			passwordResetToken.setUser(users);
			passwordResetToken.setToken(token);
			passwordResetToken.setExpiryDate(expiryDate);
			
			passwordResetTokenRepository.save(passwordResetToken);
		}
		
		String resetUrl = frontEndUrl + "/reset-password/" + token;
		
		emailService.send(email, "重設密碼確認信", "請點擊下方連結重設密碼：\n" + resetUrl);
			
		return Result.ok(null);
	}

	@Override
	public Result resetPassword(ResetPasswordRequest req) {
		Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(req.getToken());
		
		if(tokenOptional.isPresent()) {
			PasswordResetToken passwordResetToken = tokenOptional.get();
			
			//token過期：回傳token失效Result
			if(passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
				return Result.build(null, ResultCodeEnum.PASSWORD_TOKEN_INVALID);
			}
			
			//從token裡獲取user，重設密碼並儲存user，最後刪除token
			Users user = passwordResetToken.getUser();
			
			user.setPassword(passwordEncoder.encode(req.getPassword()));
			
			userRepository.save(user);
			
			passwordResetTokenRepository.delete(passwordResetToken);
		} else {
			//無此token：回傳token失效Result
			return Result.build(null, ResultCodeEnum.PASSWORD_TOKEN_INVALID);
		}
		
		return Result.ok(null);
	}
}
