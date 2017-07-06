package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.tikal.toledo.dao.EmisorDAO;
import com.tikal.toledo.model.DatosEmisor;

public class EmisorDAOImp implements EmisorDAO {

	@Override
	public void add(DatosEmisor e) {
		ofy().save().entity(e).now();
	}

	@Override
	public List<DatosEmisor> todos() {
		return ofy().load().type(DatosEmisor.class).list();
	}

	@Override
	public void eliminar(DatosEmisor e) {
		ofy().delete().entity(e).now();
	}

	@Override
	public void activar(DatosEmisor e) {
		DatosEmisor ed = this.getActivo();
		if (ed != null) {
			ed.setActivo(false);
			ofy().save().entity(ed).now();
		}
		e.setActivo(true);
		ofy().save().entity(e).now();

	}

	@Override
	public DatosEmisor getActivo() {
		List<DatosEmisor> lista = ofy().load().type(DatosEmisor.class).filter("activo", true).list();
		if (lista.size() > 0) {
			return lista.get(0);
		}
		return null;
	}

	@Override
	public DatosEmisor getById(Long id) {
		return ofy().load().type(DatosEmisor.class).id(id).now();
	}

}
