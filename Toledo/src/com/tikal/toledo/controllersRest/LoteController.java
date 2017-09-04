package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.controllersRest.VO.ListaLotesVO;
import com.tikal.toledo.controllersRest.VO.LoteVO;
import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.ProveedorDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Proveedor;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.EmailSender;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value = { "/lotes" })
public class LoteController {

	@Autowired
	LoteDAO lotedao;

	@Autowired
	ProveedorDAO pdao;

	@Autowired
	ProductoDAO hdao;

	@Autowired
	TornilloDAO tdao;
	
	@Autowired
	UsuarioDAO usuariodao;
	
	@Autowired
	PerfilDAO perfildao;

	@RequestMapping(value = {
			"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException {
		if(Util.verificarPermiso(re, usuariodao, perfildao, 2)){
		Lote l = (Lote) JsonConvertidor.fromJson(json, Lote.class);
		lotedao.guardar(l);
		String s="";
		if(l.getTipo()==0){
		Producto h = hdao.cargar(l.getIdProducto());
		if (h != null) {
			h.setExistencia(h.getExistencia() + l.getCantidad());
			s=hdao.guardar(h);
		}
		}else {
			Tornillo t = tdao.cargar(l.getIdProducto());
			if (t != null) {
				t.setExistencia(t.getExistencia() + l.getCantidad());
				s=tdao.guardar(t);
				
			}
		}
		rs.getWriter().print(s);
		}else{
			rs.sendError(403);
		}
//		rs.getWriter().println(JsonConvertidor.toJson(l));
	}

	@RequestMapping(value = {
			"/save" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void guardar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException {
		if(Util.verificarPermiso(re, usuariodao, perfildao, 2)){
		ListaLotesVO listavo = (ListaLotesVO) JsonConvertidor.fromJson(json, ListaLotesVO.class);
		lotedao.guardarLotes(listavo.getLista());
		rs.getWriter().println(JsonConvertidor.toJson(listavo));
		}else{rs.sendError(403);}
	}

	@RequestMapping(value = { "/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException {
		if(Util.verificarPermiso(re, usuariodao, perfildao, 2,0)){
		List<Lote> lotes = lotedao.porProducto(Long.parseLong(id));
		List<LoteVO> lvos = new ArrayList<LoteVO>();

		for (Lote l : lotes) {
			LoteVO lvo = new LoteVO(l);
			if (l.getProveedor() != null) {
				Proveedor p = pdao.cargar(l.getProveedor());
				lvo.setProveedor(p.getNombre());
			}
			lvos.add(lvo);
		}
		rs.getWriter().println(JsonConvertidor.toJson(lvos));
		}else{
			rs.sendError(403);
		}
	}

}
