package com.yuran.hotel.user.vo;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	private String oldPassword;
	private String newPassword;
}
