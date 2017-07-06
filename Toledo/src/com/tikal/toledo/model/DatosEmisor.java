package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class DatosEmisor {

	@Id 
	private Long id;
	
	@Index
	private String rfc;
	
	private String nombre;
	
	@Index
	private boolean activo;
	
	private DomicilioFiscal domicilioFiscal;

	public DomicilioFiscal getDomicilioFiscal() {
		return domicilioFiscal;
	}

	public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) {
		this.domicilioFiscal = domicilioFiscal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
