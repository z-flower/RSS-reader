package com.service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import com.dao.NewsDao;
import com.dao.impl.FileDaoImpl;
import com.model.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class RSSService{

	

	private List<Channel> channelList;
	private List<News> newsList;
	private NewsDao rssdao;
	public RSSService(){
		rssdao=new FileDaoImpl();//创建filedaoimpl类型的·数据访问层对象
	}
	//获取列表信息
    public List<Channel> getChannelList(){
		if(channelList==null){
			channelList=new ArrayList<Channel>();
			
			Channel c1=new Channel();
			c1.setName("国内财经·");
			c1.setFilePath("NewsFiles/rss_domestic.xml");
			c1.setUrl("http://finance.qq.com/financenews/domestic/rss_domestic.xml");
			
			Channel c2=new Channel();
			c2.setName("国际新闻");
			c2.setFilePath("NewsFiles/rss_newswj.xml");
			c2.setUrl("http://news.qq.com/newsgj/rss_newswj.xml");
			
			Channel c3=new Channel();
			c3.setName("社会新闻");
			c3.setFilePath("NewsFiles/rss_newssh.xml");
			c3.setUrl("http://news.qq.com/newssh/rss_newssh.xml");
			
			Channel c4=new Channel();
			c4.setName("最新电影");
			c4.setFilePath("NewsFiles/rss_movie.xml");
			c4.setUrl("http://ent.qq.com/movie/rss_movie.xml");
			
			Channel c5=new Channel();
			c5.setName("最新电视");
			c5.setFilePath("NewsFiles/rss_tv.xml");
			c5.setUrl("http://ent.qq.com/tv/rss_tv.xml");
			
			channelList.add(c1);
			channelList.add(c2);
			channelList.add(c3);
			channelList.add(c4);
			channelList.add(c5);
			
		}
		return channelList;
	}
  //获取新闻列表信息
    public List<News> getNewsList(String filePath){
    	newsList=new ArrayList<News>();
		Document doc=load(filePath);
		newsList=parse(doc);
		return newsList;
	}
  //读取XML文件
    public Document load(String filePath){
    	  //1.指定解析器
    	Document doc1=null;
		SAXBuilder sb=new SAXBuilder(false);
	  	//2.创建文件对象
    	File fXML=new File(filePath);
    	//3.调用build（）
    		try{
    			doc1=(Document) sb.build(fXML);
    		}catch(JDOMException e){
    			
    			e.printStackTrace();
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    	return doc1;
    }
  //解析xml文件
	private List<News> parse(Document doc){
    	 newsList=new ArrayList<News>();
    	News news=null;
    	
    	Element root=doc.getRootElement();//得到XML文档的根节
    	Element eChannel=root.getChild("channel");
    	List<Element> itemList=eChannel.getChildren("item");
    	for(int i=0;i< itemList.size();i++){
    		Element item=itemList.get(i);
    		news=itemToNews(item);
    		newsList.add(news);
    	}
    	return newsList;	
    }
	//将item对象转化为News
    private  News itemToNews(Element item){
		News news=new News();
		//获取节点内容
		
		 String newsTitle=item.getChildText("title").trim();
		 String newsLink=item.getChildText("link");
		 String newsDate=item.getChildText("pubDate");
		 String newsAuthor=item.getChildText("author");
		 String newsDescription=item.getChildText("description");
	     String newsGuid=item.getChildText("guid");
	     String newsCategory=item.getChildText("category").trim();
		news.setNewsTitle(newsTitle);
		news.setNewsLink(newsLink);
		news.setNewsDate(newsDate);
		news.setNewsAuthor(newsAuthor);
		news.setNewsDescription(newsDescription);
		news.setNewsGuid(newsGuid);
		news.setNewsCategory(newsCategory);
		return news;  
		
	} 
 	
	public String NewstoString(News news){
		String content="";
	    content=

				"标题："+news.getNewsTitle()+"\r\n"+
				"链接："+news.getNewsLink()+"\r\n"+
				"作者："+news.getNewsAuthor()+"\r\n"+
				"发布时间："+news.getNewsDate()+"\r\n"+
				"...............\r\n："+news.getNewsDescription()+"\r\n";
		return content;
	}
	//调用数据访问层的方法，保存新闻内容
	public boolean save(){
		boolean flag=false;
		if(rssdao.save(newsList)){
			flag=true;//success
		}
		return flag;
		
	}
	
}