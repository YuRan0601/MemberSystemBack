package com.yuran.hotel.user.vo;

import com.yuran.hotel.user.entity.Users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
	
	@NotBlank
	private String gender;
	
	@NotBlank
	private String email;
	
	public void applyUser(Users user) {
		if(this.gender != null) {
			user.setGender(this.gender);
		}
		
		if(this.email != null) {
			user.setEmail(this.email);
		}
	}
}
