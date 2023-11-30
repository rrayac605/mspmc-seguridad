package mx.gob.imss.cit.pmc.seguridad.integration.dao;

import java.util.Optional;

import mx.gob.imss.cit.pmc.seguridad.integration.model.ParametroDTO;


public interface ParametroRepository {

	Optional<ParametroDTO> findOneByCve(String cveIdParametro);

}
