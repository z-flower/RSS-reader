package com.model;
//新闻内容:新闻标题，内容来源，发布时间
//私有成员变量，无参构造方法，get（），set（）String类
public class News {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	private String newsTitle;
	private String newsLink;
	private String newsDate;
	private String newsAuthor;
	private String newsDescription;
	private String newsGuid;
	private String newsCategory;
	public News(){
		this.newsTitle="";
		this.newsLink="";
		this.newsDate="";
		this.newsAuthor="";
		this.newsDescription="";
		this.newsGuid="";
		this.newsCategory="";
	}
	
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}
	public String getNewsAuthor() {
		return newsAuthor;
	}
	public void setNewsAuthor(String newsAuthor) {
		this.newsAuthor = newsAuthor;
	}
	public String getNewsDescription() {
		return newsDescription;
	}
	public void setNewsDescription(String newsDescription) {
		this.newsDescription = newsDescription;
	}
	public String getNewsLink() {
		return newsLink;
	}
	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}
	public String getNewsGuid() {
		return newsGuid;
	}
	public void setNewsGuid(String newsGuid) {
		this.newsGuid = newsGuid;
	}
	public String getNewsCategory() {
		return newsCategory;
	}
	public void setNewsCategory(String newsCategory) {
		this.newsCategory = newsCategory;
	}
}
