package com.ssm.ssmshop.entity;

import java.io.Serializable;

public class UserInfo implements ApplicationEntity,Serializable {
	private static final long serialVersionUID = 1L;

	/**账户余额*/
	private double 	account;
	/**积分*/
	private int 	score;
	/**收藏数量*/
	private int 	favoriteCount ;
	/**待签收订单数量*/
	private int 	readyOrderCount;
	/**已付款订单数量*/
	private int 	paidOrderCount;
	/**未付款订单数量*/
	private int 	noPaidOrderCount;
	
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
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public int getReadyOrderCount() {
		return readyOrderCount;
	}
	public void setReadyOrderCount(int readyOrderCount) {
		this.readyOrderCount = readyOrderCount;
	}
	public int getPaidOrderCount() {
		return paidOrderCount;
	}
	public void setPaidOrderCount(int paidOrderCount) {
		this.paidOrderCount = paidOrderCount;
	}
	public int getNoPaidOrderCount() {
		return noPaidOrderCount;
	}
	public void setNoPaidOrderCount(int noPaidOrderCount) {
		this.noPaidOrderCount = noPaidOrderCount;
	}
	
}
