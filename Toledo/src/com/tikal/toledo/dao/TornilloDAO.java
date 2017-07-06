package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Tornillo;

public interface TornilloDAO {

	public void guardar(Tornillo t);
	
	public void guardar(List<Tornillo> lista);

	public Tornillo cargar(Long id);

	public List<Tornillo> buscar(String search);
	
	public List<Tornillo> todos();
	
	public List<Tornillo> page(int p);
	
	public int total();
	
	public void alv();
	
	public void formula(float impuesto, float descuento, float ganancia);
}
