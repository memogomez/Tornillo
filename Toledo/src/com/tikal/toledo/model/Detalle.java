package com.tikal.toledo.model;

public class Detalle {
	
	private String claveUnidad;
	private String unidad;
	private String claveSat;
	private String descripcion;
	private Long idProducto;
	private double cantidad;
	private double precioUnitario;
	private double importe;
	private int tipo;
	public String getClaveUnidad() {
		return claveUnidad;
	}
	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getClaveSat() {
		return claveSat;
	}
	public void setClaveSat(String claveSat) {
		this.claveSat = claveSat;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public double getTotal() {
		return importe;
	}
	public void setTotal(double importe) {
		this.importe = importe;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
}