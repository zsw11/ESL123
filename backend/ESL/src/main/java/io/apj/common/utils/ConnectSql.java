package io.apj.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectSql {
	public static String returnSql(List<Map<String, String>> list,String alias) {
		String sql = "";
		List<String> attrList = new ArrayList<>();
		for (Map<String, String> item : list) {
			String s = " and "+ alias+".";
			s = attrList.contains(item.get("attribute")) ? " or "+ alias+"." : s ;
			attrList.add(item.get("attribute"));
			switch (item.get("operation")) {
			case "like":
				sql += s + item.get("attribute") + " like '%" + item.get("value1") + "%' ";
				break;
			case "eq":
				sql += s + item.get("attribute") + " = " +item.get("value1");
				break;
			case "between":
				sql += s + item.get("attribute") + " between " +item.get("value1") + "and " +item.get("value2");
				break;
			case "ge":
				sql += s + item.get("attribute") + " >= " +item.get("value1");
				break;
			case "le":
				sql += s + item.get("attribute") + " <= " +item.get("value1");
				break;
			default:
				break;
			}
		}
		return sql;
	}
}
