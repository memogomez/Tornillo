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
	
	@Index
	private String clave;
	private float precioMostrador;
	private float precioMayoreo;
	private float precioCredito;
	private float precioReferencia;
	private String claveSat;
	private String claveUnidad;
	private String unidadSat;
	
	@Index
	private String proveedor;
	
	private String marca;

	private float descuento;
	
	private float ganancia;
	
	private float impuesto;
	
	private int maximo;
	
	private int minimo;
	
	private float existencia;
	
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

	public float getExistencia() {
		return existencia;
	}

	public void setExistencia(float existencia) {
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public float getDescuento() {
		return descuento;
	}

	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}

	public float getGanancia() {
		return ganancia;
	}

	public void setGanancia(float ganancia) {
		this.ganancia = ganancia;
	}

	public float getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(float impuesto) {
		this.impuesto = impuesto;
	}

	public float getPrecioReferencia() {
		return precioReferencia;
	}

	public void setPrecioReferencia(float precioReferencia) {
		this.precioReferencia = precioReferencia;
	}
	
	public void calculaPecios(){
		float mostrador= this.precioReferencia+ (this.precioReferencia*this.impuesto)+ (this.precioReferencia*this.ganancia)-(this.precioReferencia*this.descuento);
		int aux= (int) (mostrador*100);
		this.precioMostrador=aux/100f;
		this.precioMayoreo=mostrador*0.95f;
		aux= (int) (this.precioMayoreo*100);
		this.precioMayoreo= aux/100f;
		this.precioCredito=mostrador* 1.10f;
		aux=(int) (this.precioCredito*100);
		this.precioCredito= aux/100f;
	}

	public String getClaveSat() {
		return claveSat;
	}

	public void setClaveSat(String claveSat) {
		this.claveSat = claveSat;
	}

	public String getClaveUnidad() {
		return claveUnidad;
	}

	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}

	public String getUnidadSat() {
		return unidadSat;
	}

	public void setUnidadSat(String unidadSat) {
		this.unidadSat = unidadSat;
	}
	
	
}
