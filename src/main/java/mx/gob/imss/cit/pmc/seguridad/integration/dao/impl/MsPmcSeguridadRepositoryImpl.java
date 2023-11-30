package mx.gob.imss.cit.pmc.seguridad.integration.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.seguridad.integration.dao.MsPmcSeguridadRepository;
import mx.gob.imss.cit.pmc.seguridad.integration.model.PermisoxPerfilDTO;

@Repository
public class MsPmcSeguridadRepositoryImpl implements MsPmcSeguridadRepository{

	@Autowired
	private MongoOperations mongoOperations;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<PermisoxPerfilDTO> getFuncionalidadesxPerfil(List<String> roles) {
		
		Query query = new Query();
		
		query.addCriteria(Criteria.where("desAdmonUsrs").in(roles));
		List<PermisoxPerfilDTO> find = this.mongoOperations.find(query, PermisoxPerfilDTO.class);
		return find;
	}
	
	
	public List<String> getFuncionalidadesxPerfilStr(List<String> roles) {
		
		Query query = new Query();
		
		query.addCriteria(Criteria.where("desAdmonUsrs").elemMatch(Criteria.where("desAdmonUsrs").in(roles)));
		query.fields().include("desFuncionalidad");
		List<String> string = this.mongoOperations.find(query, String.class);
		return string;
	}
}
