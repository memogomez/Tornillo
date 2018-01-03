package com.tikal.cacao.factura;

public class ValidadorFactura {
	
	public static double calcularLimiteInferior(double base, double tasaOCuota) {
		String numDecimalesBase = getNumDecimalesBase(base);
		double limInferior = (base - Math.pow( 10, -numDecimalesBase.length() ) / 2) * tasaOCuota;
		return limInferior;
	}
	
	public static double calcularLimiteSuperior(double base, double tasaOCuota) {
		String numDecimalesBase = getNumDecimalesBase(base);
		double limSuperior = ( base + Math.pow( 10, -numDecimalesBase.length() ) / 2 -
				Math.pow(10, -12) ) * tasaOCuota;
		return limSuperior;
	}
	
	private static String getNumDecimalesBase(double base) {
		String[] splitBase= String.valueOf(base).split("\\.");
		return splitBase[1];
	}

}
