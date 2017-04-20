package com.tikal.toledo.dao;

import java.util.Date;
import java.util.List;

import com.tikal.toledo.model.Venta;

public interface VentaDAO {

	public void guardar(Venta v);
	
	public Venta cargar(Long id);
	
	public List<Venta> buscar(Date fi, Date ff);
	
	public List<Venta> todos(int page);
}
