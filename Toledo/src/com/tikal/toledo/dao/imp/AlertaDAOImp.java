package com.tikal.toledo.dao.imp;

import java.util.List;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.tikal.toledo.dao.AlertaDAO;
import com.tikal.toledo.model.AlertaInventario;

public class AlertaDAOImp implements AlertaDAO{

	@Override
	public void add(AlertaInventario a) {
		ofy().save().entity(a).now();
	}

	@Override
	public List<AlertaInventario> consultar() {
		
		return ofy().load().type(AlertaInventario.class).list();
	}

	@Override
	public void delete(AlertaInventario a) {
		ofy().delete().entity(a).now();
	}

}
