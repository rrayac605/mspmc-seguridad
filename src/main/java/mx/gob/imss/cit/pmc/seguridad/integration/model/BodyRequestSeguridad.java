package mx.gob.imss.cit.pmc.seguridad.integration.model;

import lombok.Getter;
import lombok.Setter;

public class BodyRequestSeguridad {
	
	@Getter
	@Setter
	private String grant_type;
	
	@Getter
	@Setter
	private String client_id;
	
	@Getter
	@Setter
	private String client_secret;
	
	@Getter
	@Setter
	private String 	scope;
	
	@Getter
	@Setter
	private String username;
	
	@Getter
	@Setter
	private String password;


}
