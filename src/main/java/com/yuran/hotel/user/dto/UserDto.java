package com.yuran.hotel.user.dto;

import java.time.LocalDateTime;

import com.yuran.hotel.user.entity.Users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private Integer id;

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String gender;
	
	@NotBlank
	private String email;
	
	private String role;
	
	private LocalDateTime createdAt;
	
	public static UserDto toUserDto(Users user) {
		return new UserDto(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getGender(),
				user.getEmail(),
				user.getRole(),
				user.getCreatedAt()
				);
	}
	
	public static Users toUsers(UserDto userDto) {
		return new Users(
				userDto.getId(),
				userDto.getUsername(),
				userDto.getPassword(),
				userDto.getGender(),
				userDto.getEmail(),
				userDto.getRole(),
				userDto.getCreatedAt()
				);
	}
}
