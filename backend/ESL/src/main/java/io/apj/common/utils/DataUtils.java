package io.apj.common.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 函数工具类
 *
 * @author samchen
 *
 * @date 2018-12-10 17:39:37
 */
public class DataUtils {

	/**
	 * 根据实体返回实体名称列表
	 *
	 * @param model
	 * @return List
	 * @throws Exception
	 */
	public static List<String> entityNameList(Object model) throws Exception {

		List<String> list = new ArrayList<String>();

		for (Field field : model.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			list.add(field.getName());
		}

		return list;
	}

	/***
	 * 驼峰命名转为下划线命名
	 */

	public static String humpToUnderline(String para) {
		StringBuilder sb = new StringBuilder(para);
		int temp = 0;// 定位
		if (!para.contains("_")) {
			for (int i = 0; i < para.length(); i++) {
				if (Character.isUpperCase(para.charAt(i))) {
					sb.insert(i + temp, "_");
					temp += 1;
				}
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * java将字符串转换成可执行代码 工具类
	 *
	 * @param jexlExp
	 * @param map
	 * @return
	 */
	public static Object convertToCode(String jexlExp, Map<String, Object> map) {
		JexlEngine jexl = new JexlEngine();
		Expression expression = jexl.createExpression(jexlExp);
		JexlContext jc = new MapContext();
		for (String key : map.keySet()) {
			jc.set(key, map.get(key));
		}
		if (null == expression.evaluate(jc)) {
			return "";
		}
		return expression.evaluate(jc);
	}

	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString 原始字符串
	 * @param length      指定长度
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length) {
		int size = inputString.length() / length;
		if (inputString.length() % length != 0) {
			size += 1;
		}
		return getStrList(inputString, length, size);
	}

	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString 原始字符串
	 * @param length      指定长度
	 * @param size        指定列表大小
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length, int size) {
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < size; index++) {
			String childStr = substring(inputString, index * length, (index + 1) * length);
			list.add(childStr);
		}
		return list;
	}

	/**
	 * 分割字符串，如果开始位置大于字符串长度，返回空
	 *
	 * @param str 原始字符串
	 * @param f   开始位置
	 * @param t   结束位置
	 * @return
	 */
	public static String substring(String str, int f, int t) {
		if (f > str.length())
			return null;
		if (t > str.length()) {
			return str.substring(f, str.length());
		} else {
			return str.substring(f, t);
		}
	}

	/**
	 * 字母正则表达式
	 */
	public static String LETTER_REGULAR_EXPRESSION = "[a-zA-Z]\\w*";

	/**
	 * 根据正则表达式返回
	 *
	 * @param withinText
	 * @param regString
	 * @return 数组
	 */
	public static List<String> regMatch(String withinText, String regString) {
		List<String> list = new ArrayList<>();
		Pattern pattern = Pattern.compile(regString);
		Matcher matcher = pattern.matcher(withinText);
		if (matcher.find()) {
			matcher.reset();
			while (matcher.find()) {
				list.add(matcher.group());
			}
		}
		return list;
	}

