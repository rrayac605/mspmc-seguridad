package mx.gob.imss.cit.pmc.seguridad.integration.model;

import lombok.Data;

@Data
public class SeguridadResponse {
	
	
	
	ResponseTokenBody tokenBody;
	ResponseTokenInfo tokenInfo;
	ResponseUserInfo userInfo;

}
