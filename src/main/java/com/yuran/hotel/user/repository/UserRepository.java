package com.yuran.hotel.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuran.hotel.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
	
	boolean existsByUsername(String username);

	Optional<Users> findByUsername(String username);

	Optional<Users> findByEmail(String email);
	
	
	
}
