package io.apj.common.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class PathUtil {
    public static String getExcelTemplatePath(String templateFileName) throws IOException {
        return getResourcesPath() + File.separator + templateFileName + ".xls";
    }

    public static String getResourcesPath() throws IOException {
        return new ClassPathResource("static/exportTemplates").getURL().getPath();
    }
}
