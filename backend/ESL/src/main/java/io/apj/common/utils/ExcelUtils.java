package io.apj.common.utils;


import com.alibaba.excel.ExcelWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExcelUtils {
    public static <T> void mergeCell(Map<Integer, Function<T, Object>> options, List<T> items, ExcelWriter excelWriter, int startRow) {
        for (Integer cellNum : options.keySet()) {
            int firstRow = startRow;
            int lastRow = startRow;
            Function<T, Object> codeProvider = options.get(cellNum);
            for (int i = 1; i < items.size(); i++) {
                String previous = codeProvider.apply(items.get(i - 1)).toString();
                String current = codeProvider.apply(items.get(i)).toString();
                if (StringUtils.equals(previous, current) && lastRow != startRow + items.size() - 2) {
                    lastRow++;
                    continue;
                } else {
                    lastRow++;
                }
                if (lastRow == firstRow) {
                    lastRow++;
                    firstRow++;
                    continue;
                }

                excelWriter.merge(firstRow, lastRow, cellNum, cellNum);
                firstRow = lastRow;

            }
        }
    }


}
