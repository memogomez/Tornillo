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

import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/ventas"})
public class VentaController {

	@Autowired
	VentaDAO ventadao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Venta l= (Venta)JsonConvertidor.fromJson(json, Venta.class);
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
			Venta l= (Venta)JsonConvertidor.fromJson(json, Venta.class);
			
			//facturar
			
			ventadao.guardar(l);
			rs.getWriter().println(JsonConvertidor.toJson(l));
	}
	
	@RequestMapping(value = {
	"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		List<Venta> lista= ventadao.todos(0);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
}
