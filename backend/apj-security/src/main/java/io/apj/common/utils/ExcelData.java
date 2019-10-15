package io.apj.common.utils;

import java.io.Serializable;
import java.util.List;

public class ExcelData implements Serializable {

	private static final long serialVersionUID = 4444017239100620999L;

	/**
	 * 表头
	 */
	private List<String> titles;

	/**
	 * 数据
	 */
	private List<List<Object>> rows;

	/**
	 * 页签名称
	 */
	private String name;

	/**
	 * 获取表头
	 */
	public List<String> getTitles() {
		return titles;
	}

	/**
	 * 设置表头
	 */
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	/**
	 * 获取数据
	 */
	public List<List<Object>> getRows() {
		return rows;
	}

	/**
	 * 设置数据
	 */
	public void setRows(List<List<Object>> rows) {
		this.rows = rows;
	}

	/**
	 * 获取页签名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置页签名称
	 */
	public void setName(String name) {
		this.name = name;
	}

}