package mx.gob.imss.cit.pmc.seguridad.services;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import mx.gob.imss.cit.mspmccommons.exception.BusinessException;
import mx.gob.imss.cit.pmc.seguridad.controller.MsPmcSeguridadInput;
import mx.gob.imss.cit.pmc.seguridad.integration.model.TokenDTO;

public interface MsPmcSeguridadService {

	Object getInfoSeguridad(MsPmcSeguridadInput input) throws BusinessException;
	
	Object revokeToken(TokenDTO input) throws BusinessException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException;

	Object refreshToken(TokenDTO input)throws BusinessException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException;

}
