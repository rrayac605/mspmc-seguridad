package mx.gob.imss.cit.pmc.seguridad.integration.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;


@Document(collection = "MCC_PERMISOXPERFIL")
public class PermisoxPerfilDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9205628499167756610L;
	
	@Getter
	@Setter
	private String cveIdFuncionalidad;
	@Getter
	@Setter
	private String desFuncionalidad;
	@Getter
	@Setter
	private String cveIdPerfil;
	@Getter
	@Setter
	private String desPerfil;
	@Getter
	@Setter
	private String desNomenclatura;
	@Getter
	@Setter
	private String desAdmonUsrs;
	@Getter
	@Setter
	private String fecAlta;
	@Getter
	@Setter
	private String fecBaja;
	@Getter
	@Setter
	private String fecActualizacion;
}
