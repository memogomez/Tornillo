package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.AlertaDAO;
import com.tikal.toledo.model.AlertaInventario;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/alertas"})
public class AlertaController {
	@Autowired
	AlertaDAO alertadao;

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
}
