package com.yuran.hotel.user.service.impl;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuran.hotel.user.repository.TokenBlacklistRepository;
import com.yuran.hotel.user.service.BlacklistCleanupService;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

@Service
public class BlacklistCleanupServiceImpl implements BlacklistCleanupService {
	
	@Resource
	private TokenBlacklistRepository tokenBlacklistRepository;

	@Override
	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void cleanExpiredTokens() {
		LocalDateTime now = LocalDateTime.now();
		int count = tokenBlacklistRepository.deleteAllByExpiryDateBefore(now);
		System.out.println("[BlacklistCleanup] 已清除 " + count + " 筆過期的黑名單token");
	}
	
}
