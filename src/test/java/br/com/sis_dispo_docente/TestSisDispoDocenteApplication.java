package br.com.sis_dispo_docente;

import org.springframework.boot.SpringApplication;

public class TestSisDispoDocenteApplication {

	public static void main(String[] args) {
		SpringApplication.from(SisDispoDocenteApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
