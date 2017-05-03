package com.tikal.toledo.util;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.model.Tornillo;

public class Parseador {
	
	public static List<Tornillo> parsear(String cadena){
		List<Tornillo> lista= new ArrayList<Tornillo>();
		cadena=cadena.replace("<P>TOLEDO \n</P>", "");
		cadena=cadena.replace("<P>TOLEDO \nTOLEDO \n</P>", "");
		String[] primeros=cadena.split("<TD>CLAVE");
		
		for(int i=1;i< primeros.length; i++){
			String s= primeros[i];
			lista.addAll(parseaProducto(s));
		}
		
		return lista;
	}
	
	private static List<Tornillo> parseaProducto(String s){
		List<Tornillo> lista= new ArrayList<Tornillo>();
		s = s.substring(s.indexOf("<TR>"));
		s = s.substring(s.indexOf("<TD>")+4);
		String nombre = s.substring(0, s.indexOf("</TD>"));
		s=s.substring(s.indexOf("<TR>"));
		String[] elementos= s.split("<TR>");
		for(String el:elementos){
			if(el.contains("<TH/>")||(el.split("<TD/>").length>2)){
				if(el.split("<TD/>").length<4){
				nombre = el.substring(el.indexOf("<TH>")+4,el.indexOf("</TH>"));}
				continue;
			}
			
			if(el.contains("<TH>CLAVE")){
				continue;
			}
			System.out.println(el);
			if(el.length()>0){
			Tornillo t= new Tornillo();
			el=el.replace("TH", "TD");
			el=el.replace("</TR>", "");
			el=el.replace("</TD>", "");
			el=el.replace("<TD/>", "");
			el=el.replace("\r", "");
			el=el.replace("\n", "");
			el=el.replace("<P>", "");
			el=el.replace("</P>", "");
			el=el.replace("TOLEDO", "");
			String[] atts= el.split("<TD>");
			if(atts.length>1){
			t.setNombre(nombre);
			t.setClave(atts[1]);
			t.setMedidas(atts[2]);
			t.setPrecioMostrador(Float.parseFloat(atts[3].replace("$", "").replace(",", "").replace("</Table>","").replace("</Sect>", "").replace("<Sect>", "").replace("<Table>", "")));
			lista.add(t);
			}
			}
			
		}
		
		
		return lista;
	}

}
