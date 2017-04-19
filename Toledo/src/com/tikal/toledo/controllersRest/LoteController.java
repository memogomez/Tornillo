package com.tikal.toledo.controllersRest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/lotes"})
public class LoteController {

	@Autowired
	LoteDAO lotedao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
			Lote l= (Lote)JsonConvertidor.fromJson(json, Lote.class);
			lotedao.guardar(l);
			rs.getWriter().println(JsonConvertidor.toJson(l));
	}
}
