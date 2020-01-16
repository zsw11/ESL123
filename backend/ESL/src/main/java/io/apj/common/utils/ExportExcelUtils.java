package io.apj.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author samchen
 * @date 20181213
 *
 */
public class ExportExcelUtils {

	/**
	 * 导出excel
	 *
	 * @param response
	 * @param fileName
	 * @param data
	 * @throws Exception
	 */
	public static void exportExcel(HttpServletResponse response, String fileName, ExcelData data) throws Exception {
		exportExcel(data, response.getOutputStream());
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 设置强制下载不打开
		response.setContentType("application/force-download");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
	}

	public static void exportExcel(ExcelData data, OutputStream out) throws Exception {

		XSSFWorkbook wb = new XSSFWorkbook();
		try {
			String sheetName = data.getName();
			if (null == sheetName) {
				sheetName = "Sheet1";
			}
			XSSFSheet sheet = wb.createSheet(sheetName);
			writeExcel(wb, sheet, data);

			wb.write(out);
		} finally {
			out.close();
		}
	}

	private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data) {

		int rowIndex = 0;

		rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles(),rowIndex);
		writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
		if (data.getSubTitles() != null) {
			rowIndex += data.getRows().size()+1;
			rowIndex = writeTitlesToExcel(wb, sheet, data.getSubTitles(),rowIndex);
			writeRowsToExcel(wb, sheet, data.getSubRows(), rowIndex);
		}
		autoSizeColumns(sheet, data.getTitles().size() + 1);

	}

	private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles, int rowIndex) {
		int colIndex = 0;

		Font titleFont = wb.createFont();
		titleFont.setFontName("simsun");
//        titleFont.setBold(true);
		// titleFont.setFontHeightInPoints((short) 14);
		titleFont.setColor(IndexedColors.BLACK.index);

		XSSFCellStyle titleStyle = wb.createCellStyle();
//		titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);  切换poi版本
//		titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); 切换poi版本
		titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
//		titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 切换poi版本
		titleStyle.setFont(titleFont);
		setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

		Row titleRow = sheet.createRow(rowIndex);
		// titleRow.setHeightInPoints(25);
		colIndex = 0;

		for (String field : titles) {
			Cell cell = titleRow.createCell(colIndex);
			cell.setCellValue(field);
			cell.setCellStyle(titleStyle);
			colIndex++;
		}

		rowIndex++;
		return rowIndex;
	}

	private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
		int colIndex = 0;

		Font dataFont = wb.createFont();
		dataFont.setFontName("simsun");
		// dataFont.setFontHeightInPoints((short) 14);
		dataFont.setColor(IndexedColors.BLACK.index);

		XSSFCellStyle dataStyle = wb.createCellStyle();
