package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.model.Producto;

public class ProductoDAOImp implements ProductoDAO{

	@Override
	public void guardar(Producto p) {
		ofy().save().entity(p).now();
	}

	@Override
	public Producto cargar(Long id) {
		return ofy().load().type(Producto.class).id(id).now();
	}

	@Override
	public List<Producto> buscar(String search) {
		search=search.toLowerCase();
		List<Producto> lista= ofy().load().type(Producto.class).list();
		List<Producto> result= new ArrayList<Producto>();
		for(Producto p:lista){
			if(p.getId().toString().contains(search) || p.getNombre().toLowerCase().contains(search)){
				result.add(p);
			}
		}
		return result;
	}

	@Override
	public List<Producto> todos() {
		return ofy().load().type(Producto.class).limit(50).list();
	}
	
}
