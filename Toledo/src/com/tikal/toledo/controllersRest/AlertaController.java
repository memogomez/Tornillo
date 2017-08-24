package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.AlertaDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.AlertaInventario;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/alertas"})
public class AlertaController {
	@Autowired
	AlertaDAO alertadao;
	
	@Autowired 
	ProductoDAO productodao;
	
	@Autowired
	TornilloDAO tornillodao;

	@RequestMapping(value = {
	"/numAlertas" }, method = RequestMethod.GET, produces = "application/json")
	public void numAlertas(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		List<AlertaInventario> lista= alertadao.consultar();
		rs.getWriter().print(lista.size());
	}
	
	@RequestMapping(value = {
	"/get" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		List<AlertaInventario> lista= alertadao.consultar();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
	}
	
	
	@RequestMapping(value = {
	"/sql" }, method = RequestMethod.GET, produces = "application/json")
	public void sql(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, SQLException{
		TornilloDAO pdao= new com.tikal.toledo.dao.imp.TornilloDAOIpm();
		List<Tornillo> lista= new ArrayList<Tornillo>();
		String respuesta="";
		try {
			lista = pdao.page(1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(lista.size()>0){
		for(Tornillo p:lista){
			if(p.getNombre().length()<=200){
				tornillodao.guardar(p);
			}
			pdao.eliminar(p);
		}
		resp.getWriter().println(respuesta);
		}else{
			resp.getWriter().println("Ya no hay Tornillos");
		}
	}
}
