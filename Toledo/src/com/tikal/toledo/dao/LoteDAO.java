package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Lote;

public interface LoteDAO {

	public void guardar(Lote l);
	
	public List<Lote> porProducto(Long idProducto);
	
	public void guardarLotes(List<Lote> lotes);
}