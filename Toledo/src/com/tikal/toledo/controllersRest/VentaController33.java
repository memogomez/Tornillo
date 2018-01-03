package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.cacao.dao.FacturaVttDAO;
import com.tikal.cacao.factura.RespuestaWebServicePersonalizada;
import com.tikal.cacao.sat.cfd33.Comprobante;
import com.tikal.cacao.service.FacturaVTTService;
import com.tikal.cacao.springController.viewObjects.v33.ComprobanteVO;
import com.tikal.toledo.dao.AlertaDAO;
import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.dao.EmisorDAO;
import com.tikal.toledo.dao.FacturaDAO;
import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.SeriesDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.facturacion.ComprobanteVentaFactory33;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.DatosEmisor;
import com.tikal.toledo.model.Factura;
import com.tikal.toledo.model.FacturaVTT;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.EmailSender;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

 
@Controller
@RequestMapping(value={"/ventas33"})
public class VentaController33 {

	@Autowired
	EmisorDAO emisordao;
	
	@Autowired
	VentaDAO ventadao;
	
	@Autowired
	ProductoDAO productodao;
	
	@Autowired
	ClienteDAO clientedao;
	
	@Autowired
	TornilloDAO tornillodao;
	
	@Autowired
	LoteDAO lotedao;
	
	@Autowired
	ComprobanteVentaFactory33 cvFactory;
	
	@Autowired
	FacturaDAO facturadao;
	
	@Autowired
	FacturaVTTService servicio33;
	
	@Autowired
	AlertaDAO alertadao;
	
	@Autowired
	SeriesDAO seriesdao;
	
	@Autowired
	UsuarioDAO usuariodao;
	
	@Autowired
	PerfilDAO perfildao;
	
	@Autowired
	private FacturaVttDAO facturaVTTDAO;
	
	@Autowired
	private FacturaVTTService facturaVTTService;
	
	@PostConstruct
	public void init() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package with the WSDL java classes
		marshaller.setContextPath("localhost");

//		client.setMarshaller(marshaller);
//		client.setUnmarshaller(marshaller);
	}

	@RequestMapping(value = {
	"/facturar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void facturar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, MessagingException, DocumentException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 3)){
		AsignadorDeCharset.asignar(re, rs);
			Venta venta= (Venta)JsonConvertidor.fromJson(json, Venta.class);
			DatosEmisor emisor= emisordao.getActivo();
			int folio = seriesdao.getSerieFactura();
			Cliente cliente= clientedao.cargar(venta.getIdCliente());
			Comprobante c=cvFactory.generarFactura(venta, cliente,emisor);
			//facturar
			c.setFolio(folio+"");
			c.setSerie("FS");
			
			ComprobanteVO comprobanteVO= new ComprobanteVO();
			comprobanteVO.setComprobante(c);
			RespuestaWebServicePersonalizada respuestaws = servicio33.timbrarPOS(comprobanteVO, re.getSession());
			String uuid= respuestaws.getUuidFactura();
			String[] respuesta= new String[2];
			if (uuid!=null) {
//				venta.setXml(cfdiXML);
				venta.setEstatus("FACTURADO");
				venta.setUuid(uuid);
				venta.setFactura("FS"+folio);
				seriesdao.incSerieFactura();
				ventadao.guardar(venta);
				respuesta[0]="0";
			}else{
				respuesta[0]="1";
				String mensaje=respuestaws.getMensajeRespuesta();
				respuesta[1]="Mensaje de respuesta: " +mensaje ;
			}
			
			rs.getWriter().println(JsonConvertidor.toJson(respuesta));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = "/cancelarAck", method = RequestMethod.POST)
	public void cancelarAck(HttpServletRequest req, HttpServletResponse res, @RequestBody String json) throws IOException {
		if(Util.verificarPermiso(req, usuariodao, perfildao, 3)){
		Venta v= ventadao.cargar(Long.parseLong(json));
		
		DatosEmisor emisor= emisordao.getActivo();
		
		String mensaje=servicio33.cancelarAck(v.getUuid(),emisor.getRfc(), req.getSession());
		res.getWriter().print(mensaje);
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/descargaNota/{id}" }, method = RequestMethod.GET)
	public void pdfNota(HttpServletRequest re, HttpServletResponse res, @PathVariable Long id) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
		AsignadorDeCharset.asignar(re, res);
		res.setContentType("Application/PDF");
		Venta venta= ventadao.cargar(id);
		Cliente c= null;
		if(venta.getIdCliente()!=0){
			c= clientedao.cargar(venta.getIdCliente());
		}
		Comprobante cfdi=cvFactory.generarNota(venta, c,emisordao.getActivo());
		FacturaVTT factura= facturaVTTDAO.consultar(venta.getUuid());
		cfdi.setFolio("Folio: "+venta.getFolio());
		try {
//			TimbreFiscalDigital timbre= (TimbreFiscalDigital)cfdi.getComplemento().getAny().get(0);
//			String uuid= timbre.getUUID();
			PdfWriter writer = servicio33.obtenerPDF(factura, res.getOutputStream());
			if(writer!=null){
				res.setContentType("Application/Pdf");
				res.getOutputStream().flush();
				res.getOutputStream().close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/pdfDescarga/{id}" }, method = RequestMethod.GET)
	public void pdf(HttpServletRequest re, HttpServletResponse res, @PathVariable String id) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
		res.setContentType("Application/PDF");
		FacturaVTT factura=facturaVTTDAO.consultar(id);
		try {
			PdfWriter writer = servicio33.obtenerPDF(factura, res.getOutputStream());
			if(writer!=null){
				res.setContentType("Application/Pdf");
				res.getOutputStream().flush();
				res.getOutputStream().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/sendmail" }, method = RequestMethod.POST,consumes= "application/json")
	public void mail(HttpServletRequest re, HttpServletResponse res, @RequestBody String json) throws IOException, MessagingException, DocumentException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
		Venta venta= (Venta)JsonConvertidor.fromJson(json, Venta.class);
		Cliente c= clientedao.cargar(venta.getIdCliente());
		Factura f= facturadao.consultar(venta.getUuid());
		EmailSender mailero = new EmailSender();
		if(f!=null){
			if(c.getEmail()!=null){
				servicio33.enviarEmail(c.getEmail(), f.getUuid(), re.getSession());
				res.getWriter().print("Se envió");
			}
		}
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = "/xmlDescarga/{uuid}", method = RequestMethod.GET, produces = "text/xml")
	public void obtenerXML(HttpServletRequest req, HttpServletResponse res, @PathVariable String uuid) throws IOException {
		if(Util.verificarPermiso(req, usuariodao, perfildao, 1,3)){
			AsignadorDeCharset.asignar(req, res);
			FacturaVTT factura = facturaVTTDAO.consultar(uuid);
			PrintWriter writer = res.getWriter();
			if (factura != null) {
				res.setContentType("text/xml");
				writer.println(factura.getCfdiXML());
			} else {
				writer.println("La factuca con el folio fiscal (uuid) ".concat(uuid).concat(" no existe"));
			}
		}else{
			res.sendError(403);
		}
	}
	
}
