package client;

//�ͻ��˴���
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
	//��ں���
	public static void main(String args[])
	{
		SetFont.setFont(new Font("����", 0, 12));
		new Client();
	}

	//����˵���
	private JMenu menuBegin=new JMenu("ѡ�������");
	private JMenuItem LJItem=new JMenuItem("���ӷ�����");
	private JMenuItem DKItem=new JMenuItem("�Ͽ�����");
	private JMenuItem TCItem=new JMenuItem("�˳�");
	private JMenu menuAbout=new JMenu("����");
	private JMenuItem AboutItem=new JMenuItem("���ڱ�����");
	JMenuBar menuBar=null;

	//Action�¼��Ķ���
	public  Action [] ac=null;
	public  Action  copy=null;
	public  Action paste=null;
	//�����Ҽ��˵�
	 JPopupMenu Pmenu=new JPopupMenu();
		JMenuItem []ji=new JMenuItem[4];
	 String []pmItemStr={"����    Ctrl+C","ճ��    Ctrl+V","����ɫ����","Ĭ�ϱ���ɫ"};


	//��ߵ�һ����壬���ڷ����û�������Ϣ���
	private JPanel leftPane=new JPanel();

	//����һ��ͼƬ
	private JLabel leftLab=new JLabel(new ImageIcon(Client.class.getResource("icon1/top.jpg")));

	//��ʾ��ǰ��¼���û���
	private JLabel userLab=new JLabel("��ǰ�û�",new ImageIcon(Client.class.getResource("icon1/head.jpg")),JLabel.LEFT);

	//��ʾ��ǰ���������û�
	private JLabel allUserLab=new JLabel("��ǰ���������û�    0      ");

	//���ڼ��㵱ǰ�ж����û�����
	private int allUserCount=0;


	//������ʾ��ǰ�����ҵ�������Ա�б�
	private DefaultListModel dl=new DefaultListModel();
	private DefaultComboBoxModel dm=new DefaultComboBoxModel();
	private JList list=new JList(dl);

	//�����û���������Ϣ�������
	private JPanel messagePane=new JPanel();

	//������飬�������������
	private JPanel ComponentPane=new JPanel();

	//������ʾ�û���������ϢTextPane
	private JTextPane txtPane=new JTextPane();

	//���巢����Ϣ�õ����
	private JLabel []lab=new JLabel[2];
	private String []labStr={"��   ��","Ҫ˵�Ļ�"};

	//��ʾ���������û�����
	private JComboBox allU=new JComboBox();
	private DefaultComboBoxModel dcdmAllU=new DefaultComboBoxModel();

	private JComboBox [] jcomb=new JComboBox[3];
	private DefaultComboBoxModel dcdm1=new DefaultComboBoxModel();
	private DefaultComboBoxModel dcdm2=new DefaultComboBoxModel();
	private String [] str={"��ɫ","��ɫ","��ɫ","��ɫ","����ɫ","��ɫ","����ɫ","�ۺ�ɫ","��ɫ"};
	private DefaultComboBoxModel dcdm3=new DefaultComboBoxModel();
	private  String[] biaoqing ={"(��)","Ц��˵","����˵","΢Ц��","һ�ѱ���һ�����ʹ�ߵ�","������޵�",
								"��XX��һȭ,��һ����","����XXһ��,���","����XXһ��,�����˵","���ŵ�˵",
								"��мһ�˵�˵","����������˵","�䳰�ȷ�XX��","���ޱ����˵"};

	private JCheckBox [] jcheckb=new JCheckBox[3];
	private String [] jcheckbStr={"����","����","˽��"};

	//����Ҫ���͵Ļ�����ı���
	private JTextField jtf=new JTextField();

	//����,�뿪��ť
	private JButton sendBtn=new JButton("����");
	private JButton exitBtn=new JButton("�뿪");

	//����ɫ���ð�ť
	private JButton YSBtn=new JButton("����ɫ����");
	private JButton MoBtn=new JButton("Ĭ�ϱ���ɫ");


	private String load1 ="����ӭ����������������ӭ������������";
	private String load2 ="����ӭ��````````/;~~|`````````*@@@@@,````````````````";
	private String load3 ="����ӭ��```````|;`66|_````````@@@@@@@@,``````````````";
	private String load4 ="����ӭ��```````C`````_)```````aa`@@@@@@``````````````";
	private String load5 ="����ӭ��```````|```_|````````{_```?@@@@``````````````";
	private String load6 ="����ӭ��````````)``/``````````='`@@@@~```````````````";
	private String load7 ="����ӭ��```````|``��```````````��`(```````````````````";
	private String load8 ="����ӭ��``````||``|Y|`````````��```��``````````````````";
	private String load9 ="����ӭ��``````||``|.|````````/`|``||``````````````````";
	private String load10="����ӭ��``````||``|.|````````��`|``||`````````````````";
	private String load11="����ӭ��``````||``|.|`````````��|__|��`````````````````";
	private String load12="����ӭ��``````:|``|=:`````````||__|`��````````````````";
	private String load13="����ӭ��``````||_`|`|`````````|(((```|```````````````";
	private String load14="����ӭ������������������������������������������������";

	//����
	private DataPack dp=null;
	private Socket sc=null;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;

	//�߳�
	private RecTh t=null;


	//������������ҵ��ǳ�,�������ĵ�ַ,�˿ں�
	private String name="";
	private String serverIP="";
	private int serverPort=0;

	//��������
	public static SimpleAttributeSet sa1=new SimpleAttributeSet();

	//�����ʽ
	Cursor defaultCur=new Cursor(DEFAULT_CURSOR);
	Cursor handCur=new Cursor(HAND_CURSOR);


	//������
	public Client()
	{
		//��������
		setSize(1024,740);
		setTitle("��ӭ������������!");
		//�����˵�
		createMenu();

		//����LIST��CellRenderer
		list.setCellRenderer(new hh());

		//��������˳��¼�
		this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					closeSocket();
					System.exit(0);
				}
			});

		//�õ�JTextPane���¼�
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
		//����Ҽ��˵�
		 for(int i=0;i<ji.length;i++)
		 {
			 ji[i]=new JMenuItem(pmItemStr[i]);
			 ji[i].addActionListener(new ActionEv());
		 }
		 ji[0].addActionListener(copy);
		 ji[1].addActionListener(paste);
		 Pmenu.add(ji[0]);Pmenu.add(ji[1]);Pmenu.addSeparator();Pmenu.add(ji[2]);Pmenu.add(ji[3]);

		 //���Ҽ��˵�
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

		//�����Ǽ��ص��ַ���
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

		//�����ı��༭��������
		txtPane.setEditable(false);
		txtPane.setFont(new Font("����",0,15));
		txtPane.setSelectionColor(new Color(153,153,255));
		txtPane.setBackground(new Color(132,145,174,50).brighter());


		//��ǩ��ʼ��
		for(int i=0;i<lab.length;i++)
		{
			lab[i]=new JLabel(labStr[i]);
			ComponentPane.add(lab[i]);
		}

		//����ͼƬ
		for(int i=0;i<23;i++)
		{
			dcdm1.addElement(new ImageIcon(Client.class.getResource("icon1/"+i+".gif")));
		}
		//������ɫ�ַ�
		for(int i=0;i<str.length;i++)
		{
			dcdm2.addElement(str[i]);
		}
		//���ض����ַ�
		for(int i=0;i<biaoqing.length;i++)
		{
			dcdm3.addElement(biaoqing[i]);
		}

		//JComboBox�ĳ�ʼ��
		for(int i=0;i<jcomb.length;i++)
		{
			jcomb[i]=new JComboBox();
			ComponentPane.add(jcomb[i]);
		}
		jcomb[1].setModel(dcdm1);
		jcomb[2].setModel(dcdm2);
		jcomb[0].setModel(dcdm3);
		allU.setModel(dcdmAllU);

		//JCheckBox�ĳ�ʼ��
		for(int i=0;i<jcheckb.length;i++)
		{
			jcheckb[i]=new JCheckBox(jcheckbStr[i]);
			jcheckb[i].setForeground(Color.blue);
			jcheckb[i].setBackground(new Color(221,117,162).brighter());
			ComponentPane.add(jcheckb[i]);
		}


		//���ø����������
		lab[0].setForeground(Color.blue);
		lab[1].setForeground(Color.blue);

		sendBtn.setMargin(new Insets(0,0,0,0));
		exitBtn.setMargin(new Insets(0,0,0,0));
		YSBtn.setMargin(new Insets(0,0,0,0));
		MoBtn.setMargin(new Insets(0,0,0,0));

		userLab.setForeground(Color.red);
		userLab.setFont(new Font("����",0,13));
		allUserLab.setOpaque(true);
		allUserLab.setForeground(Color.white);
		allUserLab.setBackground(Color.red);

		list.setFont(new Font("����",0,13));
		list.setForeground(new Color(255,102,153));
		list.setBackground(new Color(240,240,240));
		list.setSelectedIndex(0);

		//���ø������λ��
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

		//������Ϣ����������������ϢTextPane�ͱ��飬�������������
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

		//�¼��İ�
		exitBtn.addActionListener(new ActionEv());
		sendBtn.addActionListener(new ActionEv());
		YSBtn.addActionListener(new ActionEv());
		MoBtn.addActionListener(new ActionEv());
		jtf.addActionListener(new ActionEv());
		jcomb[2].addActionListener(new ActionEv());

		//���û�����LIST�¼�
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

	//�˳�ʱ,�ر� Socket����
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

	//���ɲ˵�������
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
				bb.setToolTipText("ѡ����û��Ϳ��Զ���������Ϣ��");
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
				bb.setToolTipText("ѡ����û��Ϳ��Զ���������Ϣ��");
				if(isSelected==true)
				{
					bb.setForeground(Color.blue);
				}
				return bb;
			}
		}
	}

	//���ñ���ɫ����
	public void setBackcolor()
	{
		JColorChooser fc=new JColorChooser();
		Color col=fc.showDialog(Client.this,"ȷ��(A)",Color.red);
		if(col!=null)
		{
			txtPane.setBackground(col);
		}
	}

	//��ť��������Ҽ��˵����¼�����
	class ActionEv implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==exitBtn)//�˳���ť
			{
				exit();
				System.exit(0);
			}
			if(e.getSource()==sendBtn)//���Ͱ�ť
			{
				send();
			}
			if(e.getSource()==YSBtn)//����ɫ����
			{
				setBackcolor();
			}
			if(e.getSource()==MoBtn)//�ָ�ΪĬ�ϱ���ɫ
			{
				txtPane.setBackground(new Color(132,145,174,50).brighter());
			}
			if(e.getSource()==jtf)//�����
			{
				send();
			}
			if(e.getSource()==jcomb[2])	//��ɫ��
			{
				String color=(String)jcomb[2].getSelectedItem();
				if(color.equals("��ɫ"))StyleConstants.setForeground(sa1,Color.black);
				if(color.equals("��ɫ"))StyleConstants.setForeground(sa1,Color.red);
				if(color.equals("��ɫ"))StyleConstants.setForeground(sa1,Color.orange);
				if(color.equals("��ɫ"))StyleConstants.setForeground(sa1,Color.blue);
				if(color.equals("����ɫ"))StyleConstants.setForeground(sa1,Color.cyan);
				if(color.equals("��ɫ"))StyleConstants.setForeground(sa1,Color.green);
				if(color.equals("����ɫ"))StyleConstants.setForeground(sa1,Color.magenta);
				if(color.equals("�ۺ�ɫ"))StyleConstants.setForeground(sa1,Color.pink);
				if(color.equals("��ɫ"))StyleConstants.setForeground(sa1,Color.yellow);
			}
			if(e.getSource()==ji[2])//�Ҽ��˵������ñ���ɫ�¼�
			{
				setBackcolor();
			}
			if(e.getSource()==ji[3])//�Ҽ��˵���Ĭ�ϱ���ɫ�¼�
			{
				txtPane.setBackground(new Color(132,145,174,50).brighter());
			}
		}
	}

	//���ͷ���
	public void send()
	{
		if(LJItem.isEnabled())
		{
			JOptionPane.showMessageDialog(Client.this,"��û�����ӷ�������\n�������ӷ�����!");
		}
		else
		{
			String txt=jtf.getText().trim();
			if(txt.length()>100)
			{
				JOptionPane.showMessageDialog(Client.this,"Ҫ���͵��ַ�̫����\n  ���������룡");
			}
			else
			{
				if(jtf.getText().trim().equals("") | jtf.getText().trim()==null)
				{
					JOptionPane.showMessageDialog(Client.this,"�������ݲ���Ϊ�գ�");
					return;
				}
				else
				{
					try
					{
						//�ռ�����
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

	//�˳�����
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

	//�Ͽ����ӷ���
	public void disconnect()
	{
		exit();
		closeSocket();
	 }


	//�˵��¼�
	class MenuEv implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==AboutItem)
			{
				new client.about.About(Client.this,"���ڱ�������",true);
			}
			if(e.getSource()==TCItem) //�˳��˵�
			{
				exit();
				System.exit(0);
			}
			if(e.getSource()==DKItem) //�Ͽ����Ӳ˵�
			{
				disconnect();
				LJItem.setEnabled(true);
				DKItem.setEnabled(false);
			}
			if(e.getSource()==LJItem) //���Ӳ˵�
			{
				name=JOptionPane.showInputDialog("�����������ǳ�:(���˸��ַ�)","");
				if(name==null)
				{
				}
				else if(name.equals(""))
				{
					JOptionPane.showMessageDialog(Client.this,"  �ǳ̲���Ϊ��!\n  ����������");
				}
				else
				{
					while(name.length()>8 | name.length()==0)
					{
						JOptionPane.showMessageDialog(Client.this,"  �ǳ̲�����Ҫ��!\n  ����������");
						name = JOptionPane.showInputDialog(Client.this,"�����������ǳ�:(���˸��ַ�)","�����ǳ�",JOptionPane.OK_CANCEL_OPTION);
					}
					serverIP =JOptionPane.showInputDialog("�����������ҷ�������IP��ַ:","127.0.0.1");
					if(serverIP==null)
					{}
					else if(serverIP.equals(""))
					{
						JOptionPane.showMessageDialog(Client.this,"  ��������IP��ַ����Ϊ��!\n  ����������");
					}
					else
					{
						String tempPort = JOptionPane.showInputDialog("�����������ҷ������Ķ˿ں�:","9000");
						if(tempPort==null)
						{}
						else if(tempPort.equals(""))
						{
							JOptionPane.showMessageDialog(Client.this,"  �������Ķ˿ںŲ���Ϊ��!\n  ����������");
						}
						else
						{
							try
							{
								serverPort=Integer.parseInt(tempPort);
							}catch(Exception err)
							{
								JOptionPane.showMessageDialog(Client.this,"����Ķ˿ںŸ�ʽ����ȷ!");
								tempPort = JOptionPane.showInputDialog(Client.this,"�����������ҷ������Ķ˿ں�:","����������˿ں�",JOptionPane.OK_CANCEL_OPTION);
							}

							//���ӷ�����
							try
							{
								sc=new Socket(serverIP,serverPort);
								ois=new ObjectInputStream(sc.getInputStream());
								oos=new ObjectOutputStream(sc.getOutputStream());
								//�����Ϸ�������,�Ѹ��û�����������������
								denglu(name);

								//��List�ĵ�һ���Ϊ�������ˡ�
								dl.addElement("������");
								dcdmAllU.addElement("������");
								//�����Ϸ������󣬾�ȡ�������û�����������list
								Vector users=getUsers();
								allUserCount=users.size();
								for(int i=0;i<users.size();i++)
								{
									String tmp=(String)users.get(i);
									//�����Լ�
									if(!(tmp.equals(name)))
									{
										dl.addElement(tmp);
										dcdmAllU.addElement(tmp);
									}
								}

								//�����߳�
								t=new RecTh();
								t.start();
								//��ʾ�����ӷ�����
								JOptionPane.showMessageDialog(Client.this,"�Ѿ����ӷ�����!");

								//���Ӻ���ʾ����Ĺ����ַ���
								DefaultStyledDocument dsd=(DefaultStyledDocument)(txtPane.getStyledDocument());
								SimpleAttributeSet sa =new SimpleAttributeSet();
								String ss="��ӭ�����������ң���ע�����������Ҫ˵���������裬�������Ļ��\n���򽫱��߳���������!\n����ǰ���û���Ϊ��"+name+"\n";
								dsd.insertString(dsd.getLength(),ss,sa);

								//��userLab��Ϊ���û�����
								userLab.setText(name);
								//��ʾ��ǰ���������û�
								allUserLab.setText("��ǰ���������û�  "+allUserCount+"    ");
								//�˵�ѡ�������Ӧ������
								LJItem.setEnabled(false);
								DKItem.setEnabled(true);
							}
							catch(Exception err)
							{
								System.out.println("err4:"+err);
								JOptionPane.showMessageDialog(null,"�������������������!\n  ���Ժ�����!");
							}
						}
					}
				}
			}
		}

		//�õ������û����ֵķ���
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

		//��½�ķ���
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

	//�߳���
	class RecTh extends Thread
	{
		public void run()
		{
			//�����������õ��ַ�����
			DefaultStyledDocument dsd=(DefaultStyledDocument)(txtPane.getStyledDocument());
			SimpleAttributeSet sa =new SimpleAttributeSet();

			String shuo="˵��";
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

					if(dp.optype==2) //����
					{
						DefaultStyledDocument sd=(DefaultStyledDocument)(txtPane.getStyledDocument());
						Position pp=sd.getEndPosition();

						//si�ж��Ƿ���˽��
						boolean si=dp.si;

						if(si){shuo=" ���ĵ�˵��";}
						else{shuo=" ˵��";}

						//���ù���λ��
						txtPane.setSelectionStart(dsd.getLength());
						if(biaoqing.equals("(��)"))
						{
							String ss="*"+from +"   ��  "+to+shuo+con+"\n";
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
							String ss="*"+from +"   ��  "+to+" "+biaoqing+":"+con+"\n";
							dsd.insertString(dsd.getLength(),ss,sa);
							txtPane.insertIcon(expr);
						}
						int i=pp.getOffset();
						String len=(String)con;
						sd.setCharacterAttributes(i-len.length()-2,i,Client.sa1,true);

					}
					if(dp.optype==3) //�˳�
					{
						dl.removeElement(con);
						dcdmAllU.removeElement(con);
						String ss="�����桿 "+con+"�뿪��������!\n";
						dsd.insertString(dsd.getLength(),ss,sa);
						allUserCount-=1;
						allUserLab.setText("��ǰ���������û�  "+allUserCount+"    ");
					}
					if(dp.optype==4) //���û���½
					{
						dl.addElement((String)user);
						dcdmAllU.addElement((String)user);
						String ss="�����桿 "+user+"���뱾������!"+"\n";
						dsd.insertString(dsd.getLength(),ss,sa);
						allUserCount+=1;
						allUserLab.setText("��ǰ���������û�  "+allUserCount+"    ");
					}

					if(dp.optype==5) //ϵͳ�㲥��Ϣ
					{
						String ss="�����桿 "+con+"\n";
						dsd.insertString(dsd.getLength(),ss,sa);
					}
				}catch(Exception err)
				{
					String ss="�Ѿ��ͷ������Ͽ�����!\n";
					try
					{
						dsd.insertString(dsd.getLength(),ss,sa);
						allUserLab.setText("��ǰ���������û�  0    ");
						dl.removeAllElements();
						dcdmAllU.removeAllElements();
						userLab.setText("��ǰ�û�");
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
