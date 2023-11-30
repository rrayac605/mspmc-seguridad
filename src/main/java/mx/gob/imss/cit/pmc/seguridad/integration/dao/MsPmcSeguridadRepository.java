package mx.gob.imss.cit.pmc.seguridad.integration.dao;

import java.util.List;

import mx.gob.imss.cit.pmc.seguridad.integration.model.PermisoxPerfilDTO;

public interface MsPmcSeguridadRepository {

	List<PermisoxPerfilDTO> getFuncionalidadesxPerfil(List<String> roles);


}
