package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Proveedor;

public interface ProveedorDAO {
	public void guardar(Proveedor p);
	public Proveedor cargar(Long id);
	public List<Proveedor> buscar(String search);
}
