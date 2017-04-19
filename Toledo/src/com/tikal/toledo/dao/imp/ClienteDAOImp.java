package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.model.Cliente;

public class ClienteDAOImp implements ClienteDAO{

	@Override
	public void guardar(Cliente c) {
		ofy().save().entity(c).now();
	}

	@Override
	public Cliente cargar(Long id) {
		return ofy().load().type(Cliente.class).id(id).now();
	}

	@Override
	public List<Cliente> buscar(String search) {
		search= search.toLowerCase();
		List<Cliente> lista= ofy().load().type(Cliente.class).list();
		List<Cliente> result= new ArrayList<Cliente>();
		for(Cliente c:lista){
			if(c.getNombre().toLowerCase().contains(search)|| c.getRfc().toLowerCase().contains(search)){
				result.add(c);
			}
		}
		return result;
	}

}
