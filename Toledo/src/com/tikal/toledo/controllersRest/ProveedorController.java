package com.tikal.toledo.controllersRest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.ProveedorDAO;
import com.tikal.toledo.model.Proveedor;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/proveedores"})
public class ProveedorController {
	
	@Autowired
	ProveedorDAO proveedordao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Proveedor c= (Proveedor) JsonConvertidor.fromJson(json, Proveedor.class);
			proveedordao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		rs.getWriter().println(JsonConvertidor.toJson(proveedordao.cargar(Long.parseLong(id))));
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
		rs.getWriter().println(JsonConvertidor.toJson(proveedordao.buscar(search)));
	}
}
