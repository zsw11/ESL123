package io.apj.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class PathUtil {
	public static String getExcelTemplatePath(String templateFileName) throws IOException {
		System.out.println(new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile()
				.getParent().replace("\\ESL\\backend", "") + File.separator + "exportTemplates" + File.separator
				+ templateFileName + ".xls" + "     4444444444444444");
		return new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent()
				.replace("\\ESL\\backend", "") + File.separator + "exportTemplates" + File.separator + templateFileName
				+ ".xls";
	}

	public static String getResourcesPath() throws IOException {
		return new ClassPathResource("static/exportTemplates").getURL().getPath();
	}
}
