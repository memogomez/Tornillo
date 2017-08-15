package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.AlertaInventario;

public interface AlertaDAO {

	void add(AlertaInventario a);
	
	List<AlertaInventario> consultar();
	
	void delete(AlertaInventario a);
}
