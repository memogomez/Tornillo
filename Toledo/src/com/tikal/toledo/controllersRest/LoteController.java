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
import com.tikal.toledo.util.JsonConvertidor;

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

	@RequestMapping(value = {
			"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException {
		Lote l = (Lote) JsonConvertidor.fromJson(json, Lote.class);
		lotedao.guardar(l);
		Producto h = hdao.cargar(l.getIdProducto());
		if (h != null) {
			h.setExistencia(h.getExistencia() + l.getCantidad());
			hdao.guardar(h);
		} else {
			Tornillo t = tdao.cargar(l.getId());
			if (tdao != null) {
				t.setExistencia(t.getExistencia() + l.getCantidad());
				tdao.guardar(t);
			}
		}
		rs.getWriter().println(JsonConvertidor.toJson(l));
	}

	@RequestMapping(value = {
			"/save" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void guardar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException {
		ListaLotesVO listavo = (ListaLotesVO) JsonConvertidor.fromJson(json, ListaLotesVO.class);
		lotedao.guardarLotes(listavo.getLista());
		rs.getWriter().println(JsonConvertidor.toJson(listavo));
	}

	@RequestMapping(value = { "/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException {
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
	}

}
