package com.example.chatbotapiprueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ChatBotApiPruebaApplication {


	@GetMapping("/prueba")
	public String mensaje(){
		return "hola te saludo desde azure";
	}


	@GetMapping("/prueba2")
	public String mensaje2(){
		return "hola te saludo desde azure2";
	}
	@GetMapping("/prueba3")
	public String mensaje3(){
		return "hola te saludo desde azure3";
	}

	public static void main(String[] args) {
		SpringApplication.run(ChatBotApiPruebaApplication.class, args);
	}

}
