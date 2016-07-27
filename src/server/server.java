package server;

//服务器端代码
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import data.DataPack;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;

public class server extends JFrame
{
	//入口函数
	public static void main(String args[])
	{
		//设置字体方法
		SetFont.setFont(new Font("宋体", 0, 12));
		new server();
	}
	private JButton []btn=new JButton[3];
	private String  []btnStr={"删除用户","发出警告","广播系统信息"};
	private JTextField ServerMessageTxt=new JTextField();
	private JLabel messagelab=new JLabel("系统信息:");
	private JList listuser=null;
	private DefaultListModel dluser=new DefaultListModel();
	private JList listop=null;
	private DefaultListModel dlop=new DefaultListModel();

	//数据
	private Hashtable all=new Hashtable();
	private Vector names=new Vector();
	ServerSocket ss=null;
	Socket client=null;

	//接收字符串的定义
	public String name=null;
	public String delname=null;

	//构造器
	public server()
	{
		//窗体设置
		setSize(700,500);
		setTitle("服务器");
		Container con=getContentPane();

		//布局
		JPanel Buttompane=new JPanel();
		Buttompane.setPreferredSize(new Dimension(400,70));
		JPanel pane1=new JPanel();
		JPanel pane2=new JPanel();
		for(int i=0;i<btn.length;i++)
		{
			btn[i]=new JButton(btnStr[i]);
			btn[i].addActionListener(new btnAct());
		}
		ServerMessageTxt.addActionListener(new btnAct());
		ServerMessageTxt.setPreferredSize(new Dimension(300,25));
		pane2.add(messagelab);
		pane2.add(ServerMessageTxt);
		pane2.add(btn[2]);

		pane1.setPreferredSize(new Dimension(200,200));
		pane1.add(btn[0]);pane1.add(btn[1]);

		listuser=new JList(dluser);
		listop=new JList(dlop);
		JScrollPane jp=new JScrollPane(listuser);
		jp.setPreferredSize(new Dimension(200,200));

		Buttompane.setLayout(new BorderLayout());
		Buttompane.add(pane2,BorderLayout.CENTER);
		Buttompane.add(pane1,BorderLayout.EAST);

		con.add(jp,BorderLayout.EAST);
		con.add(new JScrollPane(listop),BorderLayout.CENTER);
		con.add(Buttompane,BorderLayout.SOUTH);

		//启动
		try
		{
			ss=new ServerSocket(9000);
			AllDeal ad=new AllDeal();
			ad.start();
			setVisible(true);
		}
		catch(Exception err)
		{
			//有实例，退出
			System.exit(0);
		}

		//窗体绑定事件
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				closeSocket();
				System.exit(0);
			}
		});
	}
	//关闭方法。
	public void closeSocket()
	{
		try
		{
			if(ss!=null)
			{
				ss.close();
				ss=null;
				client=null;
				all=null;
				names=null;
			}
		}catch(Exception e)
		{
			System.out.println("err4:"+e);
		}
	}

	//公告方法
	public void gonggao(int a,String s)
	{
		DataPack dp1=new DataPack();
		dp1.optype=a;
		dp1.txt=s;

		Enumeration en=all.keys();
		while(en.hasMoreElements())
		{
			Object key=en.nextElement();
			String tmp=(String)key;

			ClinetDeal cl=(ClinetDeal)all.get(key);
			try
			{
				cl.oos.writeObject(dp1);
				cl.oos.flush();
			}catch(IOException err)
			{
				System.out.println("err0:"+err);
			}
		}
	}
	//查看方法。
	public String chakan(String name)
	{
		Enumeration en=all.keys();
		while(en.hasMoreElements())
		{
			Object key=en.nextElement();
			String tmp=(String)key;
			if(tmp.equals(name))
			{
				return name;
			}
		}
		return null;
	}

	//删除用户方法
	public void del(String tmp)
	{
		ClinetDeal cl=(ClinetDeal)all.get(tmp);
		try
		{
			dlop.addElement(tmp+"用户被删除!");
			cl.oos.close();
			cl.ois.close();
			cl.interrupt();
		}
		catch(IOException err)
		{
			System.out.println("err1:"+err);
		}
	}

	//按钮事件的处理
	class btnAct implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			//删除用户
			if(e.getSource()==btn[0])
			{
				//得到要删除的用户名称
				delname = JOptionPane.showInputDialog(null,"输入要删除的用户名","输入用户名",JOptionPane.OK_CANCEL_OPTION);
				/* 对用户的选择情况进行处理
				 * 为null的话，用户选择取消按钮。不做处理
				 * 为""空的话，用户没有输入内容，提示重新输入
				 */
				if(delname==null)
				{}
				else if(delname.equals(""))
				{
					JOptionPane.showMessageDialog(server.this,"  用户姓名不能为空!\n  请重新输入");
				}
				else
				{
					/* 正常情况，调用查看方法，返回是否有该用户
					 * 为null的话。表示没有该用户名，提示用户不存在
					 * 否则，进行删除
					 */
					String tmp=chakan(delname);
					if(tmp==null)
					{
						JOptionPane.showMessageDialog(server.this,"该用户不存在！\n请重新输入!");
					}
					else
					{
						//最后弹出对话框，进行最后确认
						int  incept=JOptionPane.showConfirmDialog(server.this,"确定删除 "+delname+" 用户吗？","删除",JOptionPane.YES_NO_OPTION);
						if(incept==0)
						{
							//调用删除方法
							del(tmp);

							JOptionPane.showMessageDialog(server.this,delname+" 用户已经被删除!");

							//从list中删除该用户
							dluser.removeElement(delname);
							all.remove(delname);
							names.remove(delname);

							//调用公告方法，向所有用户发送该用户已经离开聊天室
							gonggao(3,delname);
						}
					}
				}
			}
			//向用户发出警告信息
			if(e.getSource()==btn[1])
			{
				String tmpname = JOptionPane.showInputDialog(null,"输入要警告的用户名","输入用户名",JOptionPane.OK_CANCEL_OPTION);
				if(tmpname==null)
				{}
				else if(tmpname.equals(""))
				{
					JOptionPane.showMessageDialog(server.this,"  用户姓名不能为空!\n  请重新输入");
				}
				else
				{
					String txt = JOptionPane.showInputDialog(null,"输入要警告话","输入",JOptionPane.OK_CANCEL_OPTION);
					if(txt==null)
					{}
					else if(txt.equals(""))
					{
						JOptionPane.showMessageDialog(server.this,"  警告话语不能为空!\n  请重新输入");
					}
					else
					{
						String tmp=chakan(tmpname);
						if(tmp!=null)
						{
							ClinetDeal cl=(ClinetDeal)all.get(tmp);
							DataPack dp1=new DataPack();
							dp1.optype=5;
							dp1.txt="******系统警告:"+txt;
							try
							{
								cl.oos.writeObject(dp1);
								cl.oos.flush();
								JOptionPane.showMessageDialog(server.this,"已经向"+tmpname+" 用户发出警告信息!");
							}
							catch(IOException err)
							{
								System.out.println("err2:"+err);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(server.this,"该用户不存在！\n请重新输入!");
						}
					}
				}
			}
			//广播系统信息
			if(e.getSource()==btn[2])
			{
				gonggao(5,ServerMessageTxt.getText());
				ServerMessageTxt.setText("");
			}
			if(e.getSource()==ServerMessageTxt)
			{
				gonggao(5,ServerMessageTxt.getText());
				ServerMessageTxt.setText("");
			}
		}
	}
	//负责所有客户的接待
	class AllDeal extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					//服务器等待用户的连接
					client=ss.accept();
					//服务器把连接上的用户的IP和端口号在LIST上显示
					dlop.addElement(client.getInetAddress ()+":  "+client.getPort() +"连接");
					//每连接上一个就启动一个线程,并启动它
					ClinetDeal cc=new ClinetDeal(client);
					cc.start();
				}
				catch(Exception err)
				{
					all=null;
					System.exit(1);
				}
			}
		}
	}

	//处理每一个连接上的客户
	class ClinetDeal extends Thread
	{
		public ObjectOutputStream oos=null;
		public ObjectInputStream ois=null;

		//生成数据包类
		DataPack dp=null;
		public ClinetDeal(Socket sc)
		{
			try
			{
				oos=new ObjectOutputStream(sc.getOutputStream());
				ois=new ObjectInputStream(sc.getInputStream());
				//取得连接上用户的姓名
				name=getLogName();
				//LIST添加 该用户的已经登陆
				dlop.addElement(client.getInetAddress ()+":  "+client.getPort() +"登陆");
				//把该用户的姓名添加到用户姓名列表里。
				dluser.addElement(name);
				//把该用户添加到姓名Vector里。
				names.add(name);

				//对所有用户发送新进来的用户
				Enumeration en=all.keys();
				while(en.hasMoreElements())
				{
					Object key=en.nextElement();
					ClinetDeal cl=(ClinetDeal)all.get(key);

					DataPack dp1=new DataPack();
					dp1.optype=4;
					dp1.con=name;

					cl.oos.writeObject(dp1);
					cl.oos.flush();
				}
				//加入Hashtable表
				all.put(name,this);

				//对新用户发所有的名单
				dp=new DataPack();
				dp.optype=4;
				dp.con=names;
				oos.writeObject(dp);
			}
			catch(Exception err)
			{
				System.out.println("err3:"+err);
			}
		}
		private String getLogName() //得到客户端的姓名
		{
			try
			{
				String n="";
				DataPack dp=(DataPack)ois.readObject();
				if(dp.optype==1)
				{
					n=(String)dp.con;
				}return n;
			}
			catch(Exception err){}
			return null;
		}
		public void run()
		{
			String exitname=null;
			//负责处理数据的转发
			while(true)
			{
				try
				{
					dp=(DataPack)ois.readObject();
					exitname=dp.from;
					if(dp.optype==2)
					{
						String con=dp.txt;
						boolean si=dp.si;

						if(si)  //是私聊的情况
						{
							String to=dp.to;
							String from=dp.from;
							String tmp1=chakan(from);
							String tmp=chakan(to);
							//分别向接收方和发送方发送
							if(tmp1!=null)
							{
								ClinetDeal cl=(ClinetDeal)all.get(tmp1);
								cl.oos.writeObject(dp);
								cl.oos.flush();
							}
							if(tmp!=null)
							{
								ClinetDeal cl=(ClinetDeal)all.get(tmp);
								cl.oos.writeObject(dp);
								cl.oos.flush();
							}
						}
						else  //不是私聊的情况
						{
							//广播
							Enumeration en=all.keys();
							while(en.hasMoreElements())
							{
								Object key=en.nextElement();
								ClinetDeal cl=(ClinetDeal)all.get(key);
								cl.oos.writeObject(dp);
								cl.oos.flush();
							}
						}
					}
					if(dp.optype==3)
					{
						dlop.addElement(exitname+"离开聊天室!");
						dluser.removeElement(exitname);
						names.remove(exitname);
						all.remove(exitname);
						//调用公告方法，向所有用户发送该用户已经离开聊天室
						gonggao(3,exitname);
						break;
					}
				}
				catch(Exception err)
				{
					names.remove(exitname);
					dluser.removeElement(exitname);

					break;
				}
			}
		}
	}
}