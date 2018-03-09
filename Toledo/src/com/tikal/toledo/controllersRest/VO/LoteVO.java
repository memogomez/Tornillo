package com.tikal.toledo.controllersRest.VO;

import java.util.Date;

import com.tikal.toledo.model.Lote;

public class LoteVO {
	private String nombre;
	private Date fecha;
	private float cantidad;
	private float costo;
	private String proveedor;
	
	public LoteVO(){
		
	}
	
	public LoteVO(Lote l){
		this.fecha=l.getFecha();
		this.cantidad=l.getCantidad();
		this.costo=l.getCosto();
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public float getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public float getCosto() {
		return costo;
	}
	public void setCosto(float costo) {
		this.costo = costo;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	
	
}
