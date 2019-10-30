package io.apj.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author samchen
 * @date 20181213
 *
 */
public class ImportExcelUtils {

	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * 
	 * @param file excel文件
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> readExcelData(MultipartFile file) throws IOException {
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 判断文件格式是否正确
		String fileName = file.getOriginalFilename();
		if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
			System.out.println("=======>>>>>>上传文件格式不正确");
			map.put("status", 403);
			map.put("message", "上传文件格式不正确!");
			return map;
		}
		boolean isExcel2003 = true;
		if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		InputStream is = file.getInputStream();
		Workbook wb = null;
		// 判断excel版本
		if (isExcel2003) {
			wb = new HSSFWorkbook(is);
		} else {
			wb = new XSSFWorkbook(is);
		}
		// 获取页签
		Sheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			System.out.println("=======>>>>>>上传文件数据不能为空!");
			map.put("status", 403);
			map.put("message", "上传文件数据不能为空!");
			return map;
		}

		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
		for (int i = 1; i <= rowNum; i++) {
			int j = 0;
			Map<Integer, String> rowMap = new HashMap<Integer, String>();
			Row row = sheet.getRow(i);

			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				rowMap.put(j, getCellFormatValue(row.getCell((short) j)).trim().replaceAll("\t\r", ""));
				j++;
			}
			list.add(rowMap);
		}
		map.put("list", list);
		map.put("status", 200);
		return map;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case Cell.CELL_TYPE_NUMERIC:
			case Cell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式
					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();
					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case Cell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;
	}

}