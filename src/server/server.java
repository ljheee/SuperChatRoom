package server;

//�������˴���
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
	//��ں���
	public static void main(String args[])
	{
		//�������巽��
		SetFont.setFont(new Font("����", 0, 12));
		new server();
	}
	private JButton []btn=new JButton[3];
	private String  []btnStr={"ɾ���û�","��������","�㲥ϵͳ��Ϣ"};
	private JTextField ServerMessageTxt=new JTextField();
	private JLabel messagelab=new JLabel("ϵͳ��Ϣ:");
	private JList listuser=null;
	private DefaultListModel dluser=new DefaultListModel();
	private JList listop=null;
	private DefaultListModel dlop=new DefaultListModel();

	//����
	private Hashtable all=new Hashtable();
	private Vector names=new Vector();
	ServerSocket ss=null;
	Socket client=null;

	//�����ַ����Ķ���
	public String name=null;
	public String delname=null;

	//������
	public server()
	{
		//��������
		setSize(700,500);
		setTitle("������");
		Container con=getContentPane();

		//����
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

		//����
		try
		{
			ss=new ServerSocket(9000);
			AllDeal ad=new AllDeal();
			ad.start();
			setVisible(true);
		}
		catch(Exception err)
		{
			//��ʵ�����˳�
			System.exit(0);
		}

		//������¼�
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				closeSocket();
				System.exit(0);
			}
		});
	}
	//�رշ�����
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

	//���淽��
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
	//�鿴������
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

	//ɾ���û�����
	public void del(String tmp)
	{
		ClinetDeal cl=(ClinetDeal)all.get(tmp);
		try
		{
			dlop.addElement(tmp+"�û���ɾ��!");
			cl.oos.close();
			cl.ois.close();
			cl.interrupt();
		}
		catch(IOException err)
		{
			System.out.println("err1:"+err);
		}
	}

	//��ť�¼��Ĵ���
	class btnAct implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			//ɾ���û�
			if(e.getSource()==btn[0])
			{
				//�õ�Ҫɾ�����û�����
				delname = JOptionPane.showInputDialog(null,"����Ҫɾ�����û���","�����û���",JOptionPane.OK_CANCEL_OPTION);
				/* ���û���ѡ��������д���
				 * Ϊnull�Ļ����û�ѡ��ȡ����ť����������
				 * Ϊ""�յĻ����û�û���������ݣ���ʾ��������
				 */
				if(delname==null)
				{}
				else if(delname.equals(""))
				{
					JOptionPane.showMessageDialog(server.this,"  �û���������Ϊ��!\n  ����������");
				}
				else
				{
					/* ������������ò鿴�����������Ƿ��и��û�
					 * Ϊnull�Ļ�����ʾû�и��û�������ʾ�û�������
					 * ���򣬽���ɾ��
					 */
					String tmp=chakan(delname);
					if(tmp==null)
					{
						JOptionPane.showMessageDialog(server.this,"���û������ڣ�\n����������!");
					}
					else
					{
						//��󵯳��Ի��򣬽������ȷ��
						int  incept=JOptionPane.showConfirmDialog(server.this,"ȷ��ɾ�� "+delname+" �û���","ɾ��",JOptionPane.YES_NO_OPTION);
						if(incept==0)
						{
							//����ɾ������
							del(tmp);

							JOptionPane.showMessageDialog(server.this,delname+" �û��Ѿ���ɾ��!");

							//��list��ɾ�����û�
							dluser.removeElement(delname);
							all.remove(delname);
							names.remove(delname);

							//���ù��淽�����������û����͸��û��Ѿ��뿪������
							gonggao(3,delname);
						}
					}
				}
			}
			//���û�����������Ϣ
			if(e.getSource()==btn[1])
			{
				String tmpname = JOptionPane.showInputDialog(null,"����Ҫ������û���","�����û���",JOptionPane.OK_CANCEL_OPTION);
				if(tmpname==null)
				{}
				else if(tmpname.equals(""))
				{
					JOptionPane.showMessageDialog(server.this,"  �û���������Ϊ��!\n  ����������");
				}
				else
				{
					String txt = JOptionPane.showInputDialog(null,"����Ҫ���滰","����",JOptionPane.OK_CANCEL_OPTION);
					if(txt==null)
					{}
					else if(txt.equals(""))
					{
						JOptionPane.showMessageDialog(server.this,"  ���滰�ﲻ��Ϊ��!\n  ����������");
					}
					else
					{
						String tmp=chakan(tmpname);
						if(tmp!=null)
						{
							ClinetDeal cl=(ClinetDeal)all.get(tmp);
							DataPack dp1=new DataPack();
							dp1.optype=5;
							dp1.txt="******ϵͳ����:"+txt;
							try
							{
								cl.oos.writeObject(dp1);
								cl.oos.flush();
								JOptionPane.showMessageDialog(server.this,"�Ѿ���"+tmpname+" �û�����������Ϣ!");
							}
							catch(IOException err)
							{
								System.out.println("err2:"+err);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(server.this,"���û������ڣ�\n����������!");
						}
					}
				}
			}
			//�㲥ϵͳ��Ϣ
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
	//�������пͻ��ĽӴ�
	class AllDeal extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					//�������ȴ��û�������
					client=ss.accept();
					//�������������ϵ��û���IP�Ͷ˿ں���LIST����ʾ
					dlop.addElement(client.getInetAddress ()+":  "+client.getPort() +"����");
					//ÿ������һ��������һ���߳�,��������
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

	//����ÿһ�������ϵĿͻ�
	class ClinetDeal extends Thread
	{
		public ObjectOutputStream oos=null;
		public ObjectInputStream ois=null;

		//�������ݰ���
		DataPack dp=null;
		public ClinetDeal(Socket sc)
		{
			try
			{
				oos=new ObjectOutputStream(sc.getOutputStream());
				ois=new ObjectInputStream(sc.getInputStream());
				//ȡ���������û�������
				name=getLogName();
				//LIST��� ���û����Ѿ���½
				dlop.addElement(client.getInetAddress ()+":  "+client.getPort() +"��½");
				//�Ѹ��û���������ӵ��û������б��
				dluser.addElement(name);
				//�Ѹ��û���ӵ�����Vector�
				names.add(name);

				//�������û������½������û�
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
				//����Hashtable��
				all.put(name,this);

				//�����û������е�����
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
		private String getLogName() //�õ��ͻ��˵�����
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
			//���������ݵ�ת��
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

						if(si)  //��˽�ĵ����
						{
							String to=dp.to;
							String from=dp.from;
							String tmp1=chakan(from);
							String tmp=chakan(to);
							//�ֱ�����շ��ͷ��ͷ�����
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
						else  //����˽�ĵ����
						{
							//�㲥
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
						dlop.addElement(exitname+"�뿪������!");
						dluser.removeElement(exitname);
						names.remove(exitname);
						all.remove(exitname);
						//���ù��淽�����������û����͸��û��Ѿ��뿪������
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