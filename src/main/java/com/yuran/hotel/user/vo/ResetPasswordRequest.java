package com.yuran.hotel.user.vo;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	private String password;
	private String token;
}
