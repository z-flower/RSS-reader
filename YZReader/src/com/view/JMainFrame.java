package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.service.*;
public class JMainFrame extends JFrame implements ActionListener {

	private final static int WIDTH=800;
	private final static int HEIGHT=600;
	private final static String TITLE="RSS阅读器";
	private RSSService rssService;
	private JButton jb1,jbExport;//读取按钮，导出按钮
	private JTextArea jta1;//新闻内容文本
	private JPanel jp1,JPmain;
	private JLabel jl1;
	private DefaultTableModel dttableModel;
	private JTable JTTable;
	private List<News> newsList;
	private JMenuBar jmb1;
	private JMenu jm1,jm3,jm4;
	private JMenuItem jmi1,jmi2,jmi3,jmi4;
    private JToolBar jtb1;
    
	private void setCenter(){
	Toolkit kit=Toolkit.getDefaultToolkit();
	Dimension screenSize=kit.getScreenSize();
	this.setLocation((screenSize.width-WIDTH)/2,(screenSize.height-HEIGHT)/2);
	}
//构造函数	
	public JMainFrame(){
		rssService=new RSSService();
		this.setIconImage(new ImageIcon("images/t01c8711e806ac8e9ef.jpg").getImage());
		this.setJMenuBar(getJMenubar());
		this.setContentPane(getJpmain());
		this.setCenter();
		this.setTitle(TITLE);
		this.setVisible(true);
		this.setSize(WIDTH, HEIGHT);
		//this.setCenter();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

//菜单栏
	public  JMenuBar getJMenubar(){
		jmb1=new JMenuBar();
		
		jm1=new JMenu("文件(f)");
		jm1.setMnemonic('f');
		
		jm4=new JMenu("新闻列表");
		List<Channel> channelList=rssService.getChannelList();
		for(int i=0;i<channelList.size();i++){
		    Channel c1=	channelList.get(i);
			jmi1=new JMenuItem(c1.toString());
			jmi1.addActionListener(new ActionListener(){
				@Override
			public void actionPerformed(ActionEvent e) {
			Channel selectedChannel=c1;//得到鼠标点击的那项
			System.out.println(selectedChannel);
			String filePath=selectedChannel.getFilePath();//该项的路经
			System.out.println(filePath);
		    newsList=rssService.getNewsList(filePath);
		    showTable(newsList);	
				}
				});
			jm4.add(jmi1);
			}
	    jm1.add(jm4);
		jm1.addSeparator();
		jmi3=new JMenuItem("退出");
		jmi3.addActionListener(this);
		jm1.add(jmi3);
		
		jm3=new JMenu("帮助(h)");
		jmi2=new JMenuItem("帮助文档");
		jmi4=new JMenuItem("问题反馈");
		jm3.add(jmi2);
		jm3.add(jmi4);
		jm3.setMnemonic('h');
		
		jmb1.add(jm1);
		jmb1.add(jm3);
		return jmb1;
	}

//退出的监听
	public void actionPerformed(ActionEvent e){
		    if(e.getSource()==jmi3){
		        System.exit(0);}
		    }
//显示新闻信息
  private void showTable(List<News> newsList){
    	int rowCount=dttableModel.getRowCount();
    	while(rowCount>0){
    		dttableModel.removeRow(0);
    		rowCount--;
    		}
    		for(int i=0;i<newsList.size();i++){
    			News news=newsList.get(i);
    			
    			SimpleDateFormat  dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			Date date=new Date();
    			String currentDate =dateFormat.format(date);
    			
    			String[] date1={news.getNewsTitle(),currentDate,news.getNewsDate(),news.getNewsAuthor()};
    			dttableModel.addRow(date1);
    		}
    }
//工具栏
	private JToolBar getJTBar(){
		jtb1=new JToolBar();
		
		jb1 = new JButton();
		jb1.add(getJBExport());
	
		jtb1.add(jb1);
		return jtb1;
		
	}
//分割面板	
	private JSplitPane getJSPClientArea(){
		JSplitPane jsp=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jsp.setDividerLocation(280);
		jsp.setLeftComponent(getJSPTable());
		jsp.setRightComponent(getJSPContent());
		return jsp;
		
	}
//状态栏
	private JPanel getJSBMy(){
		jp1=new JPanel();
		jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
		jl1=new JLabel("www.aaaaaa.com");
		jp1.add(jl1);
		return jp1;
		
	}
	
//主面板
	public JPanel getJpmain(){
		JPmain=new JPanel();
		JPmain.setLayout(new BorderLayout());
		
		JPmain.add(getJTBar(),BorderLayout.NORTH);
		JPmain.add(getJSPClientArea(),BorderLayout.CENTER);
		JPmain.add(getJSBMy(),BorderLayout.SOUTH);
		return JPmain;
	}
	
//导出按钮
	private JButton getJBExport(){
		if(jbExport==null){
			jbExport=new JButton(new ImageIcon("images/export-to-document.png"));
			 jbExport.setToolTipText("文件导出");
			jbExport.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(rssService.save()){
						JOptionPane.showMessageDialog(null, "新闻信息·保存成功");
					}else{
						JOptionPane.showMessageDialog(null, "新闻信息·保存失败");
					}
				}
			});
		}
		return jbExport;
	}

//滚动表格
    private JScrollPane getJSPTable(){
    	JScrollPane jspTable=null;
    	if(JTTable==null){
    		//创建表格数据类型
    		dttableModel=new DefaultTableModel();
    		dttableModel.addColumn("主题");
    		dttableModel.addColumn("接受时间");
    		dttableModel.addColumn("发布时间");
    		dttableModel.addColumn("作者");
    		//根据表格数据类型创建表格
    		JTTable =new JTable(dttableModel);
    		//采用匿名内部类的形式为表格添加鼠标单击事件监听器
    		JTTable.addMouseListener(new MouseAdapter(){
    			//处理鼠标事件是否单击
    			public void mouseClicked(MouseEvent e){
    				if(e.getClickCount()==1){
    					int seletedRow=JTTable.getSelectedRow();//获取鼠标单击位置
    					News selectedNews=newsList.get(seletedRow);//获取选中新闻信息
    					jta1.setText(rssService.NewstoString(selectedNews));
    				}
    			}
    		});
    		jspTable=new JScrollPane(JTTable);
    		
    	}
		return jspTable;
   }
//滚动文本
    private JScrollPane getJSPContent(){
		 JScrollPane jsp=null;
		 if(jta1==null){
		 jta1=new JTextArea();
		 jta1.setLineWrap(true);
		 jsp=new JScrollPane(jta1);
		 jsp.setPreferredSize(new Dimension(700,260));
		 }
		 return jsp;
		 }

	}
    
    

