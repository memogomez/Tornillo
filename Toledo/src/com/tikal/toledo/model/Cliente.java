package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

public class Cliente {
	@Id
	private Long id;
	
	@Index
	private String nombre;
	
	private String rfc;
	
	private Domicilio domicilio;
	
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
	
	public String getCalle(){
		return this.domicilio.calle;
	}
	
	public void setCalle(String calle){
		this.domicilio.calle=calle;
	}
	
	public String getColonia(){
		return this.domicilio.colonia;
	}
	
	public void setColonia(String colonia){
		this.domicilio.colonia=colonia;
	}
	
	public String getNumero(){
		return this.domicilio.numero;
	}
	
	public void setNumero(String numero){
		this.domicilio.numero=numero;
	}
	
	public String getCP(){
		return this.domicilio.cp;
	}
	
	public void setCP(String cp){
		this.domicilio.cp=cp;
	}

	private class Domicilio{
		String calle;
		String colonia;
		String numero;
		String cp;
	}
}
