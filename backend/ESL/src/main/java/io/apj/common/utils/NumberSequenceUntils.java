package io.apj.common.utils;

import java.util.Date;

/**
 * 编码规则通用类
 * 
 * @author samchen
 * @date 2018-12-17
 *
 */
public class NumberSequenceUntils {

	/** 维修工单号 前缀 **/
	public static final String REPAIR_WORK_ORDER_PREFIX = "GZWX";

	/** 服务工单号 前缀 **/
	public static final String SERVICE_WORK_ORDER_PREFIX = "CS";

	/**
	 * 获取维修工单编码
	 * 
	 * @param serialNumber     流水号(4位)
	 * @param organizationCode 组织机构编码
	 * @return string
	 */
	public static String getRepairOrderNumber(int serialNumber, String organizationCode) {
		String num = serialNumber + "";
		int length = num.length();
		switch (length) {
		case 1:
			num = "000" + num;
			break;
		case 2:
			num = "00" + num;
			break;
		case 3:
			num = "0" + num;
			break;
		default:
			break;

		}

		String str = REPAIR_WORK_ORDER_PREFIX + organizationCode + DateUtils.format(new Date(), "yyyyMMdd") + num;

		return str;
	}

	/**
	 * 获取服务工单编码
	 * 
	 * @param serialNumber     流水号(4位)
	 * @param organizationCode 组织机构编码
	 * @return string
	 */
	public static String getServiceOrderNumber(int serialNumber, String organizationCode) {
		String num = serialNumber + "";
		int length = num.length();
		switch (length) {
		case 1:
			num = "000" + num;
			break;
		case 2:
			num = "00" + num;
			break;
		case 3:
			num = "0" + num;
			break;
		default:
			break;

		}
		String str = SERVICE_WORK_ORDER_PREFIX + organizationCode + DateUtils.format(new Date(), "yyyyMMdd") + num;

		return str;
	}



}
