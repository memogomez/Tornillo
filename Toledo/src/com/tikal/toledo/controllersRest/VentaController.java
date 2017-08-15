package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.toledo.dao.AlertaDAO;
import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.dao.FacturaDAO;
import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.factura.Estatus;
import com.tikal.toledo.facturacion.ComprobanteVentaFactory;
import com.tikal.toledo.facturacion.ws.WSClient;
import com.tikal.toledo.model.AlertaInventario;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Factura;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.sat.cfd.Comprobante;
import com.tikal.toledo.sat.timbrefiscaldigital.TimbreFiscalDigital;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.CorteDeCaja;
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
	FacturaDAO facturadao;
	
	@Autowired
	WSClient client;
	
	@Autowired
	AlertaDAO alertadao;
	
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
			Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
			cal.add(Calendar.HOUR, -5);
			l.setFecha(cal.getTime());
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
			Comprobante c=cvFactory.generarFactura(venta, clientedao.cargar(venta.getIdCliente()));
			//facturar
			TimbraCFDIResponse timbraCFDIResp = client.getTimbraCFDIResponse(Util.marshallComprobante(c));
			List<Object> listaResultado = timbraCFDIResp.getTimbraCFDIResult().getAnyType();
			int codigoError = (int) listaResultado.get(1);
			if (codigoError == 0) {
				
			
				String cfdiXML = (String) listaResultado.get(3);
				Factura f= new Factura();
				f.setCfdiXML(cfdiXML);
				f.setCodigoQR((byte[])listaResultado.get(4));
				Comprobante cfdi= Util.unmarshallXML(cfdiXML);
				
				TimbreFiscalDigital timbreFD = (TimbreFiscalDigital) cfdi.getComplemento().getAny().get(0);
				Date fechaCertificacion = timbreFD.getFechaTimbrado().toGregorianCalendar().getTime();
				
				f.setFechaCertificacion(fechaCertificacion);
				f.setSelloDigital((String)listaResultado.get(5));
				f.setUuid(timbreFD.getUUID());
				f.setEstatus(Estatus.TIMBRADO);
//				venta.setXml(cfdiXML);
				venta.setEstatus("FACTURADO");
				venta.setUuid(f.getUuid());
				ventadao.guardar(venta);
				facturadao.guardar(f);
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
	
	@RequestMapping(value = {"/descargaNota/{id}" }, method = RequestMethod.GET)
	public void pdfNota(HttpServletRequest re, HttpServletResponse res, @PathVariable Long id) throws IOException{
		res.setContentType("Application/PDF");
		Venta venta= ventadao.cargar(id);
		Cliente c= null;
		if(venta.getIdCliente()!=0){
			c= clientedao.cargar(venta.getIdCliente());
		}
		Comprobante cfdi=cvFactory.generarNota(venta, c);
		try {
//			TimbreFiscalDigital timbre= (TimbreFiscalDigital)cfdi.getComplemento().getAny().get(0);
//			String uuid= timbre.getUUID();
			PDFFactura pdfFactura = new PDFFactura();
			PdfWriter writer = PdfWriter.getInstance(pdfFactura.getDocument(), res.getOutputStream());
			pdfFactura.getDocument().open();
			pdfFactura.getPieDePagina().setUuid("Nota");
			
//			if (factura.getEstatus().equals(Estatus.CANCELADO)) {
//				pdfFactura.getPieDePagina().setFechaCancel(factura.getFechaCancelacion());
//				pdfFactura.getPieDePagina().setSelloCancel(factura.getSelloCancelacion());
//				pdfFactura.construirPdfCancelado(cfdi, factura.getSelloDigital(), factura.getCodigoQR(),factura.getSelloCancelacion(),factura.getFechaCancelacion());
//				pdfFactura.crearMarcaDeAgua("CANCELADO", writer);
//			}else{
				pdfFactura.construirPdf(cfdi, "", null);
//			}
			pdfFactura.getDocument().close();
			res.getOutputStream().flush();
			res.getOutputStream().close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = {"/pdfDescarga/{id}" }, method = RequestMethod.GET)
	public void pdf(HttpServletRequest re, HttpServletResponse res, @PathVariable String id) throws IOException{
		res.setContentType("Application/PDF");
		Factura factura=facturadao.consultar(id);
		Comprobante cfdi = Util.unmarshallXML(factura.getCfdiXML());
		try {
			TimbreFiscalDigital timbre= (TimbreFiscalDigital)cfdi.getComplemento().getAny().get(0);
			String uuid= timbre.getUUID();
			PDFFactura pdfFactura = new PDFFactura();
			PdfWriter writer = PdfWriter.getInstance(pdfFactura.getDocument(), res.getOutputStream());
			pdfFactura.getDocument().open();
			pdfFactura.getPieDePagina().setUuid(uuid);
			
			if (factura.getEstatus().equals(Estatus.CANCELADO)) {
				pdfFactura.getPieDePagina().setFechaCancel(factura.getFechaCancelacion());
				pdfFactura.getPieDePagina().setSelloCancel(factura.getSelloCancelacion());
				pdfFactura.construirPdfCancelado(cfdi, factura.getSelloDigital(), factura.getCodigoQR(),factura.getSelloCancelacion(),factura.getFechaCancelacion());
				pdfFactura.crearMarcaDeAgua("CANCELADO", writer);
			}else{
				pdfFactura.construirPdf(cfdi, factura.getSelloDigital(), factura.getCodigoQR());
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
	
	@RequestMapping(value = "/xmlDescarga/{uuid}", method = RequestMethod.GET, produces = "text/xml")
	public void obtenerXML(HttpServletRequest req, HttpServletResponse res, @PathVariable String uuid) throws IOException {
			AsignadorDeCharset.asignar(req, res);
			Factura factura = facturadao.consultar(uuid);
			PrintWriter writer = res.getWriter();
			if (factura != null) {
				res.setContentType("text/xml");
				writer.println(factura.getCfdiXML());
			} else {
				writer.println("La factuca con el folio fiscal (uuid) ".concat(uuid).concat(" no existe"));
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
	
	@RequestMapping(value = "/corteDeCaja", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public void corte(HttpServletRequest req, HttpServletResponse res) throws IOException {
			AsignadorDeCharset.asignar(req, res);
			Date datef;
			Date datei;
			Calendar c = Calendar.getInstance();
			datei=c.getTime();
			c.add(Calendar.DATE, 1);
			datef = c.getTime();
			datei.setHours(0);
			datei.setMinutes(1);
			int dia1=datei.getDate();
			
			int dia2=datef.getDate();
//			List<Factura> lista = facturaDAO.buscar(datei, datef, rfc);
//			Reporte rep = new Reporte(lista);
			List<Venta> lista=ventadao.buscar(datei, datef);
			CorteDeCaja corte= new CorteDeCaja();
			corte.setVentas(lista);
			HSSFWorkbook reporte=corte.getReporte();
			reporte.write(res.getOutputStream());
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
				if(p.getMinimo()!=0){
					if(p.getExistencia()< p.getMinimo()){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario por debajo del m�nimo";
						alertadao.add(a);
					}else{if(p.getExistencia() < (p.getMinimo()*1.10)){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario a punto de llegar al m�nimo";
						alertadao.add(a);
					}}
				}
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
				if(p.getMinimo()!=0){
					if(p.getExistencia()< p.getMinimo()){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario por debajo del m�nimo";
						alertadao.add(a);
					}else{if(p.getExistencia() < (p.getMinimo()*1.10)){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre()+" "+p.getMedidas();
						a.alerta="Inventario a punto de llegar al m�nimo";
						alertadao.add(a);
					}}
				}
			}
			
			
			
			monto+= d.getPrecioUnitario()* d.getCantidad();
		}
		return monto;
	}
	
}
