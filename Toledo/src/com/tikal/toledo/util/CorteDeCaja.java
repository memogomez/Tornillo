package com.tikal.toledo.util;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.tikal.toledo.model.Venta;

public class CorteDeCaja {
	private List<Venta> ventas;

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}
	
	public HSSFWorkbook getReporte() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "Hoja excel");

		String[] headers = new String[] { "Cliente", "Facturado", "Forma de Pago","Importe"};
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
        }
        float totalCaja=0;
        for(int i=0; i<this.ventas.size();i++){
        	HSSFRow dataRow = sheet.createRow(i + 1);
        	Venta v= ventas.get(i);
        	v.llenarRenglon(dataRow);
        	if(v.getFormaDePago()!=null){
        		if(v.getFormaDePago().compareToIgnoreCase("Efectivo")==0){
        			totalCaja+=v.getMonto();
        		}
        	}
        	
        }
        HSSFRow dataRow = sheet.createRow(ventas.size()+3);
        dataRow.createCell(0).setCellValue("Total en Caja");
        dataRow.createCell(1).setCellValue(totalCaja);
        sheet.setColumnWidth(0, 13*256);
        sheet.setColumnWidth(1, 35*256);
        sheet.setColumnWidth(2, 10*256);
        sheet.setColumnWidth(3, 15*256);
//        sheet.setColumnWidth(4, 25*256);
//        sheet.setColumnWidth(5, 20*256);
//        sheet.setColumnWidth(6, 25*256);
//        sheet.setColumnWidth(7, 40*256);
//        sheet.setColumnWidth(8, 13*256);
//        sheet.setColumnWidth(9, 13*256);
//        sheet.setColumnWidth(10, 13*256);
//        sheet.setColumnWidth(11, 20*256);
		return workbook;
	}
	
}
