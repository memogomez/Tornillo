package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.ProveedorDAO;
import com.tikal.toledo.model.Proveedor;

public class ProveedorDAOImp implements ProveedorDAO{
	@Override
	public void guardar(Proveedor c) {
		ofy().save().entity(c).now();
	}

	@Override
	public Proveedor cargar(Long id) {
		return ofy().load().type(Proveedor.class).id(id).now();
	}

	@Override
	public List<Proveedor> buscar(String search) {
		search= search.toLowerCase();
		List<Proveedor> lista= ofy().load().type(Proveedor.class).list();
		List<Proveedor> result= new ArrayList<Proveedor>();
		for(Proveedor c:lista){
			if(c.getNombre().toLowerCase().contains(search)){
				result.add(c);
			}
		}
		return result;
	}

	@Override
	public List<Proveedor> todos() {
		return ofy().load().type(Proveedor.class).limit(50).list();
	}

}
