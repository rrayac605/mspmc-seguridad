package mx.gob.imss.cit.pmc.seguridad.integration.dao.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mx.gob.imss.cit.pmc.seguridad.integration.dao.MsPmcSeguridadRepository;
import mx.gob.imss.cit.pmc.seguridad.integration.dao.ParametroRepository;

@SpringBootTest
class MsPmcSeguridadRepositoryImplTest {
	
	@Autowired
	MsPmcSeguridadRepository msPmcSeguridadRepository;
	
	@Autowired
	ParametroRepository parametroRepository;
	
	@Test
	void testPerfiles() {
		
		assertNotNull(parametroRepository);
		
		try {
			List<String> roles = new ArrayList<String>();
			roles.add("DELEGADO");
			roles.add("JEFE DE DEPARTAMENTO DE SUPERVISION DE AFILIACION VIGENCIA");
			roles.add("JEFE DE DEPARTAMENTO AFILIACION VIGENCIA");
			roles.add("JEFE DE OFICINA DE CLASIFICACION DE EMPRESAS");
			
			
			msPmcSeguridadRepository.getFuncionalidadesxPerfil(roles);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
