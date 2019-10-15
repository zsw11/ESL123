package io.apj.modules.basic.entity;

import java.util.ArrayList;
import java.util.List;

public class FilterEntity {
	String code;
	String title;
	String type;
	String column;
	List<?> params = new ArrayList<>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<?> getParams() {
		return params;
	}

	public void setParams(List<?> params) {
		this.params = params;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
}
