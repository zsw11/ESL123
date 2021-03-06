package io.apj.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 常量
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月15日 下午1:23:52
 */
@Configuration
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
	/** 数据权限过滤 */
	public static final String SQL_FILTER = "sql_filter";

	/** 报表审批状态 */
	public static final String APPROVE_REPORT_STATUS_SUBMIT = "01"; // 待审批
	public static final String APPROVE_REPORT_STATUS_AGREE = "02"; // 已审批
	public static final String APPROVE_REPORT_STATUS_CREATE = "03"; // 未审批
	public static final String APPROVE_REPORT_STATUS_REJECT = "04"; // 未通过
	
	/** 工位类型组别  */
	public static final String WORKSTATION_TYPE_SUB = "SUB";
	public static final String WORKSTATION_TYPE_MAIN = "MAIN";
	public static final String WORKSTATION_TYPE_PACKING = "PACKING";
	public static final String WORKSTATION_TYPE_PRINT = "PRINT";
	public static final String WORKSTATION_TYPE_EXTERNAL = "EXTERNAL";
	
	/** LST/ST类型 */
	public static final String LST = "01";
	public static final String ST = "02";
	
	/** 人机两个表输入数值 */
	public static final Integer MAN_MACHINE_COMBINATION_ENTER = 2;
	
	/**
	 * 菜单类单型
	 * 
	 * @author chenshun
	 * @email sunlightcs@gmail.com
	 * @date 2016年11月15日 下午1:24:29
	 */
	public enum MenuType {
		/**
		 * 目录
		 */
		CATALOG(0),
		/**
		 * 菜单
		 */
		MENU(1),
		/**
		 * 按钮
		 */
		BUTTON(2);

		private int value;

		MenuType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * 定时任务状态
	 * 
	 * @author chenshun
	 * @email sunlightcs@gmail.com
	 * @date 2016年12月3日 上午12:07:22
	 */
	public enum ScheduleStatus {
		/**
		 * 正常
		 */
		NORMAL(0),
		/**
		 * 暂停
		 */
		PAUSE(1);

		private int value;

		ScheduleStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * 云服务商
	 */
	public enum CloudService {
		/**
		 * 七牛云
		 */
		QINIU(1),
		/**
		 * 阿里云
		 */
		ALIYUN(2),
		/**
		 * 腾讯云
		 */
		QCLOUD(3);

		private int value;

		CloudService(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * 批量导入数量控制
	 */
	public final static int importNum = 1000;
	/**
	 * 系统访问存储图片路径
	 */
	public static String READ_PATH = "/home/ubuntu/resources-img/";
	/**
	 * Excel 模板存储路径
	 */
	public static String TEMPLATE_PATH;

	@Value("${templatePath}")
	public void setTemplatePath(String path) {
		Constant.TEMPLATE_PATH = path;
	}

}
