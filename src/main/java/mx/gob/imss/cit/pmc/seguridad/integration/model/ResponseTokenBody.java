package mx.gob.imss.cit.pmc.seguridad.integration.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Getter;
import lombok.Setter;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ResponseTokenBody {
	
	@Getter
	@Setter
	String access_token;
	
	@Getter
	@Setter
	String token_type;
	
	@Getter
	@Setter
	String expires_in;
	
	@Getter
	@Setter
	String refresh_token;
	
	@Getter
	@Setter
	String scope;

}
