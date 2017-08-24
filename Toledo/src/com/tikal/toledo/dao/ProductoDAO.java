package com.tikal.toledo.dao;

import java.sql.SQLException;
import java.util.List;

import com.tikal.toledo.model.Producto;

public interface ProductoDAO {

	public String guardar(Producto p) throws SQLException;

	public void guardar(List<Producto> lista) throws SQLException;
	
	public Producto cargar(Long id);

	public List<Producto> buscar(String search);
	
	public List<Producto> todos();
	
	public List<Producto> todos(int page) throws SQLException;

	int total();
	
	public void alv();

	public void formula(float impuesto, float descuento, float ganancia);
	
	public void eliminar(Producto p) throws SQLException;
}
