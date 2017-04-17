package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Index;

public class Tornillo extends Producto{

	@Index 
	String medidas;
	
	String mayoreo;
	
}
