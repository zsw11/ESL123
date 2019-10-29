package io.apj.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.io.IORuntimeException;

public class FileUtil {
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {

		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		URLEncoder.encode(fileName, "utf-8");
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}

	public static BufferedOutputStream getOutputStream(String path) throws IORuntimeException {
		return getOutputStream(path);
	}

	/**
	 * 根据文件路径删除本地文件
	 * 
	 * @param path
	 * @return Map<String, Object>
	 * @throws FileNotFoundException
	 */
	public static Map<String, Object> deleteByFilePath(String path) throws FileNotFoundException {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			File file = new File(path);
			if (file.exists()) {
				if (file.delete()) {
					System.out.println("1-删除成功!");
					params.put("status", 200);
					params.put("msg", "1-删除成功!");
				} else {
					System.out.println("0-删除失败!");
					params.put("status", 500);
					params.put("msg", "0-删除失败!");
				}
			} else {
				System.out.println("文件不存在！");
				params.put("status", 404);
				params.put("msg", "文件不存在！");
			}
			return params;
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.toString());
			params.put("status", 500);
			params.put("msg", e.toString());
			return params;
		}
	}
}
