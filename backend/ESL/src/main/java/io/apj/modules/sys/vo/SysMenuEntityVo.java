package io.apj.modules.sys.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.PinyinUtil;
import io.apj.modules.sys.entity.SysMenuEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

public class SysMenuEntityVo implements BaseVo<SysMenuEntityVo, SysMenuEntity>, Comparable<SysMenuEntityVo>{

	// entity 转   vo
	public static SysMenuEntityVo makeVo(SysMenuEntity menu) {
		SysMenuEntityVo vo = new SysMenuEntityVo();
		if(menu != null) {
//			BeanUtils.copyProperties(vo,menu);
			vo.id = menu.getId();
			vo.parentId = menu.getParentId();
			vo.name = menu.getName();
			vo.pinyin = PinyinUtil.getPinYin(menu.getName());
			vo.url = menu.getUrl();
			vo.perms = menu.getPerms() == null || "".equals(menu.getPerms()) ? new String[0] : menu.getPerms().split(",");
			vo.typeId = getTypeId(menu.getType());
			vo.icon = menu.getIcon();
			vo.orderNumber = menu.getOrderNum();
			vo.isNav = menu.getPerms() == null || "".equals(menu.getPerms()) ? true : false;
			vo.createdAt = menu.getCreateAt();
			vo.updateAt = menu.getUpdateAt();
			vo.deleteAt = menu.getDeleteAt();
		}
		return vo;
	};

	// vo 转  entity
	public static SysMenuEntity makeEntity(SysMenuEntityVo vo) {
		SysMenuEntity ent = new SysMenuEntity();
		ent.setId(vo.getId());
		ent.setParentId(vo.getParentId());
		ent.setName(vo.getName());
		ent.setUrl(vo.getUrl());
		ent.setIcon(vo.getIcon());
		ent.setPerms(mekePerms(vo.getPerms()));
		ent.setType(getType(vo.getTypeId()));
		ent.setOrderNum(vo.getOrderNumber());
		return ent;
	};

	// elist 转  vlist
	@Override
	public List<SysMenuEntityVo> makeVoList(List<?> elist) {
		List<SysMenuEntityVo> vlist = new ArrayList<>();
		if(elist != null) {
			for(Object e : elist) {
				vlist.add(SysMenuEntityVo.makeVo((SysMenuEntity) e));
			}
		}
		return vlist;
	}

	public static List<SysMenuEntityVo> staticMakeVoList(List<?> elist) {
		List<SysMenuEntityVo> vlist = new ArrayList<>();
		if(elist != null) {
			for(Object e : elist) {
				vlist.add(SysMenuEntityVo.makeVo((SysMenuEntity) e));
			}
		}
		return vlist;
	}

	// 字符串数组转字符串
	private static String mekePerms(String[] arr) {
		String str = StringUtils.join(arr, ",");
		return str;
	}

	// typeId 转为  名称
	private static String getTypeId(Integer t) {
		String type = "dir";
		switch (t) {
		case 0:
			break;
		case 1:
			type = "menu";
			break;
		case 2:
			type = "button";
			break;
		}
		return type;
	}

	// 名称    转为   typeId
	private static int getType(String t) {
		int type = 0;
		switch (t) {
		case "dir":
			break;
		case "menu":
			type = 1;
			break;
		case "button":
			type = 2;
			break;
		}
		return type;
	}

	/**
	 * id
	 */
	private Long id;

	/**
	 * parentId
	 */
	private Long parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 拼音
	 */
	private String pinyin;

	/**
	 * 链接
	 */
	private String url;

	/**
	 * 参数
	 */
	private String[] perms;

	/**
	 * typeId
	 */
	private String typeId;

	/**
	 * isNav
	 */
	private Boolean isNav;

	/**
	 * icon
	 */
	private String icon;

	/**
	 * orderNumber
	 */
	private Integer orderNumber;

	/**
	 * 创建时间
	 */
	private Date createdAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	/**
	 * 删除时间
	 */
	private Date deleteAt;
	/**
	 * 子菜单
	 */
	private List<SysMenuEntityVo> subMenus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getPerms() {
		return perms;
	}

	public void setPerms(String[] perms) {
		this.perms = perms;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public Boolean getIsNav() {
		return isNav;
	}

	public void setIsNav(Boolean isNav) {
		this.isNav = isNav;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<SysMenuEntityVo> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<SysMenuEntityVo> subMenus) {
		this.subMenus = subMenus;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updateAt;
	}

	public void setUpdatedAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	@Override
	public int compareTo(SysMenuEntityVo arg0) {
		if(this.orderNumber >= arg0.getOrderNumber()){
			return 1;
        }
      return -1;
	}
}
