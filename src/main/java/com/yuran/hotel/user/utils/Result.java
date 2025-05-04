package com.yuran.hotel.user.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {
	
	//響應碼
	private Integer code;
	
	//響應訊息
	private String message;
	
	//攜帶資料
	private T data;
	
	/**
	 * 建立一個攜帶資料的Result
	 * @param <T> 資料的型態
	 * @param data 攜帶的資料
	 * @return
	 */
	protected static <T> Result<T> build(T data) {
		Result<T> result = new Result<T>();

		if(data != null) {
			result.setData(data);
		}
		
		return result;
	}
	
	/**
	 * 建立一個Result
	 * @param <T> 回傳的資料的型態
	 * @param body 回傳的資料
	 * @param code 響應碼
	 * @param message 響應碼訊息
	 * @return
	 */
	public static <T> Result<T> build(T body, Integer code, String message) {
		Result<T> result = build(body);
		result.setCode(code);
		result.setMessage(message);
		return result;
	}
	
	/**
	 * 透過ResultCodeEnum建立Result
	 * @param <T> 回傳的資料的型態
	 * @param body 回傳的資料
	 * @param resultCodeEnum 設定好code、message的ResultCodeEnum
	 * @return
	 */
	public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
		Result<T> result = build(body);
		result.setCode(resultCodeEnum.getCode());
		result.setMessage(resultCodeEnum.getMessage());
		return result;
	}
	
	/**
	 * 建立一個響應碼為200的Result
	 * @param <T> 回傳的資料的型態
	 * @param data 回傳的資料
	 * @return
	 */
	public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
	}
}
