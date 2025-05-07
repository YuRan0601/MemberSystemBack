package com.yuran.hotel.user.service;

import com.yuran.hotel.user.dto.UserDto;
import com.yuran.hotel.user.utils.Result;
import com.yuran.hotel.user.vo.ChangePasswordRequest;
import com.yuran.hotel.user.vo.UpdateProfileRequest;

public interface UserService {
	/**
	 * 修改會員資料
	 * @param username 會員帳號
	 * @param request UpdateProfileRequest 前端傳來要修改的資料(gender、email)
	 * @return 修改後的user內容(不含密碼)
	 */
	UserDto updateUserProfile(String username, UpdateProfileRequest request);
	
	/**
	 * 會員修改密碼(在已經登入的情況下)
	 * @param username 會員帳號
	 * @param request ChangePasswordRequest 前端傳來的舊密碼與新密碼
	 * @return 修改成功回傳code 200；舊密碼與會員密碼不符，回傳code 503
	 */
	Result changePassword(String username, ChangePasswordRequest request);
}
