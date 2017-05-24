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

import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/ventas"})
public class VentaController {

	@Autowired
	VentaDAO ventadao;
	
	@Autowired
	ProductoDAO productodao;
	
	@Autowired
	TornilloDAO tornillodao;
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
	
	
	private void actualizarInventario(List<Detalle> detalles){
		for(Detalle d:detalles){
			if(d.getTipo()==0){
				Producto p= productodao.cargar(d.getIdProducto());
			}
		}
	}
	
}
