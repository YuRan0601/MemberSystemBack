package com.yuran.hotel.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	private String token;
	
	@OneToOne()
	@JoinColumn(name = "user_id")
	private Users user;
	
	@Column(name = "expiry_date")
	private LocalDateTime expiryDate;
}
