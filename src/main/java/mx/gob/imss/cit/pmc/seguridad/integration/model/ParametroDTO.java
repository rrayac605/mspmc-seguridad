package mx.gob.imss.cit.pmc.seguridad.integration.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "MCC_PARAMETRO")
@JsonPropertyOrder({ "cveIdParametro", "cveIdParametro" })
public class ParametroDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String cveIdParametro;
	@Getter
	@Setter
	private String desParametro;

}
