package com.yuran.hotel.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuran.hotel.user.entity.PasswordResetToken;
import com.yuran.hotel.user.entity.Users;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

	Optional<PasswordResetToken> findByUser(Users users);

	Optional<PasswordResetToken> findByToken(String string);

}
