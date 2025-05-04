package com.yuran.hotel.user.service;

public interface EmailService {
	void send(String to, String subject, String content);
}
