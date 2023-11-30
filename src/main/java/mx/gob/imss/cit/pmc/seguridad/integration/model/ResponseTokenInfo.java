package mx.gob.imss.cit.pmc.seguridad.integration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ResponseTokenInfo {
	
	List<String> scope;
	String issuer;
	String audience;
	String expires_in;
	String user_id;
}
