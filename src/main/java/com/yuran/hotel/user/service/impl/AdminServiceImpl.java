package com.yuran.hotel.user.service.impl;

import org.springframework.stereotype.Service;

import com.yuran.hotel.user.repository.UserRepository;
import com.yuran.hotel.user.service.AdminService;

import jakarta.annotation.Resource;

@Service
public class AdminServiceImpl implements AdminService {
	@Resource
	private UserRepository userRepository;
}
