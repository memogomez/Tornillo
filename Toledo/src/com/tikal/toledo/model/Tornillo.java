package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Tornillo extends Producto{

	@Index 
	private String medidas;
	
	private String mayoreo;

	public String getMedidas() {
		return medidas;
	}

	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}

	public String getMayoreo() {
		return mayoreo;
	}

	public void setMayoreo(String mayoreo) {
		this.mayoreo = mayoreo;
	}
	
	
	
}
