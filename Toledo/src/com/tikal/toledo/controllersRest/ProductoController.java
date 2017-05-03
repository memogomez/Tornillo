package com.tikal.toledo.controllersRest;

import java.io.IOException;
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
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Parseador;

@Controller
@RequestMapping(value={"/productos"})
public class ProductoController {

	@Autowired
	ProductoDAO productodao;
	
	@Autowired
	TornilloDAO tdao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
			productodao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
	}
	
	@RequestMapping(value = {
	"/addMultiple" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addMultiple(HttpServletRequest re, HttpServletResponse rs,@RequestBody String cadena) throws IOException{
		cadena = cadena.replace("<P>TOLEDO </P>", "");
		List<Tornillo> lista=Parseador.parsear(cadena);
//			Producto c= (Producto) JsonConvertidor.fromJson(json, Producto.class);
//			productodao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
			tdao.guardar(lista);
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
			
			rs.getWriter().println(JsonConvertidor.toJson(productodao.cargar(Long.parseLong(id))));
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void busca(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
			List<Producto> lista= productodao.buscar(search);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	@RequestMapping(value = {
	"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		List<Producto> lista= productodao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	
}
