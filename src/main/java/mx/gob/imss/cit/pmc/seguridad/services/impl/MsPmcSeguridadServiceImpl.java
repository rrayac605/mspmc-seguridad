package mx.gob.imss.cit.pmc.seguridad.services.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import mx.gob.imss.cit.mspmccommons.exception.BusinessException;
import mx.gob.imss.cit.pmc.seguridad.controller.MsPmcSeguridadInput;
import mx.gob.imss.cit.pmc.seguridad.integration.dao.MsPmcSeguridadRepository;
import mx.gob.imss.cit.pmc.seguridad.integration.dao.ParametroRepository;
import mx.gob.imss.cit.pmc.seguridad.integration.model.ParametroDTO;
import mx.gob.imss.cit.pmc.seguridad.integration.model.ResponseTokenBody;
import mx.gob.imss.cit.pmc.seguridad.integration.model.ResponseTokenInfo;
import mx.gob.imss.cit.pmc.seguridad.integration.model.ResponseUserInfo;
import mx.gob.imss.cit.pmc.seguridad.integration.model.SeguridadResponse;
import mx.gob.imss.cit.pmc.seguridad.integration.model.TokenDTO;
import mx.gob.imss.cit.pmc.seguridad.services.MsPmcSeguridadService;

@Service("msPmcCatalagosServiceImpl")
public class MsPmcSeguridadServiceImpl implements MsPmcSeguridadService {

	@Autowired
	MsPmcSeguridadRepository msPmcSeguridadRepository;
	
	@Autowired
	ParametroRepository parametroRepository;
	
	HashMap<String, Optional<ParametroDTO>> parameters = new HashMap<String, Optional<ParametroDTO>>();
	

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object getInfoSeguridad(MsPmcSeguridadInput input) throws BusinessException{
		
		SeguridadResponse response = new SeguridadResponse();
		if (!validaParameters(parameters)) {
			Optional<ParametroDTO> grant_type = parametroRepository.findOneByCve("grant_type");
			Optional<ParametroDTO> client_id = parametroRepository.findOneByCve("client_id");
			Optional<ParametroDTO> client_secret = parametroRepository.findOneByCve("client_secret");
			Optional<ParametroDTO> scope = parametroRepository.findOneByCve("scope");
			Optional<ParametroDTO> urlToken = parametroRepository.findOneByCve("urlToken");
			Optional<ParametroDTO> urlTokenInfo = parametroRepository.findOneByCve("urlTokenInfo");
			Optional<ParametroDTO> urlUserInfo = parametroRepository.findOneByCve("urlUserInfo");
			Optional<ParametroDTO> urlRevokeToken = parametroRepository.findOneByCve("urlRevokeToken");

			parameters.put("grant_type", grant_type);
			parameters.put("client_id", client_id);
			parameters.put("client_secret", client_secret);
			parameters.put("scope", scope);
			parameters.put("urlToken", urlToken);
			parameters.put("urlTokenInfo", urlTokenInfo);
			parameters.put("urlUserInfo", urlUserInfo);
			parameters.put("urlRevokeToken", urlRevokeToken);
		}
		try {
			
			ResponseTokenBody token = getToken(input, parameters.get("grant_type"), parameters.get("client_id"), parameters.get("client_secret"), parameters.get("scope"), parameters.get("urlToken"));
			ResponseTokenInfo tokenInfo = getTokenInfo(token, parameters.get("urlTokenInfo"));
			ResponseUserInfo userInfo = getUserInfo(tokenInfo, token, parameters.get("urlUserInfo"));			
			
			if(userInfo != null) {
				response.setTokenBody(token);
				response.setTokenInfo(tokenInfo);
				response.setUserInfo(userInfo);
				return response;
			}else {
				return null;
			}			    
				    
		} catch (Exception e) {
			logger.error("",e);
		}
		return response;
	}

	private boolean validaParameters(HashMap<String, Optional<ParametroDTO>> parameters) {
		if (parameters.get("grant_type")==null|| parameters.get("client_id") ==null || parameters.get("client_secret")==null || parameters.get("scope")==null || parameters.get("urlToken")==null
				||parameters.get("urlTokenInfo")==null || parameters.get("urlUserInfo")==null) {
			return false;
		}// TODO Auto-generated method stub
		return true;
	}

