package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class AlertaInventario {

	@Id public Long id;
	@Index public Long idproducto;
	public String nombre;
	public String alerta;
	
}
