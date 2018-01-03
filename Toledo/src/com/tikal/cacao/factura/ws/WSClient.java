package com.tikal.cacao.factura.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import localhost.AsignaTimbresEmisorResponse;
import localhost.CancelaCFDIAckResponse;
import localhost.CancelaCFDIResponse;
import localhost.ObtieneCFDIResponse;
import localhost.ObtieneTimbresDisponiblesResponse;
import localhost.RegistraEmisorResponse;
import localhost.TimbraCFDIResponse;

/**
 * <p>Esta clase abstracta contiene los m&eacute;todos para consumir el Web Sevice de Timbrado
 * de CFDI en la version 3.2.</p>
 * 
 * <p>Adem&aacute;s esta clase extiende la clase {@code WebServiceGatewaySupport} para facilitar la forma
 * en que las subclases de {@code WSClient} implementen el consumo del Web Service.</p>
 * 
 * <p>Todos los m&eacute;todos de esta clase regresan un objeto <em>Response</em> especifico. Estos objetos tienen un campo de tipo 
 * {@code ArrayOfAnyType} que a su vez tiene un s&oacute;lo campo <em>anyType</em> de tipo {@code List} cuyos elementos
 * pueden ser de cualquier tipo. El objeto lista <em>anyType</em> contiene la respuesta del web service.</p>
 * 
 * <p>La respuesta entre cada uno de los m&eacute;tos var&iacute; pero su estructura general es:</p>
 * <ul>
 * 	<li>Elemento [0] : devuelve el tipo de excepci&oacute;n en caso de error. Tipo {@code String}</li>
 * 	<li>Elemento [1] : el código de error. Ver el catálogo de errores del proveedor de timbrado (valor 0 en caso de &eacute;xito).
 *    Tipo {@code String} o {@code Integer}</li>
 *  <li>Elemento [2] : Mensaje de respuesta. Puede ser un mensaje de que la operaci&oacute;n se realiz&oacute; con &eacute;xito o
 *   la descripcin del error presentado. Tipo {@code String} </li>
 *  <li>Elemento [3] : el xml de comprobante pero ya timbrado (Cuando se utiliza la operaci&oacute;n de timbrado).
 *   El xml del acuse de cancela&oacute;n (cuando se utiliza la operaci&oacute;n de cancelaci&oacute;n con acuse) Tipo {@code String}</li>
 *  <li>Elemento [4] : un arreglo de bytes que son una imagen. Esta imagen es el QRCode o C&oacute;digo Bidimensional que va 
 *  impreso en la representaci&oacute;n impresa del comprobante. Tipo <em>byte[]<em></li>
 *  <li>Elemento [5] : una cadena que es el sello digital del comprobante y que va impreso en la representaci&oacute;n impresa del comprobante. 
 *   Tipo {@code String}</li>
 * </ul>
 * 
 * @author Tikal
 *
 */
public abstract class WSClient extends WebServiceGatewaySupport {

	/**
	 * <p>&Eacute;ste m&eacute;todo registra un emisor en la plataforma de Integradores del web service de Timbrado.</p>
	 * 
	 * <p>Permite al emisor (empresa) con el RFC especificado mandar a timbrar sus facturas.</p>
	 * 
	 * <p>Regresa un objeto {@code RegistraEmisorResponse} en cuyo estado contiene la respuesta del web service de 
	 * Timbrado. El campo <em>registraEmisorResult</em> ti</p>
	 * 
	 * @param rfcEmisor el RFC de la empresa qu quiere utilizar el servicio de Timbrado
	 * @param pass la contrase&ntilde;a con la que la empresa se registro ante el SAT
	 * @param cer un objeto {@code ByteArrayInputStream} con el contenido del archivo del certificado de seguridad (cer) del emisor 
	 * @param ker un objeto {@code InputStream} con el contenido del archivo key (llave) asociado con el archivo cer
	 * @return un objeto {@code RegistraEmisorResponse} con la respuesta del web service
	 */
	public abstract RegistraEmisorResponse getRegistraEmisorResponse(String rfcEmisor, String pass, ByteArrayInputStream cer, InputStream ker);