	/**
	 * json字符串转map
	 *
	 * @param strJson
	 * @return map
	 */
	public static Map<String, Object> jsonToMap(String strJson) {
		Map<String, Object> res = null;
		try {
			Gson gson = new Gson();
			res = gson.fromJson(strJson, new TypeToken<Map<String, Object>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
		}
		return res;
	}

	/**
	 * json字符串转list
	 *
	 * @param strJson
	 * @return list
	 */
	public static List<Map<String, Object>> jsonToList(String strJson) {
		List<Map<String, Object>> res = null;
		try {
			Gson gson = new Gson();
			res = gson.fromJson(strJson, new TypeToken<List<Map<String, Object>>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
		}
		return res;
	}

	public static List<String> intersectionList(List<String> A, List<String> B) {
		List<String> list = new ArrayList<String>();
		if (A.size() > 0 && B.size() > 0) {
			A.containsAll(B);
			list.addAll(A);
		}
		return list;
	}

	/**
	 * map转javabean
	 */
	public static void transMap2Bean2(Map<String, Object> map, Object obj) {
		if (map == null || obj == null) {
			return;
		}
		try {
			map.remove("createAt");
			map.remove("updateAt");
			map.remove("deleteAt");
			BeanUtils.populate(obj, map);
		} catch (Exception e) {
			System.out.println("transMap2Bean2 Error " + e);
		}
	}

	public static Map<String, String> advancedSearchJsonReturnSQL(Map<String, Object> map, JsonArray fieldFilter) {
		Map<String, String> sqlMap = new HashMap<>();
		String sql = "select " + fieldFilter.get(0).getAsJsonObject().get("tableAlias").getAsString() + "." + "*";
		String from = " from " + fieldFilter.get(0).getAsJsonObject().get("table").getAsString() + " "
				+ fieldFilter.get(0).getAsJsonObject().get("tableAlias").getAsString() + " ";
		String as = "";
		int tableflag = 1;
		for (JsonElement jsonItem : fieldFilter) {
			if (tableflag == 1) {
				tableflag++;
				continue;
			} else {
				for (JsonElement jsonField : jsonItem.getAsJsonObject().get("fieldList").getAsJsonArray()) {
					as += "," + jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "."
							+ jsonField.getAsJsonObject().get("field").getAsString() + " as "
							+ jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "_"
							+ jsonField.getAsJsonObject().get("field").getAsString();
				}
			}
		}
		// 初始化所有表数组，并将主表添加进去
		List<String> joinTable = new ArrayList<>();
		joinTable.add(fieldFilter.get(0).getAsJsonObject().get("tableAlias").getAsString());
		String leftjoin = fieldFilter.get(0).getAsJsonObject().get("join").getAsString();
		List<Map<String, Object>> mapList = null;
		if (map.get("filterType") != null) {
			if (map.get("filterType").toString().equals("complex")) {
				mapList = (List<Map<String, Object>>) map.get("complexFilters");
			} else {
				mapList = (List<Map<String, Object>>) map.get("filters");
			}
		} else {
			mapList = (List<Map<String, Object>>) map.get("filters");
		}
		// 循环读取请求参数中的所有表别名，将所有表别名添加到joinTable
		for (Map<String, Object> item : mapList) {
			for (JsonElement jsonItem : fieldFilter) {
				if (jsonItem.getAsJsonObject().get("tableAlias").getAsString().equals(item.get("table"))) {
					if (!joinTable.contains(jsonItem.getAsJsonObject().get("tableAlias").getAsString())) {
						// sql语句增加left join表
//						leftjoin += jsonItem.getAsJsonObject().get("join").getAsString();
						joinTable.add(jsonItem.getAsJsonObject().get("tableAlias").getAsString());
					}
				}
			}
		}
		// 查询所有表中未删除的数据
		String where = " where " + fieldFilter.get(0).getAsJsonObject().get("tableAlias").getAsString();
		// 遍历所有表，添加leftjoin表不能已删除
		for (int i = 1; i < joinTable.size(); i++) {
			where += "and " + joinTable.get(i);
		}
		String afterWhere = "";
		// 循环读取请求中的参数数组
		for (Map<String, Object> item : mapList) {
			// 循环读取对应的json文件中所有表
			for (JsonElement jsonItem : fieldFilter) {
				// 判断请求中的表是否与json文件中的表相等
				if (jsonItem.getAsJsonObject().get("tableAlias").getAsString().equals(item.get("table"))) {
					// 循环读取json文件中的表中的字段是否等于请求中的字段
					for (JsonElement jsonField : jsonItem.getAsJsonObject().get("fieldList").getAsJsonArray()) {
						if (jsonField.getAsJsonObject().get("code").getAsString().equals(item.get("field"))) {
							// 判断该表的该字段是否多次出现，多次出现使用or关键字
							switch ((String) item.get("operation")) {
							case "eq":
								afterWhere += " and " + jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "."
										+ jsonField.getAsJsonObject().get("field").getAsString() + " = '"
										+ item.get("value") + "' ";
								break;
							case "like":
								// 判断字段是否附带拼音字段，有则添加模糊查询该字段的拼音
								if (jsonField.getAsJsonObject().get("pinyin") != null) {
									afterWhere += " and " + "( "
											+ jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "."
											+ jsonField.getAsJsonObject().get("field").getAsString() + " like" + " '%"
											+ item.get("value") + "%' ";
									afterWhere += " or " + jsonItem.getAsJsonObject().get("tableAlias").getAsString()
											+ "." + jsonField.getAsJsonObject().get("pinyin").getAsString() + " like"
											+ " '%" + item.get("value") + "%' )";
								} else {
									afterWhere += " and " + jsonItem.getAsJsonObject().get("tableAlias").getAsString()
											+ "." + jsonField.getAsJsonObject().get("field").getAsString() + " like"
											+ " '%" + item.get("value") + "%' ";
								}
								break;
							case "between":
								afterWhere += " and " + jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "."
										+ jsonField.getAsJsonObject().get("field").getAsString() + " between '"
										+ item.get("value") + "' and '" + item.get("value1") + "' ";
								break;
							case "ge":
								afterWhere += " and " + jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "."
										+ jsonField.getAsJsonObject().get("field").getAsString() + " >='"
										+ item.get("value") + "' ";
								break;
							case "le":
								afterWhere += " and " + jsonItem.getAsJsonObject().get("tableAlias").getAsString() + "."
										+ jsonField.getAsJsonObject().get("field").getAsString() + " <='"
										+ item.get("value") + "' ";
								break;
							default:
								break;
							}
						}
					}
				}
			}
		}
		if (afterWhere.length() > 4) {
			// 定义字符串s 用以截取拼接字段条件前后加上括号
			String s = afterWhere.substring(4);
			afterWhere = " and (" + s + ")";
		}
		// 一对一关系
		sqlMap.put("sql", sql + as + from + leftjoin + where + afterWhere);
		// 查询总条数
		sqlMap.put("sqlCount", "select  count(*) from (" + sql + from + leftjoin + where + afterWhere + "group by "
				+ fieldFilter.get(0).getAsJsonObject().get("tableAlias").getAsString() + ".id ) as result");
		// 报表查询条数
		sqlMap.put("sqlMain", sql + from + leftjoin + where + afterWhere + "group by "
				+ fieldFilter.get(0).getAsJsonObject().get("tableAlias").getAsString() + ".id");
		sqlMap.put("sqlCountReport",
				"select  count(*) from (" + sql + from + leftjoin + where + afterWhere + " ) as result");
		// 报表查询
		sqlMap.put("sqlMainReport", sql + from + leftjoin + where + afterWhere);
		return sqlMap;
	}

	/**
	 * 根据表别名和字段别名返回字段中文名
	 *
	 * @param fileName   配置文件名
	 * @param tableAlias 表别名
	 * @param field      字段别名
	 * @return
	 */
	public static String rtlName(String fileName, String tableAlias, String field) throws FileNotFoundException {
		String name = "";
		try {
			InputStream stream = DataUtils.class.getClassLoader()
					.getResourceAsStream("static/query-configuration/" + fileName + ".json");
			InputStreamReader inputStreamReader = new InputStreamReader(stream, "UTF-8");
			// 创建JSON解析
			JsonParser parser = new JsonParser();
			// 获取跟目录
			JsonArray array = (JsonArray) parser.parse(inputStreamReader);

			for (int i = 0; i < array.size(); i++) {
				JsonObject subObject = array.get(i).getAsJsonObject();
				if (tableAlias.equals(subObject.get("tableAlias").getAsString())) { // 判断表别名
					JsonArray fieldList = (JsonArray) subObject.get("fieldList");
					for (int j = 0; j < fieldList.size(); j++) { // 判断表字段
						JsonObject item = fieldList.get(j).getAsJsonObject();
						if (field.equals(item.get("code").getAsString())) {
							name = item.get("name").getAsString();
							break;// 匹配成功，结束循环
						}
					}
				}

			}

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 返回excel格式数据
	 *
	 * @param filterFieldList 过滤字段数组
	 * @param fileName        配置文件名
	 * @param entityList      数据源
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Map<String, Object> rtlExcelData(List<String> filterFieldList, String fileName,
			List<Map<String, Object>> entityList) throws FileNotFoundException {

		Map<String, Object> param = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		List<List<Object>> rows = new ArrayList<List<Object>>();

		if (filterFieldList.size() == 0) {
			return param;
		}

		for (String item : filterFieldList) { // 获取表头数据
			String[] arr = item.split("\\.");
			if (arr.length != 2) { // 正确格式：表别名.字段名
				continue;
			}
			String tableAlias = arr[0];
			String field = arr[1];
			// 获取表头中文名
			String name = rtlName(fileName, tableAlias, field);
			if (!name.isEmpty()) {
				titles.add(name);
			} else {
				titles.add("未配置字段");
			}
		}

		for (Map<String, Object> map : entityList) {// 获取表数据
			List<Object> row = new ArrayList<Object>();
			for (String str : filterFieldList) {
				row.add(map.get(str));
			}
			rows.add(row);
		}
		param.put("titles", titles);
		param.put("rows", rows);

		return param;

	}

	/**
	 * 处理Excel的数据源
	 */
	public static Map<String, Object> dataChange(String tName, Object item, HashMap<String, String> dictMap) {
		Map<String, Object> arr = new HashMap<String, Object>();
		try {
			Field[] fields = item.getClass().getDeclaredFields();
			for (int j = 0; j < fields.length; j++) { // 遍历所有属性
				Field f = fields[j];
				f.setAccessible(true);
				// 时间格式化
				if (f.getType().getName() == "java.util.Date" && f.get(item) != null) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date = formatter.format((Date) f.get(item));
					arr.put(tName + "." + f.getName(), date);
				} else {
					// 获得类名
					String className = item.getClass().getSimpleName();
					if (f.get(item) != null) {
						// 过滤字典项
						DictUtils dictUtils = new DictUtils();

						String value = dictUtils.dictFilter(className, f.getName(), f.get(item).toString(), dictMap);
//						// 出入库流水
//						if (className.equals("StocksHistoryEntity")) {
//							if (f.getName().equals("type")) {
//								Field field = item.getClass().getDeclaredField("ifOutIn");
//								// 设置对象的访问权限，保证对private的属性的访问
//								field.setAccessible(true);
//								boolean outOrIn = (boolean) field.get(item);
//								// 出入库类型判断
//								if (outOrIn) {
//									value = dictUtils.getDictData("StockInType", value, dictMap);
//								} else {
//									value = dictUtils.getDictData("StockOutType", value, dictMap);
//								}
//							}
//						}
						arr.put(tName + "." + f.getName(), value);
					} else {
						arr.put(tName + "." + f.getName(), f.get(item));
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//		catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}
		return arr;
	}

	/**
	 * 拼接审批流程sql语句
	 */
	public static String activitiCandidateSql(String perms) {
		String sql = "SELECT staff.name " + "FROM sys_user_role ur "
				+ "LEFT JOIN sys_role_menu rm ON rm.role_id = ur.role_id "
				+ "LEFT JOIN sys_role_dept rd ON rd.role_id =ur.role_id " + "LEFT JOIN sys_menu m ON m.id = rm.menu_id "
				+ "LEFT JOIN sys_user u on u.id = ur.user_id "
				+ "LEFT JOIN basic_staff staff on staff.user_id = u.id "
				+ "LEFT JOIN sys_dept dept ON dept.id = rd.dept_id " + " WHERE m.perms = '" + perms
				+ "' and dept.dept_type = 'headquarters'";
		return sql;
	}

	/**
	 * 拼接审批流程sql语句
	 */
	public static String activitiCandidateIDSql(String perms) {
		String sql = "SELECT staff.id " + "FROM sys_user_role ur "
				+ "LEFT JOIN sys_role_menu rm ON rm.role_id = ur.role_id "
				+ "LEFT JOIN sys_role_dept rd ON rd.role_id =ur.role_id " + "LEFT JOIN sys_menu m ON m.id = rm.menu_id "
				+ "LEFT JOIN sys_user u on u.id = ur.user_id "
				+ "LEFT JOIN basic_staff staff on staff.user_id = u.id "
				+ "LEFT JOIN sys_dept dept ON dept.id = rd.dept_id " + " WHERE m.perms = '" + perms
				+ "' and dept.dept_type = 'headquarters'";
		return sql;
	}
}
