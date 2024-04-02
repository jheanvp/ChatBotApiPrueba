package com.example.chatbotapiprueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class ChatBotApiPruebaApplication {
	public static void main(String[] args) throws GeneralSecurityException, IOException {

		SpringApplication.run(ChatBotApiPruebaApplication.class, args);
	}
}
