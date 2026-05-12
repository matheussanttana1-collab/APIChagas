package com.example.DoencaChagas;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DoencaChagasApplication {

	public static void main(String[] args) {
		// Em vez de SpringApplication.run, nós iniciamos o ciclo de vida do JavaFX.
		// A classe JavaFxApplication será responsável por inicializar o Spring Boot.
		Application.launch(JavaFxApplication.class, args);
	}
}
