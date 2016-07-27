package client;

//客户端代码
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.text.*;
import client.about.*;
import data.DataPack;

public class Client extends JFrame
{
	//入口函数
	public static void main(String args[])
	{
		SetFont.setFont(new Font("宋体", 0, 12));
		new Client();
	}

	//定义菜单条
	private JMenu menuBegin=new JMenu("选择服务器");
	private JMenuItem LJItem=new JMenuItem("连接服务器");
	private JMenuItem DKItem=new JMenuItem("断开连接");
	private JMenuItem TCItem=new JMenuItem("退出");
	private JMenu menuAbout=new JMenu("关于");
	private JMenuItem AboutItem=new JMenuItem("关于本程序");
	JMenuBar menuBar=null;

	//Action事件的定义
	public  Action [] ac=null;
	public  Action  copy=null;
	public  Action paste=null;
	//定义右键菜单
	 JPopupMenu Pmenu=new JPopupMenu();
		JMenuItem []ji=new JMenuItem[4];
	 String []pmItemStr={"复制    Ctrl+C","粘贴    Ctrl+V","背景色设置","默认背景色"};


	//左边的一块面板，用于放置用户在线信息情况
	private JPanel leftPane=new JPanel();

	//放置一张图片
	private JLabel leftLab=new JLabel(new ImageIcon(Client.class.getResource("icon1/top.jpg")));

	//显示当前登录的用户名
	private JLabel userLab=new JLabel("当前用户",new ImageIcon(Client.class.getResource("icon1/head.jpg")),JLabel.LEFT);

	//显示当前所有在线用户
	private JLabel allUserLab=new JLabel("当前所有在线用户    0      ");

	//用于计算当前有多少用户在线
	private int allUserCount=0;


	//定义显示当前聊天室的所有人员列表
	private DefaultListModel dl=new DefaultListModel();
	private DefaultComboBoxModel dm=new DefaultComboBoxModel();
	private JList list=new JList(dl);

	//定义用户的聊天信息区的面板
	private JPanel messagePane=new JPanel();

	//定义表情，文字输入框的面板
	private JPanel ComponentPane=new JPanel();

	//定义显示用户的聊天信息TextPane
	private JTextPane txtPane=new JTextPane();

	//定义发送信息用的组件
	private JLabel []lab=new JLabel[2];
	private String []labStr={"我   对","要说的话"};

	//显示所有在线用户名单
	private JComboBox allU=new JComboBox();
	private DefaultComboBoxModel dcdmAllU=new DefaultComboBoxModel();

	private JComboBox [] jcomb=new JComboBox[3];
	private DefaultComboBoxModel dcdm1=new DefaultComboBoxModel();
	private DefaultComboBoxModel dcdm2=new DefaultComboBoxModel();
	private String [] str={"黑色","红色","橙色","蓝色","蓝绿色","绿色","红紫色","粉红色","黄色"};
	private DefaultComboBoxModel dcdm3=new DefaultComboBoxModel();
	private  String[] biaoqing ={"(无)","笑着说","哭着说","微笑道","一把鼻涕一把泪地痛诉道","嚎嚎大哭道",
								"打XX了一拳,喝一声道","踢了XX一脚,大吼","吻了XX一下,深情地说","紧张地说",
								"不屑一顾地说","低声下气地说","冷嘲热讽XX道","面无表情地说"};

	private JCheckBox [] jcheckb=new JCheckBox[3];
	private String [] jcheckbStr={"滚屏","分屏","私聊"};

	//输入要发送的话语的文本框
	private JTextField jtf=new JTextField();

	//发言,离开按钮
	private JButton sendBtn=new JButton("发言");
	private JButton exitBtn=new JButton("离开");

	//背景色设置按钮
	private JButton YSBtn=new JButton("背景色设置");
	private JButton MoBtn=new JButton("默认背景色");


	private String load1 ="【欢迎】　　　　　　欢迎来到本聊天室";
	private String load2 ="【欢迎】````````/;~~|`````````*@@@@@,````````````````";
	private String load3 ="【欢迎】```````|;`66|_````````@@@@@@@@,``````````````";
	private String load4 ="【欢迎】```````C`````_)```````aa`@@@@@@``````````````";
	private String load5 ="【欢迎】```````|```_|````````{_```?@@@@``````````````";
	private String load6 ="【欢迎】````````)``/``````````='`@@@@~```````````````";
	private String load7 ="【欢迎】```````|``＼```````````＼`(```````````````````";
	private String load8 ="【欢迎】``````||``|Y|`````````／```＼``````````````````";
	private String load9 ="【欢迎】``````||``|.|````````/`|``||``````````````````";
	private String load10="【欢迎】``````||``|.|````````＼`|``||`````````````````";
	private String load11="【欢迎】``````||``|.|`````````＼|__|＼`````````````````";
	private String load12="【欢迎】``````:|``|=:`````````||__|`＼````````````````";
	private String load13="【欢迎】``````||_`|`|`````````|(((```|```````````````";
	private String load14="【欢迎】　　－－－－－－－－－－－－－－－－－－－－－";