	/**
	 * <p>&Eacute;ste m&eacute;todo manda a timbrar la cadena codificada del xml del comprobante</p>
	 * @param xmlComprobanteBase64 la cadena xml del comprobante codificada en <em>Base64</em>
	 * @return un objeto {@code TimbraCFDIResponse} con el xml del comprobante timbrado
	 */
	public abstract TimbraCFDIResponse getTimbraCFDIResponse(String xmlComprobanteBase64);
	
	/**
	 * <p>&Eacute;ste m&eacute;todo manda una solicitud de cancelaci&oacute;n de un comprobante timbrado.</p>
	 * <p>La respuesta de &eacute;ste m&eacute;todo NO CONTIENE el acuse de cancelaci&oacute;n</p>
	 * @param uuid el identificador del comprobante a cancelar
	 * @param rfcEmisor el RFC del emisor del comprobante a cancelar
	 * @return un objeto {@code CancelaCFDIResponse} con la respuesta del servicio de cancelaci&oacute;n
	 */
	public abstract CancelaCFDIResponse getCancelaCFDIResponse(String uuid, String rfcEmisor);
	
	/**
	 * <p>&Eacute;ste m&eacute;todo manda una solicitud de cancelaci&oacute;n CON ACUSE de un comprobante timbrado.</p>
	 * <p>El acuse de cancelaci&oacute;n viene en la lista de elementos del objeto <em>anyType</em> que esta dentro del
	 * objeto {@code CancelaCFDIAckResponse} que se regresa.</p> 
	 * @param uuid el identificador del comprobante a cancelar
	 * @param rfcEmisor el RFC del emisor del comprobante a cancelar
	 * @return un objeto {@code CancelaCFDIAckResponse} con el acuse de cancelaci&oacute;n
	 */
	public abstract CancelaCFDIAckResponse getCancelaCFDIAckResponse(String uuid, String rfcEmisor);
	
	/**
	 * <p>&Eacute;ste m&eacute;todo regresa el comprobante con el <em>uuid</em> y <em>rfcEmisor</em> especificados</p> 
	 * @param uuid el identificador del comprobante a consultar
	 * @param rfcEmisor el RFC del emisor del comprobante a consultar
	 * @return un objeto {@code ObtieneCFDIResponse} con el comprobante a consultar
	 */
	public abstract ObtieneCFDIResponse getObtieneCFDIResponse(String uuid, String rfcEmisor);
	
	/**
	 * <p>Consulta global de los timbres disponibles.</p>
	 * <p>El objeto que regresa &eacute;ste m&eacute;todo contiene en su objeto lista <em>anyType</em> un elemento
	 * de tipo arreglo de int con la cantidad de timbres contratados, timbres consumidos y timbres disponibles<p>
	 * @return un objeto {@code ObtieneTimbresDisponiblesResponse} con la cantidad global de timbres disponibles
	 */
	public abstract ObtieneTimbresDisponiblesResponse getObtieneTimbresDisponiblesResponse();
	
	/**
	 * <p>Consulta por emisor de la cantidad de timbres disponibles.</p>
	 * <p>El objeto que regresa &eacute;ste m&eacute;todo contiene en su objeto lista <em>anyType</em> un elemento
	 * de tipo arreglo de int con la cantidad de timbres contratados, timbres consumidos y timbres disponibles<p>
	 * @param rfcEmisor el RFC del emisor del cual se quiere consultar la cantidad de timbres disponibles
	 * @return un objeto {@code ObtieneTimbresDisponiblesResponse} con la cantidad de timbres disponibles del emisor 
	 * cuyo RFC es el especificado
	 */
	public abstract ObtieneTimbresDisponiblesResponse getObtieneTimbresDisponiblesResponsePorEmisor(String rfcEmisor);
	
	/**
	 * <p>&Eacute;ste m&eacute;todo asigna la cantidad de timbres especificada al emisor con el RFC especificado.</p>
	 * @param rfcEmisor el RFC del emisor al que se le quieren asignar los timbres
	 * @param numTimbres la cantidad de timbres que se quieren asignar
	 * @return un objeto {@code AsignaTimbresEmisorResponse} con la respuesta de la operaci&oacute;n de asignaci&oacute;n
	 */
	public abstract AsignaTimbresEmisorResponse getAsignaTimbresEmisorResponse(String rfcEmisor, int numTimbres);
}