	private ResponseUserInfo getUserInfo(ResponseTokenInfo tokenInfo, ResponseTokenBody token, Optional<ParametroDTO> urlUserInfo) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
		RestTemplate restTemplate = getRestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+token.getAccess_token());

		String requestJson=null;
		HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
		ResponseUserInfo response = restTemplate.postForObject(urlUserInfo.get().getDesParametro(), entity, ResponseUserInfo.class);
		//Validacion de sitemas
		logger.info("<--------UsuarioSistemasIMSS-------->");
		logger.info(response.getEmployeeNumber() + "->" + response.getImsssistemas());
		logger.info("<----------------------------------->");
		Optional<ParametroDTO> userImssSistemas = parametroRepository.findOneByCve("user.Imss_Sistemas");
		parameters.put("user.Imss_Sistemas", userImssSistemas);
		String pmc = userImssSistemas.get().getDesParametro();
		//Validacion de perfiles
		logger.info("<--------UsuarioPerfilesIMSS-------->");
		logger.info(response.getEmployeeNumber() + "->" + response.getImssperfiles());
		logger.info("<----------------------------------->");
		Optional<ParametroDTO> userImssPerfiles = parametroRepository.findOneByCve("user.Imss_Perfiles");
		parameters.put("user.Imss_Perfiles", userImssPerfiles);
		String[] perfiles = userImssPerfiles.get().getDesParametro().split(",");
		String[] imssPerfil = response.getImssperfiles().split(",");
		boolean acceso = false;
		for(String perfil : perfiles) {
			Optional<String> valida = Arrays.asList(imssPerfil).stream().filter(p -> p.equals(perfil)).findFirst();
			if(valida.isPresent()) {
				acceso = true;
				break;
			}
		}
		if(response.getImsssistemas().contains(pmc) && acceso) {
			return response;
		}else {
			return null;
		}		
		
	}

	private ResponseTokenInfo getTokenInfo(ResponseTokenBody token, Optional<ParametroDTO> urlTokenInfo) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
		RestTemplate restTemplate = getRestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+token.getAccess_token());

		String requestJson=null;
		HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
		ResponseTokenInfo response = restTemplate.postForObject(urlTokenInfo.get().getDesParametro(), entity, ResponseTokenInfo.class);
		
		return response;
	}

	private ResponseTokenBody getToken(MsPmcSeguridadInput input, Optional<ParametroDTO> grant_type,
			Optional<ParametroDTO> client_id, Optional<ParametroDTO> client_secret, Optional<ParametroDTO> scope,
			Optional<ParametroDTO> urlToken)
			throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		RestTemplate restTemplate = getRestTemplate();
		
		LinkedMultiValueMap<String, String> requestSeuridad = new LinkedMultiValueMap<String, String>();
		requestSeuridad.add("grant_type",grant_type.get().getDesParametro());
		requestSeuridad.add("client_id",client_id.get().getDesParametro());
		requestSeuridad.add("client_secret",client_secret.get().getDesParametro());
		requestSeuridad.add("scope",scope.get().getDesParametro());
		requestSeuridad.add("username",input.getUsrName());
		requestSeuridad.add("password",input.getPassword());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestSeuridad, headers);
		
		ResponseEntity<ResponseTokenBody>  response = restTemplate.exchange(urlToken.get().getDesParametro(), 
				HttpMethod.POST, entity, ResponseTokenBody.class);

		return response.getBody();
	}
	
	public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
	    TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
	        @Override
	        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	            return true;
	        }
	    };
	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
	    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    requestFactory.setHttpClient(httpClient);
	    RestTemplate restTemplate = new RestTemplate(requestFactory);
	    return restTemplate;
	}

	@Override
	public Object revokeToken(TokenDTO input) throws BusinessException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
		RestTemplate restTemplate = getRestTemplate();
		
		

	
		LinkedMultiValueMap<String, String> requestSeuridad = new LinkedMultiValueMap<String, String>();
		requestSeuridad.add("client_id",parameters.get("client_id").get().getDesParametro());
		requestSeuridad.add("client_secret",parameters.get("client_secret").get().getDesParametro());
		requestSeuridad.add("token",input.getRefresh_token());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestSeuridad, headers);
		
		ResponseEntity<ResponseTokenBody>  response = restTemplate.exchange(parameters.get("urlRevokeToken").get().getDesParametro(), 
				HttpMethod.POST, entity, ResponseTokenBody.class);

		return response.getBody();
	}

	@Override
	public Object refreshToken(TokenDTO input)
			throws BusinessException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
		RestTemplate restTemplate = getRestTemplate();
		

		LinkedMultiValueMap<String, String> requestSeuridad = new LinkedMultiValueMap<String, String>();
		requestSeuridad.add("grant_type","refresh_token");
		requestSeuridad.add("client_id",parameters.get("client_id").get().getDesParametro());
		requestSeuridad.add("client_secret",parameters.get("client_secret").get().getDesParametro());
		requestSeuridad.add("refresh_token",input.getRefresh_token());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestSeuridad, headers);
		ResponseEntity<ResponseTokenBody> response=null;
		try {
			response = restTemplate.exchange(parameters.get("urlToken").get().getDesParametro(),
					HttpMethod.POST, entity, ResponseTokenBody.class);
		} catch (HttpClientErrorException httpce) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN , httpce.getMessage()); 
		}

		return response.getBody();
	}

}
