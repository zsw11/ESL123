package io.apj.common.utils;

/**
 * 常量
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
	/** 数据权限过滤 */
	public static final String SQL_FILTER = "sql_filter";

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
	 * 入库单型
	 */
	public final static String STOCK_IN_TYPE = "STOCK_IN_TYPE";

	/**
	 * 出库单型
	 */
	public final static String STOCK_OUT_TYPE = "STOCK_OUT_TYPE";

	/**
	 * 批量导入数量控制
	 */
	public final static int importNum = 1000;

	/**
	 * 系统访问存储图片路径
	 */
	public static String READ_PATH = "/home/ubuntu/resources-img/";

	/**
	 * 系统文件存放路径
	 */
	public static String FILE_PATH = "/home/ubuntu/file/";

	/**
	 * 系统excel模板文件存放路径
	 */
	public static String EXCEL_PATH = "/home/ubuntu/excel/";
	
	/**
	 * 系统html模板文件存放路径
	 */
	public static String HTML_PATH = "/home/ubuntu/html/";

}
