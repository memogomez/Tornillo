package com.tikal.toledo.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.DatosEmisor;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.sat.cfd.Comprobante;
import com.tikal.toledo.sat.cfd.Comprobante.Conceptos;
import com.tikal.toledo.sat.cfd.Comprobante.Conceptos.Concepto;
import com.tikal.toledo.sat.cfd.Comprobante.Emisor;
import com.tikal.toledo.sat.cfd.Comprobante.Emisor.RegimenFiscal;
import com.tikal.toledo.sat.cfd.Comprobante.Impuestos;
import com.tikal.toledo.sat.cfd.Comprobante.Impuestos.Traslados;
import com.tikal.toledo.sat.cfd.Comprobante.Impuestos.Traslados.Traslado;
import com.tikal.toledo.sat.cfd.Comprobante.Receptor;
import com.tikal.toledo.util.Util;

/**
 * @author Tikal
 *
 */
@Service
public class ComprobanteVentaFactory {
	
	
	public Comprobante generarFactura(Venta venta, Cliente cliente, DatosEmisor emisor) {
		Comprobante comprobante = new Comprobante();
		comprobante.setVersion("3.2");
		comprobante.setFecha(Util.getXMLDate(new Date(), FormatoFecha.COMPROBANTE));
		comprobante.setMoneda("MXN");
		comprobante.setLugarExpedicion("Toluca, México"); //TODO agregar ciudad
		comprobante.setTipoDeComprobante("ingreso");
		comprobante.setMetodoDePago(venta.getFormaDePago()); //hardcoded
		comprobante.setFormaDePago("Pago en una sola exhibición"); //hardcoded
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
		comprobante.setVersion("3.2");
		comprobante.setFecha(Util.getXMLDate(new Date(), FormatoFecha.COMPROBANTE));
		comprobante.setMoneda("MXN");
		comprobante.setLugarExpedicion("Toluca, México"); //TODO agregar ciudad
		comprobante.setTipoDeComprobante("");
		comprobante.setMetodoDePago(""); //hardcoded
		comprobante.setFormaDePago(""); //hardcoded
		
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
		Emisor emisor = new Emisor();
		emisor.setRfc(de.getRfc());
		emisor.setNombre(de.getNombre());
		emisor.setDomicilioFiscal(de.getDomicilioFiscal());
		RegimenFiscal regimenFiscal = new RegimenFiscal();
		regimenFiscal.setRegimen(de.getRegimen());
		emisor.getRegimenFiscal().add(regimenFiscal);
		return emisor;
	}
	
	private Receptor construirReceptor(Cliente cliente) {
		Receptor receptor = new Receptor();
		if(cliente!=null){
		receptor.setRfc(cliente.getRfc());
		receptor.setNombre(cliente.getNombre());
		receptor.setDomicilio(cliente.getDomicilio());
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
			concepto.setUnidad("PZA");
			concepto.setDescripcion(detalle.getDescripcion());
			
			double valorUnitarioSinIVA = detalle.getPrecioUnitario() / 1.16;
			double importeIVA = valorUnitarioSinIVA * 0.16 * cantidad;
			double importe = valorUnitarioSinIVA * cantidad;
			
			concepto.setValorUnitario( BigDecimal.valueOf( (double)valorUnitarioSinIVA ).setScale(2, RoundingMode.HALF_UP) );
			concepto.setImporte( BigDecimal.valueOf( (double)importe ).setScale(2, RoundingMode.HALF_UP) );
			
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
		traslado.setImpuesto("IVA");
		traslado.setTasa(BigDecimal.valueOf(0.16));
		traslado.setImporte(importe.setScale(2, RoundingMode.HALF_UP));
		
		traslados.getTraslado().add(traslado);
		impuestos.setTraslados(traslados);
		impuestos.setTotalImpuestosTrasladados(traslado.getImporte());
		return impuestos;
	}
}
