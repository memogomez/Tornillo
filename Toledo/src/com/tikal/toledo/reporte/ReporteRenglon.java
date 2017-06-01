package com.tikal.toledo.reporte;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;

public class ReporteRenglon {
	private Long id;
	private String nombre;
	private String medidas;
	private String clave;
	private int cantidad;
	
	public ReporteRenglon(Producto p){
		this.id=p.getId();
		this.nombre=p.getNombre();
		this.medidas="";
		this.clave=p.getClave();
		this.cantidad=p.getExistencia();
	}
	
	public ReporteRenglon(Tornillo p){
		this.id=p.getId();
		this.nombre=p.getNombre();
		this.medidas=p.getMedidas();
		this.clave=p.getClave();
		this.cantidad=p.getExistencia();
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
	public String getMedidas() {
		return medidas;
	}
	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public void llenarRenglon(HSSFRow r){
		for(int i=0;i<11;i++){
			r.createCell(i);
		}
		
		r.getCell(0).setCellValue(this.getId());
		r.getCell(1).setCellValue(this.getClave());
		r.getCell(2).setCellValue(this.getNombre());
		r.getCell(3).setCellValue(this.getMedidas());
		r.getCell(4).setCellValue(this.getCantidad());
	}
	
}
