package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Tornillo;

public interface TornilloDAO {

	public void guardar(Tornillo t);
	
	public void guardar(List<Tornillo> lista);

	public Tornillo cargar(Long id);

	public List<Tornillo> buscar(String search);
	
	public List<Tornillo> todos();
	
	public void alv();
}
