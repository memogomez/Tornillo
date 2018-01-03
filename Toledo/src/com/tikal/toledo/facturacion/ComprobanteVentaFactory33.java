package com.tikal.toledo.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tikal.cacao.sat.cfd.catalogos.dyn.C_ClaveProdServ;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_ClaveUnidad;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_CodigoPostal;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_FormaDePago;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_Impuesto;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_MetodoDePago;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_Moneda;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_RegimenFiscal;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_TipoDeComprobante;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_TipoFactor;
import com.tikal.cacao.sat.cfd.catalogos.dyn.C_UsoCFDI;
import com.tikal.cacao.sat.cfd33.Comprobante;
import com.tikal.cacao.sat.cfd33.Comprobante.Conceptos;
import com.tikal.cacao.sat.cfd33.Comprobante.Conceptos.Concepto;
import com.tikal.cacao.sat.cfd33.Comprobante.Emisor;
import com.tikal.cacao.sat.cfd33.Comprobante.Impuestos;
import com.tikal.cacao.sat.cfd33.Comprobante.Impuestos.Traslados;
import com.tikal.cacao.sat.cfd33.Comprobante.Impuestos.Traslados.Traslado;
import com.tikal.cacao.sat.cfd33.Comprobante.Receptor;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.DatosEmisor;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.util.Util;

/**
 * @author Tikal
 *
 */
@Service
public class ComprobanteVentaFactory33 {
	
	
	public Comprobante generarFactura(Venta venta, Cliente cliente, DatosEmisor emisor) {
		Comprobante comprobante = new Comprobante();
		comprobante.setVersion("3.3");
		comprobante.setFecha(Util.getXMLDate(new Date(), FormatoFecha.COMPROBANTE));
		comprobante.setMoneda(new C_Moneda("MXN"));
		comprobante.setLugarExpedicion(new C_CodigoPostal(emisor.getDomicilioFiscal().getCodigoPostal()));
		comprobante.setTipoDeComprobante(new C_TipoDeComprobante("I"));
		
		comprobante.setFormaPago(new C_FormaDePago(this.getFormaPago(venta.getFormaDePago())));
		comprobante.setMetodoPago(new C_MetodoDePago("PUE"));
		
		
		comprobante.setEmisor(construirEmisor(emisor));
		comprobante.setReceptor(construirReceptor(cliente));
		construirConceptos(venta.getDetalles(), comprobante);
		
		//comprobante.setImpuestos(construirImuestos(comprobante.getSubTotal()));
		BigDecimal total = comprobante.getSubTotal().add(comprobante.getImpuestos().getTotalImpuestosTrasladados());
		comprobante.setTotal(total.setScale(2, RoundingMode.HALF_UP));

		return comprobante;
//		venta.setXml();
	}
	
	public Comprobante generarNota(Venta venta, Cliente cliente, DatosEmisor emisor) {
		Comprobante comprobante = new Comprobante();
		comprobante.setVersion("3.3");
		comprobante.setFecha(Util.getXMLDate(new Date(), FormatoFecha.COMPROBANTE));
		comprobante.setMoneda(new C_Moneda("MXN"));
		comprobante.setLugarExpedicion(new C_CodigoPostal(emisor.getDomicilioFiscal().getCodigoPostal()));
		comprobante.setTipoDeComprobante(new C_TipoDeComprobante("I"));
		comprobante.setFormaPago(new C_FormaDePago(this.getFormaPago(venta.getFormaDePago())));
		comprobante.setMetodoPago(new C_MetodoDePago("PUE"));
		
		comprobante.setEmisor(construirEmisor(emisor));
		comprobante.setReceptor(construirReceptor(cliente));
		if(venta.getDetalles()!=null){
			construirConceptos(venta.getDetalles(), comprobante);
		}
		//comprobante.setImpuestos(construirImuestos(comprobante.getSubTotal()));
//		BigDecimal total = comprobante.getSubTotal().add(comprobante.getImpuestos().getTotalImpuestosTrasladados());
		comprobante.setTotal(new BigDecimal(venta.getMonto()).setScale(2, RoundingMode.HALF_UP));

		return comprobante;
	}
	
	private Emisor construirEmisor(DatosEmisor de) {
		Emisor emisor = new Comprobante.Emisor();
		emisor.setRfc(de.getRfc());
		emisor.setNombre(de.getNombre());
		emisor.setRegimenFiscal(new C_RegimenFiscal("601"));
		
		return emisor;
	}
	
