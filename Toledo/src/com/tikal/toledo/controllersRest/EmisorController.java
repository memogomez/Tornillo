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

import com.tikal.toledo.dao.EmisorDAO;
import com.tikal.toledo.model.DatosEmisor;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"emisor/"})
public class EmisorController {
	@Autowired 
	EmisorDAO emisordao;
	
	@RequestMapping(value = {
	"/registrar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		DatosEmisor de= (DatosEmisor) JsonConvertidor.fromJson(json, DatosEmisor.class);
		emisordao.add(de);
		rs.getWriter().print(JsonConvertidor.toJson(de));
	}
	
	@RequestMapping(value = {
	"/eliminar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void eliminar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		DatosEmisor de= emisordao.getById(Long.parseLong(json));
		emisordao.eliminar(de);;
		rs.getWriter().print(JsonConvertidor.toJson(de));
	}
	
	@RequestMapping(value = {
	"/activar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void activar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		DatosEmisor de= emisordao.getById(Long.parseLong(json));
		emisordao.activar(de);;
		rs.getWriter().print(JsonConvertidor.toJson(de));
	}
	
	@RequestMapping(value = {
	"/getAll" }, method = RequestMethod.GET, produces = "application/json")
	public void getAll(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		rs.getWriter().print(JsonConvertidor.toJson(emisordao.todos()));
	}
	
	@RequestMapping(value = {
	"/get/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void getAll(HttpServletRequest re, HttpServletResponse rs, @PathVariable long id) throws IOException{
		rs.getWriter().print(JsonConvertidor.toJson(emisordao.getById(id)));
	}


}
