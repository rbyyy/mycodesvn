package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 权限角色
 * @author eternal
 *
 */
public class Auth implements ApplicationEntity, Serializable {

	private static final long serialVersionUID = 1L;
	
	/**主键*/
	private int id;
	/**用户名*/
	private String username;
	/**密码*/
	private String password;
	/**手机号*/
	private String mobile;
	/**电子信箱*/
	private String email;
	/**角色类型   1管理员  2商店 3 小蜜蜂  4用户*/
	private int roleType;
	/**角色ID*/
	private String roleId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public int getRoleType() {
		return roleType;
	}
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	
}
