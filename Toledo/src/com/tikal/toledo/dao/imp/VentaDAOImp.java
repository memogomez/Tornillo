package com.tikal.toledo.dao.imp;

import java.util.Date;
import java.util.List;

import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.model.Venta;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class VentaDAOImp implements VentaDAO{

	@Override
	public void guardar(Venta v) {
		ofy().save().entity(v).now();
	}

	@Override
	public Venta cargar(Long id) {
		return ofy().load().type(Venta.class).id(id).now();
	}

	@Override
	public List<Venta> buscar(Date fi, Date ff) {
		List<Venta> lista= ofy().load().type(Venta.class).filter("fecha >=",fi).filter("fecha <=",ff).list();
		return null;
	}

}
