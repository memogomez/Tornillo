/**
 * 
 */
package com.tikal.toledo.util;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.tikal.toledo.facturacion.FormatoFecha;
import com.tikal.toledo.sat.cfd.Comprobante;



/**
 * @author Tikal
 *
 */
public class Util {

	/**
	 * Convierte la fecha en el formato "<tt>yyyy-MM-dd'T'HH:mm:ss.SSSZ</tt>" a su representaci&oacute;n en {@code java.util.Date}
	 * @param cadenaFecha fecha con formato "<tt>yyyy-MM-dd'T'HH:mm:ss.SSSZ</tt>"
	 * @return la fecha convertida en un objeto {@code java.util.Date} 
	 */
	public static Date obtenerFecha(String cadenaFecha) {
		DateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		//TODO suposición de que en App Engine esta línea causa error formato.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
		Date fechaIngreso = new Date();
		try {
			fechaIngreso = formato.parse(cadenaFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fechaIngreso;
	}
	
	/**
	 * Convierte y regresa la cadena especificada en un objeto {@code java.util.Date} utilizando el formato
	 * especificado
	 * @param cadenaFecha la representaci&oacute;n en {@code String} de un objeto {@code java.util.Date}
	 * @param formato el formato que se utiliza para analizar gramaticalmente la cadena especificada
	 * @return la cadena especificada convertida en un objeto {@code java.util.Date}
	 */
	public static Date obtenerFecha(String cadenaFecha, DateFormat formato) {
		formato.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
		Date fecha = null;
		cadenaFecha = cambiarNombreDelMes(cadenaFecha);
		try {
			fecha = formato.parse(cadenaFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fecha;
	}
	
	/**
	 * 
	 * @param fecha Fecha a la que se le dará el formato
	 * @return una representaci&oacute;n del la fecha en objeto String
	 */
	public static String regresaFechaConFormato(Date fecha) {
		String cadenaFecha = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		cadenaFecha = df.format(fecha);
		return cadenaFecha;
	}
	
	public static String cambiarNombreDelMes(String strFecha) {
		String mes = strFecha.substring(0, 3);
		switch (mes) {
		case "Jan":
			strFecha = strFecha.replace(mes, "Ene");
			break;
		case "Apr":
			strFecha = strFecha.replace(mes, "Abr");
			break;
		case "Aug":
			strFecha = strFecha.replace(mes, "Ago");
			break;
		case "Dec":
			strFecha = strFecha.replace(mes, "Dic");
			break;
		}
	    return strFecha;
	}
	
	
	
	/**
	 * Este m&eacute;todo regresa la cantidad de d&iacute;as entre la fecha
	 * especificada y la fecha actual
	 * @param fecha 
	 * @return la cantidad de d&iacute;as entre la fecha indicada y la fecha actual
	 */
	// agregar parametro 'int i' para efect
	public static int obtenerDiasEntre(Date fecha) { //, int i) 
	
		Calendar presente = Calendar.getInstance();
//		if (i >= 2) {
//			presente = new GregorianCalendar();
//			//presente.setTimeInMillis(1_492_268_400_000L); // 15-abril-2017
//			presente.setTimeInMillis(1_493_564_400_000L);
//		} else {
//			presente = Calendar.getInstance();
//		}
		//Calendar presente = new GregorianCalendar(2017, 4, 15);
	    
		Calendar pasado = Calendar.getInstance();
	    pasado.setTime(fecha);

	    int dias = 0;

	    while (pasado.before(presente)) {
	        pasado.add(Calendar.DATE, 1);
	        if (pasado.before(presente)) {
	            dias++;
	        }
	    } return dias;
	}
	
	public static int obtenerDiasEntre(Date fechaIn, Date fechaFin) {
		Calendar cFechaFin = Calendar.getInstance();
		Calendar cFechaIn = Calendar.getInstance();
	    cFechaIn.setTime(fechaIn);
	    cFechaFin.setTime(fechaFin);

	    int dias = 0;

	    while (cFechaIn.before(cFechaFin)) {
	        cFechaIn.add(Calendar.DATE, 1);
	        if (cFechaIn.before(cFechaFin)) {
	            dias++;
	        }
	    } return dias;
	}
	
	public static int obtenerMesesEntre(Date fechaIn, Date fechaFin) {
		Calendar cFechaFin = Calendar.getInstance();
		Calendar cFechaIn = Calendar.getInstance();
	    cFechaIn.setTime(fechaIn);
	    cFechaFin.setTime(fechaFin);
	    
	    int meses = 0;
	    while (cFechaIn.before(cFechaFin)) {
	    	cFechaIn.add(Calendar.MONTH, 1);
	    	if (cFechaIn.before(cFechaFin)) {
	    		meses++;
	    	}
	    }
		return meses;
	}
	
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	
	public static String randomString( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		}
	
	public static BigDecimal redondearBigD(double cantidad) {
		BigDecimal bigD = BigDecimal.valueOf(cantidad).setScale(2, RoundingMode.HALF_UP);
		return bigD;
	}
	
	/**
	 * &Eacute;ste m&eacute;todo regresa el n&uacute;mero decimal especificado con un redondeo a 
	 * dos valores a la derecha del punto decimal.
	 * @param cantidad la cantidad a redonear
	 * @return la cantidad con solo dos valores a la derecha del punto decimal
	 */
	public static double redondear(double cantidad) {
		return redondearBigD(cantidad).doubleValue();
	}
	
	public static double truncar(double cantidad) {
		BigDecimal bigD = BigDecimal.valueOf(cantidad).setScale(0, RoundingMode.DOWN);
		return bigD.doubleValue();
	}
	
	public static Date regresarFecha(int dia){
        int fechin = dia;
        String nueva = "";
        Date fechaPago = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();
        String fecha = df.format(now).toString();
        String febrero = fecha.substring(3,5);
        if(fechin > 28 && febrero.equals("02")){
            nueva = fecha.replace(fecha.substring(0,2),Integer.toString(28));
        }else {
            nueva = fecha.replace(fecha.substring(0, 2), Integer.toString(fechin));
        }
        try {
            fechaPago = df.parse(nueva);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaPago;
    }
	
	/**
	 * &Eacute;ste m&eacute;todo regresa un objeto {@code Date} representando
	 * el momento actual del mes que resulte de restar a &eacute;ste, el n&uacute;mero de meses
	 * especificado.
	 * @param meses n&uacute;mero de meses que se restar&aacute;n de la fecha actual
	 * @return un objeto {@code Date} que resulte de restar al momento actual el n&uacute;mero de meses
	 */
	public static Date obtenerFechaMesesAntes(int meses) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -meses);
		return calendar.getTime();
	}
	
	public static String regresaTextoOCadenaVacia(String texto) {
		if (texto == null) {
			texto = "";
		}	
		return texto;
	}
	
	public static XMLGregorianCalendar getXMLDate(Date date, FormatoFecha formato) {
    	GregorianCalendar c = new GregorianCalendar();
    	c.setTime(date);
    	XMLGregorianCalendar date2 = null;
    	try {
	    	switch (formato) {
			case COMPROBANTE:
				date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(
						c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH),
						c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
						DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
				break;
			case NOMINA:
				date2 = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
						c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
				break;
			default:
				break;
			}
    	} catch (DatatypeConfigurationException exception) {
    		return date2;
    	}
    	 
    	return date2;
    }
	
	
	public static String marshallComprobante(Comprobante c) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Comprobante.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		
			jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,	
				"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd ");
		
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(c, sw);
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