	//网络
	private DataPack dp=null;
	private Socket sc=null;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;

	//线程
	private RecTh t=null;


	//定义进入聊天室的昵称,服务器的地址,端口号
	private String name="";
	private String serverIP="";
	private int serverPort=0;

	//属性设置
	public static SimpleAttributeSet sa1=new SimpleAttributeSet();

	//光标样式
	Cursor defaultCur=new Cursor(DEFAULT_CURSOR);
	Cursor handCur=new Cursor(HAND_CURSOR);


	//构造器
	public Client()
	{
		//窗体属性
		setSize(1024,740);
		setTitle("欢迎来到本聊天室!");
		//构件菜单
		createMenu();

		//设置LIST的CellRenderer
		list.setCellRenderer(new hh());

		//本窗体绑定退出事件
		this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					closeSocket();
					System.exit(0);
				}
			});

		//得到JTextPane的事件
		ac=txtPane.getActions();
		for(int i=0;i<ac.length;i++)
		{
			String tmp=(String)(ac[i].getValue(Action.NAME));

			if(tmp.equals("copy-to-clipboard"))
			{
				copy=ac[i];
			}
			if(tmp.equals("paste-from-clipboard"))
			{
				paste=ac[i];
			}
		}
		//添加右键菜单
		 for(int i=0;i<ji.length;i++)
		 {
			 ji[i]=new JMenuItem(pmItemStr[i]);
			 ji[i].addActionListener(new ActionEv());
		 }
		 ji[0].addActionListener(copy);
		 ji[1].addActionListener(paste);
		 Pmenu.add(ji[0]);Pmenu.add(ji[1]);Pmenu.addSeparator();Pmenu.add(ji[2]);Pmenu.add(ji[3]);

		 //绑定右键菜单
		txtPane.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e)
				{
					if(e.isPopupTrigger())
					{
						Pmenu.setVisible(true);
					}
				}
				public void mouseEntered(MouseEvent e){}
				public void mouseExited(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
				public void mouseReleased(MouseEvent e)
				{
					if(e.isPopupTrigger())
					{
						Pmenu.show(txtPane,e.getX(),e.getY());
					}
				}
			});

		//启动是加载的字符串
		txtPane.replaceSelection(load14+"\n");
		txtPane.replaceSelection(load13+"\n");
		txtPane.replaceSelection(load12+"\n");
		txtPane.replaceSelection(load11+"\n");
		txtPane.replaceSelection(load10+"\n");
		txtPane.replaceSelection(load9+"\n");
		txtPane.replaceSelection(load8+"\n");
		txtPane.replaceSelection(load7+"\n");
		txtPane.replaceSelection(load6+"\n");
		txtPane.replaceSelection(load5+"\n");
		txtPane.replaceSelection(load4+"\n");
		txtPane.replaceSelection(load3+"\n");
		txtPane.replaceSelection(load2+"\n");
		txtPane.replaceSelection(load1+"\n");

		//设置文本编辑区的属性
		txtPane.setEditable(false);
		txtPane.setFont(new Font("宋体",0,15));
		txtPane.setSelectionColor(new Color(153,153,255));
		txtPane.setBackground(new Color(132,145,174,50).brighter());


		//标签初始化
		for(int i=0;i<lab.length;i++)
		{
			lab[i]=new JLabel(labStr[i]);
			ComponentPane.add(lab[i]);
		}

		//加载图片
		for(int i=0;i<23;i++)
		{
			dcdm1.addElement(new ImageIcon(Client.class.getResource("icon1/"+i+".gif")));
		}
		//加载颜色字符
		for(int i=0;i<str.length;i++)
		{
			dcdm2.addElement(str[i]);
		}
		//加载动作字符
		for(int i=0;i<biaoqing.length;i++)
		{
			dcdm3.addElement(biaoqing[i]);
		}

		//JComboBox的初始化
		for(int i=0;i<jcomb.length;i++)
		{
			jcomb[i]=new JComboBox();
			ComponentPane.add(jcomb[i]);
		}
		jcomb[1].setModel(dcdm1);
		jcomb[2].setModel(dcdm2);
		jcomb[0].setModel(dcdm3);
		allU.setModel(dcdmAllU);

		//JCheckBox的初始化
		for(int i=0;i<jcheckb.length;i++)
		{
			jcheckb[i]=new JCheckBox(jcheckbStr[i]);
			jcheckb[i].setForeground(Color.blue);
			jcheckb[i].setBackground(new Color(221,117,162).brighter());
			ComponentPane.add(jcheckb[i]);
		}


		//设置各组件的属性
		lab[0].setForeground(Color.blue);
		lab[1].setForeground(Color.blue);

		sendBtn.setMargin(new Insets(0,0,0,0));
		exitBtn.setMargin(new Insets(0,0,0,0));
		YSBtn.setMargin(new Insets(0,0,0,0));
		MoBtn.setMargin(new Insets(0,0,0,0));

		userLab.setForeground(Color.red);
		userLab.setFont(new Font("宋体",0,13));
		allUserLab.setOpaque(true);
		allUserLab.setForeground(Color.white);
		allUserLab.setBackground(Color.red);

		list.setFont(new Font("宋体",0,13));
		list.setForeground(new Color(255,102,153));
		list.setBackground(new Color(240,240,240));
		list.setSelectedIndex(0);

		//设置各组件的位置
		lab[0].setBounds(15,5,80,25);allU.setBounds(75,7,100,20);
		jcomb[0].setBounds(180,7,160,20);jcomb[1].setBounds(355,7,80,20);
		jcomb[2].setBounds(450,7,80,20);
		jcheckb[0].setBounds(530,5,50,25);jcheckb[1].setBounds(585,5,50,25);
		YSBtn.setBounds(650,5,80,23);MoBtn.setBounds(735,5,80,23);
		lab[1].setBounds(15,45,50,25);jtf.setBounds(80,45,450,23);
		jcheckb[2].setBounds(530,45,50,25);
		sendBtn.setBounds(590,45,50,23);exitBtn.setBounds(650,45,50,23);
		ComponentPane.setPreferredSize(new Dimension(1024,100));
		ComponentPane.setBackground(new Color(221,117,162).brighter());
		ComponentPane.setLayout(null);
		ComponentPane.add(jtf);ComponentPane.add(sendBtn);ComponentPane.add(exitBtn);
		ComponentPane.add(YSBtn);ComponentPane.add(MoBtn);
		ComponentPane.add(allU);

		//聊天信息区的面板添加聊天信息TextPane和表情，文字输入框的面板
		messagePane.setLayout(new BorderLayout());
		messagePane.add(new JScrollPane(txtPane),BorderLayout.CENTER);
		messagePane.add(ComponentPane,BorderLayout.SOUTH);

		leftPane.setLayout(new FlowLayout(FlowLayout.LEFT,1,0));
		leftPane.setPreferredSize(new Dimension(140,600));
		leftPane.setMaximumSize(new Dimension(140,600));
		leftPane.add(leftLab);
		leftPane.add(userLab);
		leftPane.add(allUserLab);
		leftPane.add(list);

		Container con=getContentPane();
		con.setLayout(new BorderLayout());
		JScrollPane jsp=new JScrollPane(leftPane);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		con.add(jsp,BorderLayout.WEST);
		con.add(messagePane,BorderLayout.CENTER);
		setVisible(true);

		//事件的绑定
		exitBtn.addActionListener(new ActionEv());
		sendBtn.addActionListener(new ActionEv());
		YSBtn.addActionListener(new ActionEv());
		MoBtn.addActionListener(new ActionEv());
		jtf.addActionListener(new ActionEv());
		jcomb[2].addActionListener(new ActionEv());

		//绑定用户名字LIST事件
		list.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e)
				{
					String listValue=(String)list.getSelectedValue();
					dcdmAllU.setSelectedItem(listValue);
				}
				public void mouseEntered(MouseEvent e)
				{
					list.setCursor(handCur);
				}
				public void mouseExited(MouseEvent e)
				{
					list.setCursor(defaultCur);
				}
				public void mousePressed(MouseEvent e)
				{}
				public void mouseReleased(MouseEvent e)
				{
				}
			});
	}

	//退出时,关闭 Socket方法
	public void closeSocket()
	{
		try
		{
			if(sc!=null)
			{
				sc.close();
				sc=null;
				ois=null;
				oos=null;
			}
		}catch(Exception e)
		{
			System.out.println("err0:"+e);
		}
	}

	//生成菜单条方法
	public void createMenu()
	{
		menuBar=new JMenuBar();
		menuBar.setBackground(new Color(160,178,255));
		menuBegin.add(LJItem);
		menuBegin.add(DKItem);
		menuBegin.addSeparator();
		menuBegin.add(TCItem);
		menuAbout.add(AboutItem);
		LJItem.addActionListener(new MenuEv());
		DKItem.addActionListener(new MenuEv());
		TCItem.addActionListener(new MenuEv());
		AboutItem.addActionListener(new MenuEv());
		menuBar.add(menuBegin);
		menuBar.add(menuAbout);
		DKItem.setEnabled(false);
		setJMenuBar(menuBar);
	}

	//listCellRenderer
	class hh implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if(index==0)
			{
				JLabel bb=new JLabel((String)value);
				bb.setForeground(new Color(221,117,162));
				bb.setToolTipText("选择该用户就可以对他发送信息！");
				if(isSelected==true)
				{
					bb.setForeground(Color.blue);
				}
				return bb;
			}
			else
			{
				JLabel bb=new JLabel((String)value,new ImageIcon(Client.class.getResource("icon1/head.jpg")),JLabel.LEFT);
				bb.setForeground(new Color(221,117,162));
				bb.setToolTipText("选择该用户就可以对他发送信息！");
				if(isSelected==true)
				{
					bb.setForeground(Color.blue);
				}
				return bb;
			}
		}
	}

	//设置背景色方法
	public void setBackcolor()
	{
		JColorChooser fc=new JColorChooser();
		Color col=fc.showDialog(Client.this,"确认(A)",Color.red);
		if(col!=null)
		{
			txtPane.setBackground(col);
		}
	}

	//按钮，输入框，右键菜单的事件处理
	class ActionEv implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==exitBtn)//退出按钮
			{
				exit();
				System.exit(0);
			}
			if(e.getSource()==sendBtn)//发送按钮
			{
				send();
			}
			if(e.getSource()==YSBtn)//背景色设置
			{
				setBackcolor();
			}
			if(e.getSource()==MoBtn)//恢复为默认背景色
			{
				txtPane.setBackground(new Color(132,145,174,50).brighter());
			}
			if(e.getSource()==jtf)//输入框
			{
				send();
			}
			if(e.getSource()==jcomb[2])	//颜色框
			{
				String color=(String)jcomb[2].getSelectedItem();
				if(color.equals("黑色"))StyleConstants.setForeground(sa1,Color.black);
				if(color.equals("红色"))StyleConstants.setForeground(sa1,Color.red);
				if(color.equals("橙色"))StyleConstants.setForeground(sa1,Color.orange);
				if(color.equals("蓝色"))StyleConstants.setForeground(sa1,Color.blue);
				if(color.equals("蓝绿色"))StyleConstants.setForeground(sa1,Color.cyan);
				if(color.equals("绿色"))StyleConstants.setForeground(sa1,Color.green);
				if(color.equals("红紫色"))StyleConstants.setForeground(sa1,Color.magenta);
				if(color.equals("粉红色"))StyleConstants.setForeground(sa1,Color.pink);
				if(color.equals("黄色"))StyleConstants.setForeground(sa1,Color.yellow);
			}
			if(e.getSource()==ji[2])//右键菜单的设置背景色事件
			{
				setBackcolor();
			}
			if(e.getSource()==ji[3])//右键菜单的默认背景色事件
			{
				txtPane.setBackground(new Color(132,145,174,50).brighter());
			}
		}
	}

	//发送方法
	public void send()
	{
		if(LJItem.isEnabled())
		{
			JOptionPane.showMessageDialog(Client.this,"还没有连接服务器！\n请先连接服务器!");
		}
		else
		{
			String txt=jtf.getText().trim();
			if(txt.length()>100)
			{
				JOptionPane.showMessageDialog(Client.this,"要发送的字符太长！\n  请重新输入！");
			}
			else
			{
				if(jtf.getText().trim().equals("") | jtf.getText().trim()==null)
				{
					JOptionPane.showMessageDialog(Client.this,"发送内容不能为空！");
					return;
				}
				else
				{
					try
					{
						//收集数据
						DataPack pk=new DataPack();
						pk.optype=2;
						pk.from=name;
						pk.to=(String)dcdmAllU.getSelectedItem();
						pk.biaoqing=(String)jcomb[0].getSelectedItem();
						pk.txt=jtf.getText().trim();

						pk.expr=(ImageIcon)jcomb[1].getSelectedItem();

						if(jcheckb[2].isSelected())
						{
							pk.si=true;
						}
						else
						{
							pk.si=false;
						}
						oos.writeObject(pk);
						oos.flush();

					}
					catch(Exception err)
					{
					}
					jtf.setText("");
					jcomb[0].setSelectedIndex(0);
					jcomb[1].setSelectedIndex(0);
				}
			}
		}
	}

	//退出方法
	public void exit()
	{
		DataPack pk=new DataPack();
		pk.optype=3;
		pk.from=name;
		try
		{
			oos.writeObject(pk);
			oos.flush();
		}
		catch(IOException err)
		{
			System.out.println("err1:"+err);
		}
		closeSocket();
	}

	//断开连接方法
	public void disconnect()
	{
		exit();
		closeSocket();
	 }


	//菜单事件
	class MenuEv implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==AboutItem)
			{
				new client.about.About(Client.this,"关于本聊天室",true);
			}
			if(e.getSource()==TCItem) //退出菜单
			{
				exit();
				System.exit(0);
			}
			if(e.getSource()==DKItem) //断开连接菜单
			{
				disconnect();
				LJItem.setEnabled(true);
				DKItem.setEnabled(false);
			}
			if(e.getSource()==LJItem) //连接菜单
			{
				name=JOptionPane.showInputDialog("请输入您的昵称:(最多八个字符)","");
				if(name==null)
				{
				}
				else if(name.equals(""))
				{
					JOptionPane.showMessageDialog(Client.this,"  昵程不能为空!\n  请重新输入");
				}
				else
				{
					while(name.length()>8 | name.length()==0)
					{
						JOptionPane.showMessageDialog(Client.this,"  昵程不符合要求!\n  请重新输入");
						name = JOptionPane.showInputDialog(Client.this,"请输入您的昵称:(最多八个字符)","输入昵称",JOptionPane.OK_CANCEL_OPTION);
					}
					serverIP =JOptionPane.showInputDialog("请输入聊天室服务器的IP地址:","127.0.0.1");
					if(serverIP==null)
					{}
					else if(serverIP.equals(""))
					{
						JOptionPane.showMessageDialog(Client.this,"  服务器的IP地址不能为空!\n  请重新输入");
					}
					else
					{
						String tempPort = JOptionPane.showInputDialog("请输入聊天室服务器的端口号:","9000");
						if(tempPort==null)
						{}
						else if(tempPort.equals(""))
						{
							JOptionPane.showMessageDialog(Client.this,"  服务器的端口号不能为空!\n  请重新输入");
						}
						else
						{
							try
							{
								serverPort=Integer.parseInt(tempPort);
							}catch(Exception err)
							{
								JOptionPane.showMessageDialog(Client.this,"输入的端口号格式不正确!");
								tempPort = JOptionPane.showInputDialog(Client.this,"请输入聊天室服务器的端口号:","输入服务器端口号",JOptionPane.OK_CANCEL_OPTION);
							}

							//连接服务器
							try
							{
								sc=new Socket(serverIP,serverPort);
								ois=new ObjectInputStream(sc.getInputStream());
								oos=new ObjectOutputStream(sc.getOutputStream());
								//连接上服务器后,把该用户的姓名传到服务器
								denglu(name);

								//把List的第一项加为“所有人”
								dl.addElement("所有人");
								dcdmAllU.addElement("所有人");
								//连接上服务器后，就取得所有用户的姓名加入list
								Vector users=getUsers();
								allUserCount=users.size();
								for(int i=0;i<users.size();i++)
								{
									String tmp=(String)users.get(i);
									//屏蔽自己
									if(!(tmp.equals(name)))
									{
										dl.addElement(tmp);
										dcdmAllU.addElement(tmp);
									}
								}

								//启动线程
								t=new RecTh();
								t.start();
								//提示经连接服务器
								JOptionPane.showMessageDialog(Client.this,"已经连接服务器!");

								//连接后显示下面的公告字符串
								DefaultStyledDocument dsd=(DefaultStyledDocument)(txtPane.getStyledDocument());
								SimpleAttributeSet sa =new SimpleAttributeSet();
								String ss="欢迎来到本聊天室！请注意文明用语！不要说反动，侮辱，不健康的话语！\n否则将被踢出本聊天室!\n您当前的用户名为："+name+"\n";
								dsd.insertString(dsd.getLength(),ss,sa);

								//把userLab改为本用户姓名
								userLab.setText(name);
								//显示当前所有在线用户
								allUserLab.setText("当前所有在线用户  "+allUserCount+"    ");
								//菜单选项进行相应的设置
								LJItem.setEnabled(false);
								DKItem.setEnabled(true);
							}
							catch(Exception err)
							{
								System.out.println("err4:"+err);
								JOptionPane.showMessageDialog(null,"不能与服务器正常连接!\n  请稍后再试!");
							}
						}
					}
				}
			}
		}

		//得到所有用户名字的方法
		public Vector getUsers()
		{
			try
			{
				DataPack dp=(DataPack)ois.readObject();
				if(dp.optype==4)
				{
					return (Vector)dp.con;
				}
			}
			catch(Exception err){}
			return null;
		}

		//登陆的方法
		public void denglu(String name)
		{
			try
			{
				DataPack pk=new DataPack();
				pk.optype=1;
				pk.con=name;

				oos.writeObject(pk);
				oos.flush();
			}
			catch(Exception err){}
		}
	}

	//线程类
	class RecTh extends Thread
	{
		public void run()
		{
			//插入文字是用的字符属性
			DefaultStyledDocument dsd=(DefaultStyledDocument)(txtPane.getStyledDocument());
			SimpleAttributeSet sa =new SimpleAttributeSet();

			String shuo="说：";
			while(true)
			{
				try
				{
					dp=(DataPack)ois.readObject();
					Object user=dp.con;
					Object con=dp.txt;
					String from=dp.from;
					String to=dp.to;
					String biaoqing=dp.biaoqing;
					ImageIcon expr=dp.expr;

					if(dp.optype==2) //聊天
					{
						DefaultStyledDocument sd=(DefaultStyledDocument)(txtPane.getStyledDocument());
						Position pp=sd.getEndPosition();

						//si判定是否是私聊
						boolean si=dp.si;

						if(si){shuo=" 悄悄的说：";}
						else{shuo=" 说：";}

						//设置光标的位置
						txtPane.setSelectionStart(dsd.getLength());
						if(biaoqing.equals("(无)"))
						{
							String ss="*"+from +"   对  "+to+shuo+con+"\n";
							try
							{
								txtPane.insertIcon(expr);
								dsd.insertString(dsd.getLength(),ss,sa);
							}
							catch(Exception err)
							{
								System.out.println("err5:"+err);
								err.printStackTrace();
							}
						}
						else
						{
							String ss="*"+from +"   对  "+to+" "+biaoqing+":"+con+"\n";
							dsd.insertString(dsd.getLength(),ss,sa);
							txtPane.insertIcon(expr);
						}
						int i=pp.getOffset();
						String len=(String)con;
						sd.setCharacterAttributes(i-len.length()-2,i,Client.sa1,true);

					}
					if(dp.optype==3) //退出
					{
						dl.removeElement(con);
						dcdmAllU.removeElement(con);
						String ss="【公告】 "+con+"离开本聊天室!\n";
						dsd.insertString(dsd.getLength(),ss,sa);
						allUserCount-=1;
						allUserLab.setText("当前所有在线用户  "+allUserCount+"    ");
					}
					if(dp.optype==4) //新用户登陆
					{
						dl.addElement((String)user);
						dcdmAllU.addElement((String)user);
						String ss="【公告】 "+user+"进入本聊天室!"+"\n";
						dsd.insertString(dsd.getLength(),ss,sa);
						allUserCount+=1;
						allUserLab.setText("当前所有在线用户  "+allUserCount+"    ");
					}

					if(dp.optype==5) //系统广播信息
					{
						String ss="【公告】 "+con+"\n";
						dsd.insertString(dsd.getLength(),ss,sa);
					}
				}catch(Exception err)
				{
					String ss="已经和服务器断开连接!\n";
					try
					{
						dsd.insertString(dsd.getLength(),ss,sa);
						allUserLab.setText("当前所有在线用户  0    ");
						dl.removeAllElements();
						dcdmAllU.removeAllElements();
						userLab.setText("当前用户");
						DKItem.setEnabled(false);
						LJItem.setEnabled(true);
					}
					catch(Exception ee){System.out.println("err6:"+ee);}
					break;
				}
			}
		}
	}
}
