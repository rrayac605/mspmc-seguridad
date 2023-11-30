package mx.gob.imss.cit.pmc.seguridad;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mx.gob.imss.cit.pmc.seguridad.services.MsPmcSeguridadService;

@SpringBootTest
class MsPmcSeguridadApplciationTest {
	
	@Autowired
	MsPmcSeguridadService seguridad;

	@Test
	void contextLoads() {
		assertNotNull(seguridad);
	}

}
