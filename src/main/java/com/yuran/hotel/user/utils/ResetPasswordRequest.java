package com.yuran.hotel.user.utils;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	private String password;
	private String token;
}
