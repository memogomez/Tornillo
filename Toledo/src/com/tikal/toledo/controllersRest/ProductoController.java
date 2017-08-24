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
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Parseador;

@Controller
@RequestMapping(value={"/productos"})
public class ProductoController {

	@Autowired
	ProductoDAO productodao;
	
	@Autowired
	TornilloDAO tornillodao;
	
	@Autowired
	TornilloDAO tdao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException{
		AsignadorDeCharset.asignar(re, rs);
			Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
			productodao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
	}
	
	@RequestMapping(value = {
	"/addMultiple" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addMultiple(HttpServletRequest re, HttpServletResponse rs,@RequestBody String cadena) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		cadena = cadena.replace("<P>TOLEDO </P>", "");
		List<Tornillo> lista=Parseador.procesaTornillos(cadena);
//			Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
//			productodao.guardar(c);
			for(int i=0;i<lista.size();i++){
				Tornillo t= lista.get(i);
				Tornillo b=tdao.buscarNombre(t);
				if(b!=null){
					b.setExistencia(b.getExistencia()+t.getExistencia());
					b.setPrecioCredito(t.getPrecioCredito());
					b.setPrecioMayoreo(t.getPrecioMayoreo());
					b.setPrecioMostrador(t.getPrecioMostrador());
					if(b.getClave()==null){
						b.setClave(Parseador.getClave(b.getNombre(),b.getMedidas()));
					}
					lista.set(i, b);
					continue;
				}
				if(t.getClave()==null){
					t.setClave(Parseador.getClave(t.getNombre(),t.getMedidas()));
				}
				
			}
		
			rs.getWriter().println(JsonConvertidor.toJson(lista));
			tdao.guardar(lista);
	}
	
	@RequestMapping(value = {
	"/addMultipleH" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addMultipleH(HttpServletRequest re, HttpServletResponse rs,@RequestBody String cadena) throws IOException, SQLException{
		AsignadorDeCharset.asignar(re, rs);
		cadena = cadena.replace("<P>TOLEDO </P>", "");
		List<Producto> lista=Parseador.procesaHerramientas(cadena);
		
			rs.getWriter().println(JsonConvertidor.toJson(lista));
			productodao.guardar(lista);
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
			AsignadorDeCharset.asignar(re, rs);
			rs.getWriter().println(JsonConvertidor.toJson(productodao.cargar(Long.parseLong(id))));
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void busca(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
			AsignadorDeCharset.asignar(re, rs);
			List<Producto> lista= productodao.buscar(search);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	@RequestMapping(value = {
	"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs,@PathVariable int page) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		List<Producto> lista= productodao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
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
		AsignadorDeCharset.asignar(re, rs);	
		Producto p= (Producto) JsonConvertidor.fromJson(json, Producto.class);
		productodao.eliminar(p);
	}
	
	@RequestMapping(value = {
	"/aplicaFormula" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void formula(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			String[] args= json.split(",");
			float impuesto =Float.parseFloat(args[0])/100;
			float descuento= Float.parseFloat(args[1])/100;
			float ganancia= Float.parseFloat(args[2])/100;
			productodao.formula(impuesto, descuento, ganancia);
			tornillodao.formula(impuesto, descuento, ganancia);
			rs.getWriter().println(JsonConvertidor.toJson(args));
	}
}
