package io.apj.modules.sys.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.PinyinUtil;
import io.apj.modules.sys.entity.SysRoleEntity;

public class SysRoleEntityVo implements BaseVo<SysRoleEntityVo, SysRoleEntity>{

	// elist 转  vlist
	@Override
	public List<SysRoleEntityVo> makeVoList(List<?> elist) {
		List<SysRoleEntityVo> vlist = new ArrayList<>();
		if(elist != null) {
			for(Object e : elist) {
				vlist.add(SysRoleEntityVo.makeVo((SysRoleEntity) e));
			}
		}
		return vlist;
	}

	// entity 转   vo
	public static SysRoleEntityVo makeVo(SysRoleEntity ent) {
		SysRoleEntityVo vo = new SysRoleEntityVo();
		vo.id = ent.getId();
		vo.remark = ent.getRemark();
		vo.pinyin = PinyinUtil.getPinYin(ent.getRoleName());
		vo.name = ent.getRoleName();
		vo.createAt = ent.getCreateAt();
		vo.updateAt = ent.getUpdateAt();
		vo.deleteAt = ent.getDeleteAt();
		return vo;
	}

	// vo 转  entity
	public static SysRoleEntity makeEntity(SysRoleEntityVo vo) {
		SysRoleEntity ent = new SysRoleEntity();
		ent.setId(vo.getId());
		ent.setRoleName(vo.getName());
		ent.setRemark(vo.getRemark());
		ent.setMenuIdList(vo.getMenuIds());
		ent.setCreateAt(vo.getCreateAt());
		return ent;
	}

	/**
	 * id
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 拼音
	 */
	private String pinyin;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 *对应的菜单
	 */
	private List<SysMenuEntityVo> menus;

	/**
	 *对应的菜单 id
	 */
	private List<Long> menuIds;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	/**
	 * 删除时间
	 */
	private Date deleteAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public SysRoleEntityVo setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
		return this;
	}

	public List<SysMenuEntityVo> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenuEntityVo> menus) {
		this.menus = menus;
	}

	// 设置菜单并返回本身
	public SysRoleEntityVo setMenusThis(List<SysMenuEntityVo> menus) {
		this.menus = menus;
		return this;
	}
}
