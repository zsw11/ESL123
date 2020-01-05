package io.apj.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * 
 * @author samchen
 * @date 20190126
 *
 */
public class ExportExcelByTemplateUtils {

	/**
	 * 根据excel模板导出excel
	 * 
	 * @param response
	 * @param file
	 * @param list
	 * @throws IOException
	 */
	public static void exportExcel(HttpServletResponse response, File file, Map<String, Object> map, String path)
			throws IOException {

		// 下载路径
		createDir(new File(path));

		// 根据模板生成新的excel
		File excelFile = createNewFile(map, file, path);

		// 浏览器端下载文件
		downloadFile(response, excelFile);

		// 删除服务器生成文件
		deleteFile(excelFile);

	}

	/**
	 * 根据excel模板生成新的excel
	 *
	 * @param beans
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static File createNewFile(Map<String, Object> beans, File file, String path) throws IOException {
		XLSTransformer transformer = new XLSTransformer();

		// 可以写工具类来生成命名规则

		String fileName = new Date().getTime() + ".xlsx";
		File newFile = new File(path + fileName);

		// 服务器以流的形式读取文件
		ClassPathResource resource = new ClassPathResource("static/templates/payment.xlsx");

		try (InputStream in = new BufferedInputStream(
				file == null ? resource.getInputStream() : new FileInputStream(file));
				OutputStream out = new FileOutputStream(newFile)) {
			Workbook workbook = transformer.transformXLS(in, beans);
			workbook.write(out);
			out.flush();
			return newFile;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return newFile;
	}

	/**
	 * 将服务器新生成的excel从浏览器下载
	 *
	 * @param response
	 * @param excelFile
	 */
	private static void downloadFile(HttpServletResponse response, File excelFile) {
		/* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
		response.setContentType("multipart/form-data");
		/* 设置文件头：最后一个参数是设置下载文件名 */
		response.setHeader("Content-Disposition", "attachment;filename=" + excelFile.getName());
		try (InputStream ins = new FileInputStream(excelFile); OutputStream os = response.getOutputStream()) {
			byte[] b = new byte[1024];
			int len;
			while ((len = ins.read(b)) > 0) {
				os.write(b, 0, len);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 浏览器下载完成之后删除服务器生成的文件 也可以设置定时任务去删除服务器文件
	 *
	 * @param excelFile
	 */
	private static void deleteFile(File excelFile) {
		excelFile.delete();
	}

	// 如果目录不存在创建目录 存在则不创建
	private static void createDir(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
	}

}