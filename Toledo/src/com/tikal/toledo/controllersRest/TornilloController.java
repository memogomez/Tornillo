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
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Parseador;

@Controller
@RequestMapping(value = { "/tornillos" })
public class TornilloController {

	@Autowired
	TornilloDAO tornillodao;

	@RequestMapping(value = {
			"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException {
		AsignadorDeCharset.asignar(re, rs);
		Tornillo c = (Tornillo) JsonConvertidor.fromJson(json, Tornillo.class);
		tornillodao.guardar(c);
		rs.getWriter().println(JsonConvertidor.toJson(c));
	}

	@RequestMapping(value = { "/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException {
		AsignadorDeCharset.asignar(re, rs);
		rs.getWriter().println(JsonConvertidor.toJson(tornillodao.cargar(Long.parseLong(id))));
	}

	@RequestMapping(value = { "/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void busca(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException {
		AsignadorDeCharset.asignar(re, rs);
		List<Tornillo> lista = tornillodao.buscar(search);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}

	@RequestMapping(value = { "/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		AsignadorDeCharset.asignar(re, rs);
		List<Tornillo> lista = tornillodao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}

	@RequestMapping(value = { "/pages/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException {
		AsignadorDeCharset.asignar(re, rs);
		List<Tornillo> lista = tornillodao.page(page);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}

	@RequestMapping(value = { "/numPages" }, method = RequestMethod.GET, produces = "application/json")
	public void numOfPages(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		List<Tornillo> lista = tornillodao.todos();
		int pages = (lista.size() / 50);
		pages++;
		rs.getWriter().println(pages);
	}

	@RequestMapping(value = { "/setClaves" }, method = RequestMethod.GET, produces = "application/json")
	public void claves(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		List<Tornillo> lista=tornillodao.todos();
		for(Tornillo t:lista){
			String nombre= t.getNombre();
			if(nombre.toLowerCase().contains("din")){
				nombre=nombre.substring(nombre.indexOf(" ")+1);
				nombre=nombre.substring(nombre.indexOf(" ")+1);
			}
			String clave=Parseador.getClave(nombre);
			t.setClave(clave);
			
			System.out.println(clave);
		}
		tornillodao.guardar(lista);
		
//		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
	
	@RequestMapping(value = { "/alv" }, method = RequestMethod.GET, produces = "application/json")
	public void alv(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		tornillodao.alv();
		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
}