//		dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 切换poi版本
//		dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); 切换poi版本
		dataStyle.setFont(dataFont);
		setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

		for (List<Object> rowData : rows) {
			Row dataRow = sheet.createRow(rowIndex);
			// dataRow.setHeightInPoints(25);
			colIndex = 0;

			for (Object cellData : rowData) {
				Cell cell = dataRow.createCell(colIndex);
				if (cellData != null) {
					cell.setCellValue(cellData.toString());
				} else {
					cell.setCellValue("");
				}

				cell.setCellStyle(dataStyle);
				colIndex++;
			}
			rowIndex++;
		}
		return rowIndex;
	}

	private static void autoSizeColumns(Sheet sheet, int columnNumber) {

		for (int i = 0; i < columnNumber; i++) {
			int orgWidth = sheet.getColumnWidth(i);
			sheet.autoSizeColumn(i, true);
			int newWidth = (int) (sheet.getColumnWidth(i) + 100);
			if (newWidth > orgWidth) {
				sheet.setColumnWidth(i, newWidth);
			} else {
				sheet.setColumnWidth(i, orgWidth);
			}
		}
	}

	private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
		style.setBorderTop(border);
		style.setBorderLeft(border);
		style.setBorderRight(border);
		style.setBorderBottom(border);
		style.setBorderColor(BorderSide.TOP, color);
		style.setBorderColor(BorderSide.LEFT, color);
		style.setBorderColor(BorderSide.RIGHT, color);
		style.setBorderColor(BorderSide.BOTTOM, color);
	}

	/**
	 * 复制sheet
	 * @param paths
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	public static void exportExcel(List<String> paths, HttpServletResponse response, String fileName) throws IOException {
		// 告诉浏览器用什么软件可以打开此文件
		response.setContentType("application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

		response.setCharacterEncoding("utf-8");
		response.setHeader("filename",  URLEncoder.encode(fileName, "utf-8") + ".xls");
		response.setHeader("Access-Control-Expose-Headers", "filename");

		exportMergedExcel(paths, response.getOutputStream());

//		FileUtil.deleteBatchByFilePaths(paths);
	}

	public static void exportMergedExcel(List<String> paths, OutputStream out) throws IOException {
		HSSFWorkbook newExcelCreat = new HSSFWorkbook();
		try {

			for (String fromExcelName : paths) {
				InputStream in = new FileInputStream(fromExcelName);
				String sheetName = fromExcelName.substring(fromExcelName.lastIndexOf(File.separator) + 1, fromExcelName.lastIndexOf("."));
				HSSFWorkbook fromExcel = new HSSFWorkbook(in);
				int length = fromExcel.getNumberOfSheets();
				if(length<=1){       //长度为1时
					HSSFSheet oldSheet = fromExcel.getSheetAt(0);
					HSSFSheet newSheet = newExcelCreat.createSheet(sheetName);
					copySheet(newExcelCreat, oldSheet, newSheet);
				}else{
					for (int i = 0; i < length; i++) {// 遍历每个sheet
						HSSFSheet oldSheet = fromExcel.getSheetAt(i);
						HSSFSheet newSheet = newExcelCreat.createSheet(oldSheet.getSheetName());
						copySheet(newExcelCreat, oldSheet, newSheet);
					}
				}
			}
			newExcelCreat.write(out);
		} finally {
			out.close();
		}
	}

	public static void copyCellStyle(HSSFCellStyle fromStyle, HSSFCellStyle toStyle) {

		toStyle.cloneStyleFrom(fromStyle);
	}

	/**
	 * 合并单元格
	 * @param fromSheet
	 * @param toSheet
	 */
	public static void mergeSheetAllRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {
		int num = fromSheet.getNumMergedRegions();
		CellRangeAddress cellR = null;
		for (int i = 0; i < num; i++) {
			cellR = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(cellR);
		}
	}

	/**
	 * 行复制功能
	 * @param wb
	 * @param oldRow
	 * @param toRow
	 */
	public static void copyRow(HSSFWorkbook wb, HSSFRow oldRow, HSSFRow toRow) {
		toRow.setHeight(oldRow.getHeight());
		for (Iterator cellIt = oldRow.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			HSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(wb, tmpCell, newCell);
		}
	}

	/**
	 * 复制单元格
	 * @param wb
	 * @param fromCell
	 * @param toCell
	 */
	public static void copyCell(HSSFWorkbook wb, HSSFCell fromCell, HSSFCell toCell) {
		HSSFCellStyle newstyle = wb.createCellStyle();
		copyCellStyle(fromCell.getCellStyle(), newstyle);
		//  toCell.setEncoding(fromCell.getStringCelllValue());
		// 样式
		toCell.setCellStyle(newstyle);
		if (fromCell.getCellComment() != null) {
			toCell.setCellComment(fromCell.getCellComment());
		}
		// 不同数据类型处理
		int fromCellType = fromCell.getCellType();
		toCell.setCellType(fromCellType);
		if (fromCellType == HSSFCell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(fromCell)) {
				toCell.setCellValue(fromCell.getDateCellValue());
			} else {
				toCell.setCellValue(fromCell.getNumericCellValue());
			}
		} else if (fromCellType == HSSFCell.CELL_TYPE_STRING) {
			toCell.setCellValue(fromCell.getStringCellValue());
		} else if (fromCellType == HSSFCell.CELL_TYPE_BLANK) {
			// nothing21
		} else if (fromCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
			toCell.setCellValue(fromCell.getBooleanCellValue());
		} else if (fromCellType == HSSFCell.CELL_TYPE_ERROR) {
			toCell.setCellErrorValue(fromCell.getErrorCellValue());
		} else if (fromCellType == HSSFCell.CELL_TYPE_FORMULA) {
			toCell.setCellFormula(fromCell.getCellFormula());
		} else { // nothing29
		}

	}

	/**
	 * Sheet复制
	 * @param wb
	 * @param fromSheet
	 * @param toSheet
	 */
	public static void copySheet(HSSFWorkbook wb, HSSFSheet fromSheet, HSSFSheet toSheet) {
		mergeSheetAllRegion(fromSheet, toSheet);
		// 设置列宽
		int length = fromSheet.getRow(fromSheet.getFirstRowNum()).getLastCellNum();
		for (int i = 0; i <= length; i++) {
			toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
		}
		for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
			HSSFRow oldRow = (HSSFRow) rowIt.next();
			HSSFRow newRow = toSheet.createRow(oldRow.getRowNum());
			copyRow(wb, oldRow, newRow);
		}
		//读取图片
		for (HSSFShape shape : fromSheet.getDrawingPatriarch().getChildren()) {
			if (shape instanceof HSSFPicture) {
				HSSFPicture pic = (HSSFPicture) shape;
				int pictureIndex = pic.getPictureIndex();
				HSSFClientAnchor anchor = pic.getClientAnchor();//pic.getPreferredSize();
				HSSFPatriarch patriarch = toSheet.createDrawingPatriarch();
				int index = wb.addPicture(pic.getPictureData().getData(), 2);
				patriarch.createPicture(anchor,pictureIndex);
			}
		}
	}

}
