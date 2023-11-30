package mx.gob.imss.cit.pmc.seguridad.controller;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import mx.gob.imss.cit.mspmccommons.dto.ErrorResponse;
import mx.gob.imss.cit.mspmccommons.enums.EnumHttpStatus;
import mx.gob.imss.cit.mspmccommons.exception.BusinessException;
import mx.gob.imss.cit.pmc.seguridad.integration.model.TokenDTO;
import mx.gob.imss.cit.pmc.seguridad.services.MsPmcSeguridadService;

@RestController
@Api(value = "Seguridad PMC", tags = { "Seguridad PMC Rest" })
@RequestMapping("/msseguridad/v1")
public class MsPmcSeguridadController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String MSG = "mspmcseguridad service ready to return";
	
	@Autowired
	private MsPmcSeguridadService msPmcSeguridadService;
	
    @RequestMapping("/health/ready")
    @ResponseStatus(HttpStatus.OK)
    public void ready() {
    	// Indica que el ms esta listo para recibir peticiones
    }

    @RequestMapping("/health/live")
    @ResponseStatus(HttpStatus.OK)
    public void live() {
    	// Indica que el ms esta vivo
    }
    
    @ApiOperation(value = "Generador token sesion PMC", nickname = "generadorTokenSesion", notes = "Genera el token de la sesion actual", response = Object.class, responseContainer = "Object", tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Respuesta exitosa", response = Object.class, responseContainer = "Object"),
			@ApiResponse(code = 204, message = "Sin resultados", response = Object.class),
			@ApiResponse(code = 500, message = "Describe un error general del sistema", response = Object.class) })
    @CrossOrigin(origins = "*", allowedHeaders="*")
    @PostMapping(value = "/logintoken")
    public Object getToken(/*@ApiModelProperty(required = true, value = "Datos del usuario") @Valid*/
    		 @RequestBody MsPmcSeguridadInput input) throws BusinessException {
    	
    	Object respuesta = null;
    	
        logger.debug("{}", MSG);
        
        Object model;
        
        try {
        	 Object requestSeguridad =  msPmcSeguridadService.getInfoSeguridad(input);
        	 model = requestSeguridad;
        	 if (requestSeguridad!=null) {
     	        respuesta = new ResponseEntity<Object>(model, HttpStatus.OK);
			} else {
				respuesta = new ResponseEntity<Object>(model, HttpStatus.UNAUTHORIZED);
			}
        	
        }
        catch (BusinessException be) {
        	
        	ErrorResponse errorResponse = be.getErrorResponse();
        	
        	int numberHTTPDesired = Integer.parseInt(errorResponse.getCode());
  
            respuesta = new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(numberHTTPDesired));
 
        }
        
        return respuesta;
    }
    
    @ApiOperation(value = "Cierre de sesion PMC", nickname = "cierreSesion", notes = "Cierra la sesion actual", response = Object.class, responseContainer = "Object", tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Respuesta exitosa", response = Object.class, responseContainer = "Object"),
			@ApiResponse(code = 204, message = "Sin resultados", response = Object.class),
			@ApiResponse(code = 500, message = "Describe un error general del sistema", response = Object.class) })
    @CrossOrigin(origins = "*", allowedHeaders="*")
    @PostMapping(value = "/logout")
    public Object logout(@RequestBody TokenDTO input) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
    	
    	Object respuesta = null;
    	
        logger.debug("{}", MSG);
        
        Object model;
        
        try {
        	 Object requestSeguridad =  msPmcSeguridadService.revokeToken(input);
        	 model = requestSeguridad;
     	        respuesta = new ResponseEntity<Object>(model, HttpStatus.OK);
        } catch (BusinessException be) {
        	
        	ErrorResponse errorResponse = be.getErrorResponse();
        	
        	int numberHTTPDesired = Integer.parseInt(errorResponse.getCode());
  
            respuesta = new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(numberHTTPDesired));
 
        }
        
        return respuesta;
    }
	
    @ApiOperation(value = "Actualizar token sesion PMC", nickname = "actualizarTokenSesion", notes = "Actualiza el token de la sesion actual", response = Object.class, responseContainer = "Object", tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Respuesta exitosa", response = Object.class, responseContainer = "Object"),
			@ApiResponse(code = 204, message = "Sin resultados", response = Object.class),
			@ApiResponse(code = 500, message = "Describe un error general del sistema", response = Object.class) })
    @CrossOrigin(origins = "*", allowedHeaders="*")
    @PostMapping(value = "/refresh")
    public Object refreshToken(@RequestBody TokenDTO input) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
    	
    	Object respuesta = null;
    	
        logger.debug("{}", MSG);
        
        Object model;
        
        try {
        	 Object requestSeguridad =  msPmcSeguridadService.refreshToken(input);
        	 model = requestSeguridad;
     	        respuesta = new ResponseEntity<Object>(model, HttpStatus.OK);
        } catch (BusinessException be) {
        	
        	ErrorResponse errorResponse = be.getErrorResponse();
        	
        	int numberHTTPDesired = Integer.parseInt(errorResponse.getCode());
  
            respuesta = new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(numberHTTPDesired));
 
        } catch (HttpClientErrorException httpce) {
        	
            respuesta = new ResponseEntity<ErrorResponse>(new ErrorResponse(EnumHttpStatus.CLIENT_ERROR_FORBIDDEN, httpce.getMessage(), ""), HttpStatus.valueOf(httpce.getRawStatusCode()));
        }
        
        return respuesta;
    }
	
}
