package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Producto;

public interface ProductoDAO {

	public void guardar(Producto p);

	public Producto cargar(Long id);

	public List<Producto> buscar(String search);
	
	public List<Producto> todos();
	
	public List<Producto> todos(int page);

	int total();

	public void formula(float impuesto, float descuento, float ganancia);
}
