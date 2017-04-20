package com.tikal.toledo;

import com.googlecode.objectify.ObjectifyService;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Perfil;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Proveedor;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.model.Usuario;
import com.tikal.toledo.model.Venta;
public class Register {
	public Register(){
		ObjectifyService.register(Cliente.class);
		ObjectifyService.register(Lote.class);
		ObjectifyService.register(Producto.class);
		ObjectifyService.register(Tornillo.class);
		ObjectifyService.register(Venta.class);
		ObjectifyService.register(Proveedor.class);
		ObjectifyService.register(Usuario.class);
		ObjectifyService.register(Perfil.class);
	}
}
