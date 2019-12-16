package io.apj.common.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class PathUtil {
    public static String getExcelTemplatePath(String templateFileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/exportTemplates/" + templateFileName + ".xls");
        return resource.getURL().getPath();
    }
}
