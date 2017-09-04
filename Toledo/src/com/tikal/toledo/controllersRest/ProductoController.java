package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Parseador;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value={"/productos"})
public class ProductoController {

	@Autowired
	ProductoDAO productodao;
	
	@Autowired
	TornilloDAO tornillodao;
	
	@Autowired
	TornilloDAO tdao;
	
	@Autowired
	UsuarioDAO usuariodao;
	
	@Autowired
	PerfilDAO perfildao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 0)){
		AsignadorDeCharset.asignar(re, rs);
			Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
			productodao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/addMultiple" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addMultiple(HttpServletRequest re, HttpServletResponse rs,@RequestBody String cadena) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		cadena = cadena.replace("<P>TOLEDO </P>", "");
		List<Tornillo> lista=Parseador.procesaTornillos(cadena);
//			Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
//			productodao.guardar(c);
 		String querysote="";
			for(int i=0;i<lista.size();i++){
				Tornillo t= lista.get(i);
//				Tornillo b=tdao.buscarNombre(t);
//				if(b!=null){
//					b.setExistencia(b.getExistencia()+t.getExistencia());
//					b.setPrecioCredito(t.getPrecioCredito());
//					b.setPrecioMayoreo(t.getPrecioMayoreo());
//					b.setPrecioMostrador(t.getPrecioMostrador());
//					if(b.getClave()==null){
//						b.setClave(Parseador.getClave(b.getNombre(),b.getMedidas()));
//					}
//					lista.set(i, b);
//					continue;
//				}
//				if(t.getClave()==null){
//					t.setClave(Parseador.getClave(t.getNombre(),t.getMedidas()));
//				}
				querysote+=this.hazqueryt(t)+"\n";
			}
			
			rs.getWriter().println(querysote);
//			rs.getWriter().println(JsonConvertidor.toJson(lista));
//			tdao.guardar(lista);
			
	}
	
	@RequestMapping(value = {
	"/addMultipleH" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addMultipleH(HttpServletRequest re, HttpServletResponse rs,@RequestBody String cadena) throws IOException, SQLException{
		AsignadorDeCharset.asignar(re, rs);
		cadena = cadena.replace("<P>TOLEDO </P>", "");
		List<Producto> lista=Parseador.procesaHerramientas(cadena);
//		Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
//		productodao.guardar(c);
		String querysote="";
		for(int i=0;i<lista.size();i++){
			Producto t= lista.get(i);
//			Tornillo b=tdao.buscarNombre(t);
//			if(b!=null){
//				b.setExistencia(b.getExistencia()+t.getExistencia());
//				b.setPrecioCredito(t.getPrecioCredito());
//				b.setPrecioMayoreo(t.getPrecioMayoreo());
//				b.setPrecioMostrador(t.getPrecioMostrador());
//				if(b.getClave()==null){
//					b.setClave(Parseador.getClave(b.getNombre(),b.getMedidas()));
//				}
//				lista.set(i, b);
//				continue;
//			}
//			if(t.getClave()==null){
//				t.setClave(Parseador.getClave(t.getNombre(),t.getMedidas()));
//			}
			querysote+=this.hazqueryp(t)+"\n";
		}
		
		rs.getWriter().println(querysote);
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 0,1,2)){
			AsignadorDeCharset.asignar(re, rs);
			rs.getWriter().println(JsonConvertidor.toJson(productodao.cargar(Long.parseLong(id))));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void busca(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 0,1,2)){
			AsignadorDeCharset.asignar(re, rs);
			List<Producto> lista= productodao.buscar(search);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs,@PathVariable int page) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 0,1,2)){
		AsignadorDeCharset.asignar(re, rs);
		List<Producto> lista= productodao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = { "/pages/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException, SQLException {
		AsignadorDeCharset.asignar(re, rs);
		List<Producto> lista = productodao.todos(page);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	@RequestMapping(value = {
	"/numPages" }, method = RequestMethod.GET, produces = "application/json")
	public void numOfPages(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		int total=productodao.total();
		int pages = (total/50);
		pages++;
		rs.getWriter().println(pages);
	}
	
	@RequestMapping(value = { "/alv" }, method = RequestMethod.GET, produces = "application/json")
	public void alv(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		productodao.alv();
		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
	
	@RequestMapping(value = {
	"/elimina" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void delete(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 0)){
		AsignadorDeCharset.asignar(re, rs);	
		Producto p= (Producto) JsonConvertidor.fromJson(json, Producto.class);
		productodao.eliminar(p);
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/aplicaFormula" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void formula(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 0)){
			String[] args= json.split(",");
			float impuesto =Float.parseFloat(args[0])/100;
			float descuento= Float.parseFloat(args[1])/100;
			float ganancia= Float.parseFloat(args[2])/100;
			productodao.formula(impuesto, descuento, ganancia);
			tornillodao.formula(impuesto, descuento, ganancia);
			rs.getWriter().println(JsonConvertidor.toJson(args));
		}else{
			rs.sendError(403);
		}
	}
	
	private String hazqueryt(Tornillo t){
		String query= "INSERT INTO t_tornillo (descuento, existencia, ganancia, impuesto, marca, maximo, minimo, mayoreo, medidas, nombre, precioCredito, PrecioMayoreo, precioMostrador, precioReferencia, proveedor, tipo, clave) VALUES (";
		
		query+=t.getDescuento()+", ";
		query+=t.getExistencia()+", ";
		query+=t.getGanancia()+", ";
		query+= t.getImpuesto()+", ";
		if(t.getMarca()!=null){
		query+= "'"+t.getMarca()+"', ";
		}else{
			query+= "NULL, ";
		}
		query+= t.getMaximo()+", ";
		query+= t.getMinimo()+", ";
		query+= t.getMayoreo()+", ";
		query+= "'"+t.getMedidas()+"', ";
		query+= "'"+t.getNombre()+"', ";
		query+= t.getPrecioCredito()+", ";
		query+= t.getPrecioMayoreo()+", ";
		query+= t.getPrecioMostrador()+", ";
		query+= t.getPrecioReferencia()+", ";
		if(t.getProveedor()!=null){
			query+="'"+t.getProveedor()+"', ";
		}else{
			query+="NULL, ";
		}
		query+="1, ";
		query+= "'"+t.getClave()+"');";
			
		
		return query;
	}
	private String hazqueryp(Producto t){
		String query= "INSERT INTO t_producto (descuento, existencia, ganancia, impuesto, marca, maximo, minimo, nombre, precioCredito, PrecioMayoreo, precioMostrador, precioReferencia, proveedor, tipo, clave) VALUES (";
		
		query+=t.getDescuento()+", ";
		query+=t.getExistencia()+", ";
		query+=t.getGanancia()+", ";
		query+= t.getImpuesto()+", ";
		if(t.getMarca()!=null){
		query+= "'"+t.getMarca()+"', ";
		}else{
			query+= "NULL, ";
		}
		query+= t.getMaximo()+", ";
		query+= t.getMinimo()+", ";
		query+= "'"+t.getNombre()+"', ";
		query+= t.getPrecioCredito()+", ";
		query+= t.getPrecioMayoreo()+", ";
		query+= t.getPrecioMostrador()+", ";
		query+= t.getPrecioReferencia()+", ";
		if(t.getProveedor()!=null){
			query+="'"+t.getProveedor()+"', ";
		}else{
			query+="NULL, ";
		}
		query+="0, ";
		query+= "'"+t.getClave()+"');";
			
		
		return query;
	}
}
