package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Cliente;

public interface ClienteDAO {
	
	public void guardar(Cliente c);
	
	public Cliente cargar(Long id);

	public List<Cliente> buscar(String search);
	
	public List<Cliente> todos();
}
