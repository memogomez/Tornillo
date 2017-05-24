package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value = { "/inventario" })
public class InventarioController {

	@Autowired
	TornilloDAO tornillodao;

	@Autowired
	ProductoDAO productodao;

	@RequestMapping(value = { "/pages/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException {
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
	
	@RequestMapping(value = { "/numPages" }, method = RequestMethod.GET, produces = "application/json")
	public void numOfPages(HttpServletRequest re, HttpServletResponse rs) throws IOException {
		int totalp=productodao.total();
		int totalt= tornillodao.total();
		
		int pages = ((totalp+totalt)/ 50);
		pages++;
		rs.getWriter().print(pages);
	}
}
