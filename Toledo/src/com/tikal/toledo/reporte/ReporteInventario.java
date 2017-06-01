package com.tikal.toledo.reporte;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;

public class ReporteInventario {

	public HSSFWorkbook getReporte(List<Producto> productos, List<Tornillo> tornillos){
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "Hoja excel");

		String[] headers = new String[] { "ID", "CLAVE", "NOMBRE", "MEDIDAS", "INVENTARIO"};
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
        
        List<ReporteRenglon> renglones= new ArrayList<ReporteRenglon>();
        for(Producto p: productos){
        	renglones.add(new ReporteRenglon(p));
        }
        for(Tornillo t: tornillos){
        	renglones.add(new ReporteRenglon(t));
        }
        for(int i=0; i<renglones.size();i++){
        	ReporteRenglon reng= renglones.get(i);
        	HSSFRow dataRow = sheet.createRow(i + 1);
        	reng.llenarRenglon(dataRow);
        	
        }
//        sheet.setColumnWidth(0, 13*256);
//        sheet.setColumnWidth(1, 35*256);
//        sheet.setColumnWidth(3, 15*256);
//        sheet.setColumnWidth(4, 20*256);
//        sheet.setColumnWidth(5, 25*256);
//        sheet.setColumnWidth(6, 40*256);
//        sheet.setColumnWidth(7, 13*256);
//        sheet.setColumnWidth(8, 13*256);
//        sheet.setColumnWidth(9, 13*256);
//        sheet.setColumnWidth(10, 20*256);
		return workbook;
	}
}
