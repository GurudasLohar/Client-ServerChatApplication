import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class ClientThread implements Runnable
{
Thread 		t;
ClientFrame	parent;
boolean		loop;

	public void startThread(){
		loop=true;
		t=new Thread(this);
		t.start();
	}

	public void stopThread(){
		loop=false;
	}

	public void run(){
		try{
			String s;
			while(loop){
				s=(String) parent.ois.readObject();
				parent.lst.add("Server > " + s);
			}
		}catch(Exception e){
			//parent.closeConnection();
			System.out.println("Logging out");
			System.exit(0);
		}	
	}
}

class ClientFrame extends Frame implements ActionListener{

TextField	txt;
Button	cmd;
List	lst;


Socket		soc;
ObjectOutputStream	oos;
ObjectInputStream	ois;
ClientThread	ct;

	public ClientFrame(){

	super("Client Frame");

	Font f=new Font("Arial",Font.BOLD,14);
	setLayout(null);

	txt=new TextField();
	txt.setBounds(50,50,200,25);
	add(txt);
	txt.setFont(f);

	cmd=new Button("Send");
	cmd.setBounds(50,100,100,25);
	add(cmd);
	cmd.setFont(f);
	cmd.addActionListener(this);

	lst=new List();
	lst.setBounds(50,150,200,100);
	add(lst);
	lst.setFont(f);

	setSize(300,300);
	setVisible(true);
	
	addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent we){
			//closeConnection();
			System.exit(0);
		}
	});

	initializeConnection();
}

public void initializeConnection()
{
	try{
		soc=new Socket("127.0.0.1",5000);
		oos=new ObjectOutputStream(soc.getOutputStream());
		ois=new ObjectInputStream(soc.getInputStream());

		ct=new ClientThread();
		ct.parent=this;
		ct.startThread();
	}catch(Exception e){
		System.out.println(e);
	}	
}


public void actionPerformed(ActionEvent ae)
{
	if(ae.getSource()==cmd){
		try{
			String s=txt.getText();
			oos.writeObject(s);
			lst.add("Client > " + s);

		}catch(Exception e){
			System.out.println(e);
		}	
	}
}
}

class ClientExample{

	public static void main(String cp[]){
		new ClientFrame();
	}
}