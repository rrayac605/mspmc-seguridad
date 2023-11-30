package mx.gob.imss.cit.pmc.seguridad.integration.model;

import lombok.Getter;
import lombok.Setter;

public class ResponseType {
	
	@Getter
	@Setter
	private String access_token;
	

	@Getter
	@Setter
	private String token_type;

	
	@Getter
	@Setter
	private String expires_in;

	
	@Getter
	@Setter
	private String scope;
}