	private Receptor construirReceptor(Cliente cliente) {
		Receptor receptor = new Comprobante.Receptor();
		if(cliente!=null){
		receptor.setRfc(cliente.getRfc());
		receptor.setNombre(cliente.getNombre());
		receptor.setUsoCFDI(new C_UsoCFDI("P01"));
		}
		return receptor;
	}
	
	private void construirConceptos(List<Detalle> detalleVenta, Comprobante c) {
		Conceptos conceptos = new Conceptos();
		BigDecimal subtotal = new BigDecimal(0);
		double importeTotalIVA = 0;
		for (Detalle detalle : detalleVenta) {
			Concepto concepto = new Concepto();
			int cantidad = detalle.getCantidad();
			concepto.setNoIdentificacion( Long.toString( detalle.getIdProducto() ) );
			concepto.setCantidad( BigDecimal.valueOf( (long)cantidad ) );
			concepto.setUnidad(detalle.getUnidad());
			concepto.setClaveUnidad(new C_ClaveUnidad(detalle.getClaveUnidad()));
			concepto.setClaveProdServ(detalle.getClaveSat());
			concepto.setDescripcion(detalle.getDescripcion());
			
			double valorUnitarioSinIVA = detalle.getPrecioUnitario() / 1.16;
			double importeIVA = valorUnitarioSinIVA * 0.16 * cantidad;
			double importe = valorUnitarioSinIVA * cantidad;
			
			concepto.setValorUnitario( BigDecimal.valueOf( (double)valorUnitarioSinIVA ).setScale(2, RoundingMode.HALF_UP) );
			concepto.setImporte( BigDecimal.valueOf( (double)importe ).setScale(2, RoundingMode.HALF_UP) );
			
			Comprobante.Conceptos.Concepto.Impuestos impuestos= new Comprobante.Conceptos.Concepto.Impuestos();
			Comprobante.Conceptos.Concepto.Impuestos.Traslados traslados= new Comprobante.Conceptos.Concepto.Impuestos.Traslados();
			Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado traslado= new Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado();
			
			traslado.setBase(new BigDecimal(importe).setScale(2, RoundingMode.HALF_UP));
			traslado.setImporte(new BigDecimal(importeIVA).setScale(2, RoundingMode.HALF_UP));
			traslado.setTasaOCuota(new BigDecimal(0.16).setScale(2, RoundingMode.HALF_UP));
			traslado.setTipoFactor(new C_TipoFactor("Tasa"));
			traslado.setImpuesto(new C_Impuesto("002"));
			traslados.getTraslado().add(traslado);
			impuestos.setTraslados(traslados);
			concepto.setImpuestos(impuestos);
			
			conceptos.getConcepto().add(concepto);
			subtotal = subtotal.add(concepto.getImporte());
			importeTotalIVA += importeIVA;
		}
		
		c.setSubTotal(subtotal);
		c.setConceptos(conceptos);
		c.setImpuestos(construirImuestos(BigDecimal.valueOf(importeTotalIVA)));
	}
	
	private Impuestos construirImuestos(BigDecimal importe) {
		Impuestos impuestos = new Impuestos();
		Traslados traslados = new Traslados();
		
		Traslado traslado = new Traslado();
		traslado.setImpuesto(new C_Impuesto("IVA"));
		traslado.setTasaOCuota(new BigDecimal(0.16).setScale(2, RoundingMode.HALF_UP));
		traslado.setTipoFactor(new C_TipoFactor("Tasa"));
		traslado.setImpuesto(new C_Impuesto("002"));
		traslado.setImporte(importe.setScale(2, RoundingMode.HALF_UP));
		
		traslados.getTraslado().add(traslado);
		impuestos.setTraslados(traslados);
		impuestos.setTotalImpuestosTrasladados(traslado.getImporte());
		return impuestos;
	}
	
	private String getFormaPago(String fp){
		switch(fp){
		case "Tarjeta de Crédito":{
			return "04";
		}
		case "Tarjeta de Débito":{
			return "28";
		}
		case "Efectivo":{
			return "01";
		}
		case "Transferencia":{
			return "03";
		}
		case "Cheque":{
			return "02";
		}
		}
		return "";
	}
	
	
}
