package com.tikal.toledo.dao.imp;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class TornilloDAOIpm implements TornilloDAO {

	@Override
	public void guardar(Tornillo t) {
		ofy().save().entity(t).now();
	}

	@Override
	public Tornillo cargar(Long id) {
		return ofy().load().type(Tornillo.class).id(id).now();
	}

	@Override
	public List<Tornillo> buscar(String search) {
		search=search.toLowerCase();
		List<Tornillo> lista= ofy().load().type(Tornillo.class).list();
		List<Tornillo> result= new ArrayList<Tornillo>();
		for(Tornillo p:lista){
			if(p.getId().toString().contains(search) || p.getNombre().toLowerCase().contains(search)|| p.getMedidas().toLowerCase().contains(search)){
				result.add(p);
			}
		}
		return result;
	}

}
