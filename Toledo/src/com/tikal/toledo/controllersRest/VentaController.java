package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.cacao.factura.Estatus;
import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.facturacion.ComprobanteVentaFactory;
import com.tikal.toledo.facturacion.ws.WSClient;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.sat.cfd.Comprobante;
import com.tikal.toledo.sat.timbrefiscaldigital.TimbreFiscalDigital;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.PDFFactura;
import com.tikal.toledo.util.Util;

import localhost.TimbraCFDIResponse;

@Controller
@RequestMapping(value={"/ventas"})
public class VentaController {

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
	ComprobanteVentaFactory cvFactory;
	
	@Autowired
	WSClient client;
	
	@PostConstruct
	public void init() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package with the WSDL java classes
		marshaller.setContextPath("localhost");

		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
	}
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Venta l= (Venta)JsonConvertidor.fromJson(json, Venta.class);
			l.setFecha(new Date());
			l.setCliente("Otro");
			if(l.getIdCliente()!=0){
				Cliente c= clientedao.cargar(l.getIdCliente());
				l.setCliente(c.getNombre());
			}
			l.setEstatus("VENDIDO");
			
			
			l.setMonto(actualizarInventario(l.getDetalles()));
			ventadao.guardar(l);
			rs.getWriter().println(JsonConvertidor.toJson(l));
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
			Venta l= ventadao.cargar(Long.parseLong(id));
			rs.getWriter().println(JsonConvertidor.toJson(l));
	}
	
	@RequestMapping(value = {
	"/facturar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void facturar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Venta venta= (Venta)JsonConvertidor.fromJson(json, Venta.class);
			cvFactory.generarFactura(venta, clientedao.cargar(venta.getIdCliente()));
			//facturar
			TimbraCFDIResponse timbraCFDIResp = client.getTimbraCFDIResponse(venta.getXml());
			List<Object> listaResultado = timbraCFDIResp.getTimbraCFDIResult().getAnyType();
			int codigoError = (int) listaResultado.get(1);
			if (codigoError == 0) {
				String cfdiXML = (String) listaResultado.get(3);
				venta.setXml(cfdiXML);
				venta.setEstatus("FACTURADO");
				ventadao.guardar(venta);
			}
			
			rs.getWriter().println(JsonConvertidor.toJson(venta));
	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest req, HttpServletResponse res) {
		try {
			AsignadorDeCharset.asignar(req, res);
			String fi = (String) req.getParameter("fi");
			String ff = (String) req.getParameter("ff");
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			Date datei = formatter.parse(fi);
			Date datef = formatter.parse(ff);
			Calendar c = Calendar.getInstance();
			c.setTime(datef);
			c.add(Calendar.DATE, 1);
			datef = c.getTime();

			// agregar serie en el @RequestMapping
			List<Venta> listaR = ventadao.buscar(datei, datef);

			/*
			 * List<Factura> lista=facturaDAO.buscar(datei, datef,rfc);
			 * List<FacturaVO> listaVO=new ArrayList<FacturaVO>(); for(Factura
			 * f:lista){ Comprobante c= Util.unmarshallXML(f.getCfdiXML());
			 * f.setCfdi(c); FacturaVO fVO = new FacturaVO();
			 * fVO.setUuid(f.getUuid()); fVO.setEstatus(f.getEstatus());
			 * fVO.setTotal(NumberFormat.getCurrencyInstance().format(f.getCfdi(
			 * ).getTotal().doubleValue()));
			 * fVO.setFechaCertificacion(f.getFechaCertificacion());
			 * fVO.setRfcReceptor(f.getCfdi().getReceptor().getRfc());
			 * listaVO.add(fVO);
			 * 
			 * if (f.getFechaCertificacion() == null &&
			 * f.getEstatus().equals(Estatus.GENERADO)) {
			 * f.setFechaCertificacion(c.getFecha().toGregorianCalendar().
			 * getTime()); } }
			 * res.getWriter().println(JsonConvertidor.toJson(listaVO));
			 */
			res.getWriter().println(JsonConvertidor.toJson(listaR));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = {
	"/findAll/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException{
		List<Venta> lista= ventadao.todos(page);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	@RequestMapping(value = {
	"/numPages" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		rs.getWriter().print(ventadao.pages());
	}
	
	@RequestMapping(value = {
	"/numPagesProductos" }, method = RequestMethod.GET, produces = "application/json")
	public void pagesProductos(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		int totalp= productodao.total();
		int totalt = tornillodao.total();
		int pages= ((totalp+totalt-1)/50)+1;
		rs.getWriter().print(ventadao.pages());
	}
	
	@RequestMapping(value = {
	"/pdfDescargar/{id}" }, method = RequestMethod.GET, produces = "application/pdf")
	public void pdf(HttpServletRequest re, HttpServletResponse res, @PathVariable Long id) throws IOException{
		res.setContentType("Application/Pdf");
		Venta v = ventadao.cargar(id);
		Comprobante cfdi = Util.unmarshallXML(v.getXml());
		try {
			TimbreFiscalDigital timbre= (TimbreFiscalDigital)cfdi.getComplemento().getAny().get(0);
			String uuid= timbre.getUUID();
			PDFFactura pdfFactura = new PDFFactura();
			PdfWriter writer = PdfWriter.getInstance(pdfFactura.getDocument(), res.getOutputStream());
			pdfFactura.getPieDePagina().setUuid(uuid);
//			if (v.getEstatus().compareTo("CANCELADO")==0) {
//				pdfFactura.getPieDePagina().setFechaCancel(factura.getFechaCancelacion());
//				pdfFactura.getPieDePagina().setSelloCancel(factura.getSelloCancelacion());
//				;
//			}
			writer.setPageEvent(pdfFactura.getPieDePagina());

			pdfFactura.getDocument().open();
//			if (factura.getEstatus().equals(Estatus.TIMBRADO))
				pdfFactura.construirPdf(cfdi, cfdi.getSelloDigital(), v.getCodigoQR(), imagen,
						factura.getEstatus());
			else if (factura.getEstatus().equals(Estatus.GENERADO)) {
				pdfFactura.construirPdf(cfdi, imagen, factura.getEstatus());

				PdfContentByte fondo = writer.getDirectContent();
				Font fuente = new Font(FontFamily.HELVETICA, 45);
				Phrase frase = new Phrase("Pre-factura", fuente);
				fondo.saveState();
				PdfGState gs1 = new PdfGState();
				gs1.setFillOpacity(0.5f);
				fondo.setGState(gs1);
				ColumnText.showTextAligned(fondo, Element.ALIGN_CENTER, frase, 297, 650, 45);
				fondo.restoreState();
			}

			else if (factura.getEstatus().equals(Estatus.CANCELADO)) {
				pdfFactura.construirPdfCancelado(cfdi, factura.getSelloDigital(), factura.getCodigoQR(), imagen,
						factura.getEstatus(), factura.getSelloCancelacion(), factura.getFechaCancelacion());

				pdfFactura.crearMarcaDeAgua("CANCELADO", writer);
			}

			pdfFactura.getDocument().close();
			res.getOutputStream().flush();
			res.getOutputStream().close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = {
	"/productos/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void productos(HttpServletRequest re, HttpServletResponse rs,@PathVariable int page) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		int totalp = productodao.total();
		int nump = totalp / 50;
		int offset = totalp % 50;
		nump++;
		int rest = nump - page;
		List<List> listaf= new ArrayList<List>();
		List<Producto> listap = productodao.todos(page);
		if (rest < 1) {
			List<Tornillo> lista = tornillodao.page(Math.abs(rest)+1);
			lista= lista.subList(0, 50-offset);
			if(rest<0){
				lista.addAll(tornillodao.page(Math.abs(rest)).subList(offset, 49));
			}
			listaf.add(lista);
		}
		
		listaf.add(listap);
		rs.getWriter().println(JsonConvertidor.toJson(listaf));
	}
	
	
	private float actualizarInventario(List<Detalle> detalles){
		float monto=0;
		for(Detalle d:detalles){
			if(d.getTipo()==0){
				Producto p= productodao.cargar(d.getIdProducto());
				int restante= p.getExistencia()-d.getCantidad();
				p.setExistencia(restante);
				List<Lote> lista= lotedao.porProducto(p.getId());
				int aux= d.getCantidad();
				
				for(Lote l : lista){
					if(aux< l.getCantidad()){
						l.setCantidad(l.getCantidad()-aux);
						break;
					}else{
						aux= aux-l.getCantidad();
						l.setCantidad(0);
					}
				}
				lotedao.guardarLotes(lista);
				productodao.guardar(p);
			}else{
				Tornillo p= tornillodao.cargar(d.getIdProducto());
				int restante= p.getExistencia()-d.getCantidad();
				p.setExistencia(restante);
				List<Lote> lista= lotedao.porProducto(p.getId());
				int aux= d.getCantidad();
				
				for(Lote l : lista){
					if(aux< l.getCantidad()){
						l.setCantidad(l.getCantidad()-aux);
						break;
					}else{
						aux= aux-l.getCantidad();
						l.setCantidad(0);
					}
				}
				lotedao.guardarLotes(lista);
				tornillodao.guardar(p);
			}
			monto+= d.getPrecioUnitario()* d.getCantidad();
		}
		return monto;
	}
	
}
