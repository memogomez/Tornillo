package com.tikal.toledo.dao;

import java.sql.SQLException;
import java.util.List;

import com.tikal.toledo.model.Tornillo;

public interface TornilloDAO {

	public String guardar(Tornillo t);
	
	public void guardar(List<Tornillo> lista);

	public Tornillo cargar(Long id);

	public List<Tornillo> buscar(String search);
	
	public Tornillo buscarNombre(Tornillo t);
	
	public List<Tornillo> todos()throws SQLException;
	
	public List<Tornillo> page(int p)throws SQLException;
	
	public int total();
	
	public void alv();
	
	public void formula(float impuesto, float descuento, float ganancia);
	
	public void eliminar(Tornillo t) throws SQLException;
}
