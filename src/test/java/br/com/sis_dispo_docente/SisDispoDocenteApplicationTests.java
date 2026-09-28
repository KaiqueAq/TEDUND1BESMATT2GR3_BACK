package br.com.sis_dispo_docente;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SisDispoDocenteApplicationTests {

	@Test
	void contextLoads() {
	}

}
