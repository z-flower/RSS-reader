package com.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

import com.model.Channel;
import com.model.News;
import com.view.JMainFrame;

public class UpdateThread extends Thread {
	private final static int TIMEOUT=5*1000;//5s
	private final static int DELAY_TIME=300*1000;//5min
	private List<Channel> channelList;
	
	Channel c1=new Channel();
	
	public UpdateThread(){
		RSSService rssService=new RSSService();
		channelList=rssService.getChannelList();
	}
	//线程运行方法
	public void run(){
		while(true){
			System.out.println("正在更新"+new Date());
			for(int i=0;i<channelList.size();i++){
				Channel channel=channelList.get(i);
				System.out.println("更新："+channel.getName());
				update(channel.getUrl(),channel.getFilePath());
			}
			System.out.println("更新完毕"+new Date());
			try{
				sleep(DELAY_TIME);
			}catch(InterruptedException e){
				e.printStackTrace();
				
			}
		}
		
	}
	
	//从网络下载urlPath文件，保存在filePath路径下
	
	public void update(String urlPath,String filePath){
		HttpURLConnection httpConn;
		//建立连接
			try{
				URL url=new URL(urlPath);
			    httpConn=(HttpURLConnection)url.openConnection();
			    httpConn.setConnectTimeout(TIMEOUT);
				int  respondCode=httpConn.getResponseCode();
				if(respondCode!=HttpURLConnection.HTTP_OK){
					return;
				}
				
			}catch(MalformedURLException e){
				e.printStackTrace();
				return;
				
			}catch(IOException e){
				e.printStackTrace();
				return;
			}
			
			File file=new File(filePath);
			if(hasNewRSS(httpConn,file)){//判断更新
				System.out.println("现在更新");
				ByteBuffer buffer = null;
				try {
					buffer = download(httpConn);//下载到缓冲区
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(buffer!=null){
					try {
						saveAs(buffer,file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//缓冲区数据保存到本地
				}
			}else{
				System.out.println("更新完毕");
			}
	}
	//下载文件到缓冲区
	private ByteBuffer download(HttpURLConnection httpConn) throws IOException{
		ByteBuffer buffer=ByteBuffer.allocate(65536);
		InputStream in=httpConn.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(in,"utf-8"));
		String line="";
		while((line=br.readLine())!=null){
			buffer.put(line.getBytes());
		}
		
        buffer.flip();
		br.close();
		in.close();
		httpConn.disconnect();
		
		return buffer;
		
	}
	//缓冲区文件保存到文件中
	private synchronized void saveAs(ByteBuffer buffer,File file) throws IOException{
		FileOutputStream f=new FileOutputStream(file);
		FileChannel channel=f.getChannel();
		channel.write(buffer);
		channel.close();
		
	}
	//判断文件是否有更新
	private boolean hasNewRSS(HttpURLConnection httpConn,File file){
		long current=System.currentTimeMillis();
		long http=httpConn.getHeaderFieldDate("Last-Modified", current);
		long fileLast=file.lastModified();
		if(http>fileLast){
			return true;
		}
		return false;
		
	}
}
