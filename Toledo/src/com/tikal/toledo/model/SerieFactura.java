package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SerieFactura {

	@Id Long id;
	public int folio;
}
