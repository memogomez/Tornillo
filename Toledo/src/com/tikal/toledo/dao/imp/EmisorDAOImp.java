package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.tikal.toledo.dao.EmisorDAO;
import com.tikal.toledo.model.DatosEmisor;

public class EmisorDAOImp implements EmisorDAO{

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
		ofy().delete().entity(e);
	}

}
