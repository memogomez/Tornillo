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

import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/tornillos"})
public class TornilloController {

	@Autowired
	TornilloDAO tornillodao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Tornillo c= (Tornillo) JsonConvertidor.fromJson(json, Tornillo.class);
			tornillodao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
			
			rs.getWriter().println(JsonConvertidor.toJson(tornillodao.cargar(Long.parseLong(id))));
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void busca(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
			List<Tornillo> lista= tornillodao.buscar(search);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	@RequestMapping(value = {
	"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		List<Tornillo> lista= tornillodao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	@RequestMapping(value = {
	"/pages/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs,@PathVariable int page) throws IOException{
		List<Tornillo> lista= tornillodao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	
	@RequestMapping(value = {
	"/alv" }, method = RequestMethod.GET, produces = "application/json")
	public void alv(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		tornillodao.alv();
		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
}
