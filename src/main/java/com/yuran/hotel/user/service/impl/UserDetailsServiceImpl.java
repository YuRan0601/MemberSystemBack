package com.yuran.hotel.user.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yuran.hotel.user.entity.Users;
import com.yuran.hotel.user.repository.UserRepository;
import com.yuran.hotel.user.security.CustomUserDetails;
import jakarta.annotation.Resource;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Resource
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new CustomUserDetails(user);
	}
	
}
