package com.dao.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.dao.NewsDao;
import com.model.News;
import com.service.RSSService;



public class FileDaoImpl implements NewsDao{
	private static String FILE_PATH="NewsFiles/rss.txt";

	@Override
	public boolean save(List<News> newsList) {
		boolean flag=true;
		File file=new File(FILE_PATH);
		FileWriter fw=null;
		BufferedWriter bw=null;
		try{
			fw=new FileWriter(file,true);
			bw=new BufferedWriter(fw);
			RSSService rssService=new RSSService();//遍历新闻列表，将新闻内容保存到文件
			for(News news:newsList){
				String content=rssService.NewstoString(news);
				bw.write(content);
			}
			
			bw.flush();
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			//关闭顺序与打开相反
			try {
				bw.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		return flag;
	}

}

		