package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.util.ArrayList;
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
			String clave=Parseador.getClave(nombre,t.getMedidas());
			t.setClave(clave);
			
			System.out.println(clave);
		}
		tornillodao.guardar(lista);
		
//		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
	
	@RequestMapping(value = { "/comillas" }, method = RequestMethod.GET, produces = "application/json")
	public void comillas(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		List<Tornillo> lista=tornillodao.todos();
		for(Tornillo t:lista){
			t.setMedidas(t.getMedidas().replace("&quot;", "\""));
		}
		tornillodao.guardar(lista);
//		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
	
	@RequestMapping(value = { "/alv" }, method = RequestMethod.GET, produces = "application/json")
	public void alv(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		tornillodao.alv();
		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
	
	@RequestMapping(value = { "/cambio" }, method = RequestMethod.GET, produces = "application/json")
	public void cambio(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		List<Tornillo> lista=tornillodao.todos();
		List<Tornillo> lista2= new ArrayList<Tornillo>();
		for(Tornillo t:lista){
			Tornillo t2= new Tornillo();
			t2.setClave(t.getClave());
			t2.setDescuento(0);
			t2.setExistencia(0);
			t2.setGanancia(0);
			t2.setPrecioReferencia(t.getPrecioMostrador());
			t2.setId(t.getId());
			t2.setImpuesto(0);
			t2.setMarca(t.getMarca());
			t2.setMaximo(t.getMaximo());
			t2.setMayoreo(t.getMayoreo());
			t2.setMedidas(t.getMedidas());
			t2.setMinimo(t.getMinimo());
			t2.setNombre(t.getNombre());
			t2.setPrecioCredito(t.getPrecioCredito());
			t2.setPrecioMayoreo(t.getPrecioMayoreo());
			t2.setPrecioMostrador(t.getPrecioMostrador());
			t2.setProveedor(t.getProveedor());
			t2.setTipo(t.getTipo());
			lista2.add(t2);
		}
		tornillodao.guardar(lista2);
		
		rs.getWriter().println(JsonConvertidor.toJson("ALV"));
	}
}
