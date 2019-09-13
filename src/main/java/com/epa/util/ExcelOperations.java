package com.epa.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.epa.beans.Facility.Facility860;

public class ExcelOperations {
	
	public void writeToExcel(ArrayList<Facility860> facilityList) {
		// Create a Workbook
	    Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
	
	    // Create a Sheet
	    Sheet sheet = workbook.createSheet("Facility");
	    
	    // Create a Row
	    Row headerRow = sheet.createRow(0);
	    
	    String[] columns = {"pgmSysId","primaryName","naicsCode","registryId","facAddr","cityName","stateName","postalCode",
	    		"latitude","longitude","GEOID","CountyState1","CountyState2","HUC8Code","HUC8Name","HUC8Acres"};
	
	    // Create cells
	    for(int i = 0; i < columns.length; i++) {
	        Cell cell = headerRow.createCell(i);
	        cell.setCellValue(columns[i]);
	    }
	
	    // Create Other rows and cells with employees data
	    int rowNum = 1;
	    for(Facility860 fac: facilityList) {
	        
	    	Row row = sheet.createRow(rowNum++);
	        row.createCell(0).setCellValue(fac.getPgmSysId());
	        row.createCell(1).setCellValue(fac.getPrimaryName());
	        row.createCell(2).setCellValue(fac.getNaicsCode());
	        row.createCell(3).setCellValue(fac.getRegistryId());
	        row.createCell(4).setCellValue(fac.getFacAddr());
	        row.createCell(5).setCellValue(fac.getCityName());
	        row.createCell(6).setCellValue(fac.getStateName());
	        row.createCell(7).setCellValue(fac.getPostalCode());
	        row.createCell(8).setCellValue(fac.getLatitude());
	        row.createCell(9).setCellValue(fac.getLongitude());
	        row.createCell(13).setCellValue(fac.getHUCCode());
	    }
	
		// Resize all columns to fit the content size
	    for(int i = 0; i < columns.length; i++) {
	        sheet.autoSizeColumn(i);
	    }
	
	    // Write the output to a file
	    FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("NewFacilityList.xlsx");
			workbook.write(fileOut);
		    fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
