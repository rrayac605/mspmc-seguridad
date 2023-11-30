package mx.gob.imss.cit.pmc.seguridad.integration.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ResponseUserInfo {
	
	String sub;
	String mail;
	String initials;
	String displayName;
	String givenName;
	String imsssistemas;
	String cn;
	String title;
	String employeeNumber;
	String uid;
	String employeeType;
	String imssperfiles;
	String imssmatricula;
	Integer businessCategory;
	Integer departmentNumber;
	String sn;
	String imssareas;
	
}
