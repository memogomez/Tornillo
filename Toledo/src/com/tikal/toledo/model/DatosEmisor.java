package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class DatosEmisor {

	@Id 
	private Long id;
	
	private String rfc;
	private DomicilioFiscal domicilioFiscal;
	
	
}
