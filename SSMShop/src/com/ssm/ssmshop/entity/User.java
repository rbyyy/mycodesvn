package com.ssm.ssmshop.entity;

import java.io.Serializable;

/**
 * 用户
 * @author eternal
 *
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**主键*/
	private String id;
	/**账户*/
	private double account;
	/**积分*/
	private int score;
	/**收藏*/
	private String favorite;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	
}
