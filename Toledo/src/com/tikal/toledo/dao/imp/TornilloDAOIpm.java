package com.tikal.toledo.dao.imp;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class TornilloDAOIpm implements TornilloDAO {

	@Override
	public String guardar(Tornillo t) {
		ofy().save().entity(t).now();
		return t.getNombre();
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
			if(p.getMedidas()!=null){
				if(p.getMedidas().toLowerCase().contains(search)){
					result.add(p);
					continue;
				}
			}
			if(p.getClave()!=null){
			if(p.getClave().toLowerCase().contains(search) ||p.getId().toString().contains(search) || p.getNombre().toLowerCase().contains(search)){
				result.add(p);
				continue;
			}
			}else{
				if(p.getId().toString().contains(search) || p.getNombre().toLowerCase().contains(search)){
					result.add(p);
				}
			}
		}
		return result;
	}

	@Override
	public List<Tornillo> todos() {
		return ofy().load().type(Tornillo.class).list();
	}

	@Override
	public void guardar(List<Tornillo> lista) {
		ofy().save().entities(lista).now();
	}

	@Override
	public void alv() {
		List<Tornillo> lista= ofy().load().type(Tornillo.class).list();
		ofy().delete().entities(lista).now();
	}

	@Override
	public List<Tornillo> page(int p) {
		List<Tornillo> lista= ofy().load().type(Tornillo.class).offset((p-1)*50).limit(50).list();
		return lista;
	}

	@Override
	public int total() {
		return ofy().load().type(Tornillo.class).count();
	}

	@Override
	public void formula(float impuesto, float descuento, float ganancia) {
		List<Tornillo> lista= ofy().load().type(Tornillo.class).list();
		for(Tornillo t:lista){
			t.setDescuento(descuento);
			t.setImpuesto(impuesto);
			t.setGanancia(ganancia);
			t.calculaPecios();
		}
		ofy().save().entities(lista).now();
	}

	@Override
	public Tornillo buscarNombre(Tornillo t) {
		List<Tornillo> lista=ofy().load().type(Tornillo.class).filter("nombre",t.getNombre()).list();
		for(Tornillo t1:lista){
			if(t1.getMedidas().compareTo(t.getMedidas())==0){
				return t1;
			}
		}
		return null;
	}

	@Override
	public void eliminar(Tornillo t) {
		ofy().delete().entity(t);
	}

}
