package io.apj.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传工具栏
 * 
 * @author sam
 * @date 2018-12-20
 *
 */
public class UploadImgUtils {

	/**
	 * 最大图片文件限制 (单位：/kb)
	 */
	public static Long IMG_MAX_SIZE = (long) 50 * 1000 * 1024;

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 */
	public static void judeFileExists(File file) {

		if (file.exists()) {
			System.out.println("file exists");
		} else {
			System.out.println("file not exists, create it ...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param file
	 */
	public static void judeDirExists(File file) {

		if (file.exists()) {
			if (file.isDirectory()) {
				System.out.println("dir exists");
			} else {
				System.out.println("the same name file exists, can not create dir");
			}
		} else {
			System.out.println("dir not exists, create it ...");
			file.mkdir();
		}

	}

	/**
	 * 验证图片格式
	 */
	public static boolean patternImg(String fileName) {
		String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(fileName);

		return matcher.find();
	}

	/**
	 * 仅支持图片格式文件上传
	 * 
	 * @throws FileNotFoundException
	 * 
	 */
	public static Map<String, Object> getUrlByUploadFile(MultipartFile file, String filePath)
			throws FileNotFoundException {
		Map<String, Object> param = new HashMap<String, Object>();
		// 指定本地文件夹存储图片
//		if (!StringUtils.isNotEmpty(filePath)) {
//			judeDirExists(new File(Constant.READ_PATH));
//		} else {
		filePath = Constant.READ_PATH;
		judeDirExists(new File(filePath));
//		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 校验文件格式
		if (!patternImg(fileName)) {
			param.put("status", 403);
			param.put("msg", "仅支持上传图片格式文件！");
			return param;
		}
		// 文件大小
		Long size = file.getSize();
		if (size > IMG_MAX_SIZE) {
			param.put("status", 403);
			param.put("msg", "图片文件大小超过限制！");
			return param;
		}
		// 重新生成文件名
		fileName = UUID.randomUUID() + suffixName;
		try {
			// 将图片保存到static文件夹里
			// file.transferTo(new File(filePath + fileName));
			FileOutputStream out = new FileOutputStream(filePath + fileName);
			out.write(file.getBytes());
			out.flush();
			out.close();
			param.put("status", 200);
			param.put("fileName", fileName);
			return param;
		} catch (Exception e) {
			e.printStackTrace();
			param.put("status", 500);
			param.put("msg", "未知异常错误！");
			return param;
		}
	}

	/**
	 * 根据文件路径删除本地文件
	 * 
	 * @param path
	 * @return Map<String, Object>
	 * @throws FileNotFoundException
	 */
	public static Map<String, Object> deleteByFilePath(String path) throws FileNotFoundException {

		String[] files = path.split("resources-img");
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			File file = new File(Constant.READ_PATH + files[1]);
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
