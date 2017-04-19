package com.tikal.toledo.model;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Venta {

	@Id
	private Long id;
	
	private boolean facturado;
	
	private Date fecha;
	
	private String cliente;
	
	private Long idCliente;
	
	private String user;
	
	private List<Detalle> detalles;

	private String xml;
	
	public boolean isFacturado() {
		return facturado;
	}

	public void setFacturado(boolean facturado) {
		this.facturado = facturado;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}
	
	public void addDetalle(Detalle detalle){
		this.detalles.add(detalle);
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
	
}
