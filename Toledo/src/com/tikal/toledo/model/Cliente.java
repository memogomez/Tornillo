package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.tikal.toledo.sat.cfd.TUbicacion;

@Entity
public class Cliente {
	@Id
	private Long id;
	
	@Index
	private String nombre;
	
	private String rfc;
	
	private TUbicacion domicilio;
	
	private String telefono;
	
	private int credito;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getCredito() {
		return credito;
	}

	public void setCredito(int credito) {
		this.credito = credito;
	}

	public TUbicacion getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(TUbicacion domicilio) {
		this.domicilio = domicilio;
	}
	
	

}
