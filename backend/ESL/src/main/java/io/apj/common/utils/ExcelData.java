package io.apj.common.utils;

import java.io.Serializable;
import java.util.List;

public class ExcelData implements Serializable {

    private static final long serialVersionUID = 4444017239100620999L;

    // 表头
    private List<String> titles;

    // 数据
    private List<List<Object>> rows;

    // 页签名称
    private String name;
    
    // 子表数据表头
    private List<String> subTitles;
    // 子表数据
    private List<List<Object>> subRows;

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public List<String> getSubTitles() {
		return subTitles;
	}

	public void setSubTitles(List<String> subTitles) {
		this.subTitles = subTitles;
	}

	public List<List<Object>> getSubRows() {
		return subRows;
	}

	public void setSubRows(List<List<Object>> subRows) {
		this.subRows = subRows;
	}
}