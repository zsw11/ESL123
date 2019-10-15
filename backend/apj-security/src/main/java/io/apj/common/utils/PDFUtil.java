
package io.apj.common.utils;

import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Locale;

/**
 * PDF工具类
 * 
 * @author sam
 *
 */
public class PDFUtil {

	private static String DEFAULT_ENCODING = "utf-8";
	private static String PDF_TYPE = "application/pdf";
	private static boolean DEFAULT_NOCACHE = true;
	private static String HEADER_ENCODING = "utf-8";
	private static String HEADER_NOCACHE = "no-cache";

	/**
	 * 生成PDF文件流
	 * 
	 * @param ftlName 文件名称
	 * @param root    数据
	 * @return ByteArrayOutputStream
	 * @throws Exception
	 */
	public static ByteArrayOutputStream createPDF(String ftlName, Object root) throws Exception {
		// 绝对路径
		File file = new File(Constant.HTML_PATH);
		Configuration cfg = new Configuration();
		try {
			cfg.setLocale(Locale.CHINA);
			cfg.setEncoding(Locale.CHINA, "UTF-8");
			// 设置编码
			cfg.setDefaultEncoding("UTF-8");
			// 设置模板路径
			cfg.setDirectoryForTemplateLoading(file);
			// 获取模板
			Template template = cfg.getTemplate(ftlName);
			template.setEncoding("UTF-8");
			ITextRenderer iTextRenderer = new ITextRenderer();
			// 设置字体
			ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
			fontResolver.addFont(file.getPath() + "/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 加粗字体
			// fontResolver.addFont(file.getPath() + "/font/simsunbd.ttf",
			// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Writer writer = new StringWriter();
			// 数据填充模板
			template.process(root, writer);
			// 设置输出文件内容及路径
			String str = writer.toString();
			iTextRenderer.setDocumentFromString(str);
			/* iTextRenderer.getSharedContext().setBaseURL("");//共享路径 */
			iTextRenderer.layout();
			// 生成PDF
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			iTextRenderer.createPDF(baos);
			baos.close();
			return baos;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static void renderPdf(HttpServletResponse response, final byte[] bytes, final String filename) {
		initResponseHeader(response, PDF_TYPE);
//		setFileDownloadHeader(response, filename, ".pdf");
		if (null != bytes) {
			try {
				response.getOutputStream().write(bytes);
				response.getOutputStream().flush();
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * 分析并设置contentType与headers.
	 */
	private static HttpServletResponse initResponseHeader(HttpServletResponse response, final String contentType,
			final String... headers) {
		// 分析headers参数
		String encoding = DEFAULT_ENCODING;
		boolean noCache = DEFAULT_NOCACHE;
		for (String header : headers) {
			String headerName = StringUtils.substringBefore(header, ":");
			String headerValue = StringUtils.substringAfter(header, ":");
			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
				encoding = headerValue;
			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
				noCache = Boolean.parseBoolean(headerValue);
			} else {
				throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
			}
		}
		// 设置headers参数
		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType(fullContentType);
		if (noCache) {
			// Http 1.0 header
			response.setDateHeader("Expires", 0);
			response.addHeader("Pragma", "no-cache");
			// Http 1.1 header
			response.setHeader("Cache-Control", "no-cache");
		}
		return response;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName, String fileType) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + fileType + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}
}
