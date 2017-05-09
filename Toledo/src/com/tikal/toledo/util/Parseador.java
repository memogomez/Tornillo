package com.tikal.toledo.util;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.model.Tornillo;

public class Parseador {
	
	public static List<Tornillo> parsear(String cadena){
		List<Tornillo> lista= new ArrayList<Tornillo>();
		cadena=cadena.replace("<P>TOLEDO \n</P>", "");
		cadena=cadena.replace("&quot;", "\"");
		cadena=cadena.replace("<P>TOLEDO \nTOLEDO \n</P>", "");
		cadena=cadena.replace("<TH>CLAVE", "<TD>CLAVE");
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
			System.out.println(el);
			if(el.contains("<TH/>")||(el.split("<TD/>").length>2)){
				if(el.split("<TD/>").length<4){
					String aux= el.replace("<TD", "<TH");
					aux=aux.replace("</TD", "</TH");
				nombre = aux.substring(aux.indexOf("<TH>")+4,aux.indexOf("</TH>"));}
				continue;
			}
			
			if(el.contains("<TH>CLAVE")){
				continue;
			}
			
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
			t.setNombre(nombre.trim());
			t.setClave(getClave(nombre));
			t.setMedidas(atts[2].trim());
			t.setPrecioMostrador(Float.parseFloat(atts[3].replace("$", "").replace(",", "").replace("</Table>","").replace("</Sect>", "").replace("<Sect>", "").replace("<Table>", "")));
			lista.add(t);
			}
			}
			
		}
		
		
		return lista;
	}
	
	private static String getClave(String nombre){
		String clave="";
		nombre = nombre.replaceAll("\\."," ");
		nombre = nombre.trim();
		String[] palabras= nombre.split(" ");
		for(String palabra:palabras){
			if(palabra.length()>0){
			if(palabra.matches ("^.*\\d.*$")){
				clave+=palabra;
			}else{
				clave+= palabra.substring(0, 1);
			}
			}
		}
		return clave;
	}

}
