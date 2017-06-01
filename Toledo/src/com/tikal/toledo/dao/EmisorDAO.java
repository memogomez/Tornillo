package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.DatosEmisor;

public interface EmisorDAO {
	public void add(DatosEmisor e);
	
	public List<DatosEmisor> todos();
	
	public void eliminar(DatosEmisor e);
}
