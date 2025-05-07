package com.yuran.hotel.user.utils;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
	
	
    SUCCESS(200,"success"),
    
    USERNAME_PASSWORD_ERROR(501,"usernamePasswordError"),
    
    USERNAME_ERROR(502,"usernameError"),
    
    PASSWORD_ERROR(503, "passwordError"),
    
    NOT_LOGIN(504,"notLogin"),
    
    USERNAME_USED(505,"userNameUsed"),
	
	EMAIL_USED(506,"emailUsed"),
	
	DTO_FIELD_INVALID(507,"dtoFieldInvalid"),
	
	JWT_EXPIRED(508, "jwtExpired"),
	
	JWT_INVALID(509, "jwtInvalid"),
	
	PASSWORD_TOKEN_INVALID(510, "passwordTokenInvalid");
	
	private Integer code;
	private String message;
	
	private ResultCodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
