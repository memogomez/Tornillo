package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Producto {
	@Id
	private Long id;
	
	@Index
	private String nombre;
	
	private float precioMostrador;
	private float precioMayoreo;
	private float precioCredito;
	
	
	@Index
	private String proveedor;
	
	private String marca;
	
	private int maximo;
	
	private int minimo;
	
	private int existencia;
	
	private int tipo;
	
	public Producto(){
		setTipo(0);
	}
	
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

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

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public int getMaximo() {
		return maximo;
	}

	public void setMaximo(int maximo) {
		this.maximo = maximo;
	}

	public int getMinimo() {
		return minimo;
	}

	public void setMinimo(int minimo) {
		this.minimo = minimo;
	}

	public int getExistencia() {
		return existencia;
	}

	public void setExistencia(int existencia) {
		this.existencia = existencia;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public float getPrecioMostrador() {
		return precioMostrador;
	}

	public void setPrecioMostrador(float precioMostrador) {
		this.precioMostrador = precioMostrador;
	}

	public float getPrecioMayoreo() {
		return precioMayoreo;
	}

	public void setPrecioMayoreo(float precioMayoreo) {
		this.precioMayoreo = precioMayoreo;
	}

	public float getPrecioCredito() {
		return precioCredito;
	}

	public void setPrecioCredito(float precioCredito) {
		this.precioCredito = precioCredito;
	}
	
	
}
