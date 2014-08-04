package com.gov.nzjcy.entity;


public class NewsCommentEntity implements ApplicationEntity {
	/** 新闻评论id */
	private String commentArticleId;
	/** 用户id */
	private String commentUserId;
	/** 用户名称*/
	private String commentUserName;
	/** 评论信息 */
	private String commentDescription;
	/**日期时间*/
	private  String	commentDateAndTime;
	/**状态信息*/
	private  String	 commentState;
	

	public String getCommentArticleId() {
		return commentArticleId;
	}
	public void setCommentArticleId(String commentArticleId) {
		this.commentArticleId = commentArticleId;
	}
	
	public String getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}
	
	public String getCommentUserName() {
		return commentUserName;
	}
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	
	public String getCommentDescription() {
		return commentDescription;
	}
	public void setCommentDescription(String string) {
		this.commentDescription = string;
	}
	
	public String getCommentDateAndTime() {
		return commentDateAndTime;
	}
	public void setCommentDateAndTime(String commentDateAndTime) {
		this.commentDateAndTime = commentDateAndTime;
	}
	
	public String getCommentState() {
		return commentState;
	}
	public void setCommentState(String commentState) {
		this.commentState = commentState;
	}
	
	
	
}
