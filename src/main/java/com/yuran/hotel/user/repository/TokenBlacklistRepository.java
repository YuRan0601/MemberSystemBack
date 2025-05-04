package com.yuran.hotel.user.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuran.hotel.user.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Integer> {
	boolean existsByToken(String token);
	
	int deleteAllByExpiryDateBefore(LocalDateTime time);
}
