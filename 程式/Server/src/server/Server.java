package server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.InputStream;
import java.io.OutputStream;



	class clientdata extends Thread{
	ServerSocket sock;
	byte[]  read=new byte[10];
	String name;
	String[] name1={"abcdefg","aaaaa","abca"};
	byte[] sendpass;		
	String[] password1={"123456","12345","1234"};
	String wrongpassword="00000";
	int a=0;
	Data counted=null;
	public clientdata(){
	try{sock = new ServerSocket(1237);}catch(Exception e){System.out.println("Error:"+e);};}	
	public void run(){				
		try{
			while(true){
			Socket s=sock.accept();
			InputStream getclientname=s.getInputStream();
			OutputStream outclientpassword=s.getOutputStream();
			getclientname.read(read);
			name=new String(read,"GBK");
			sendpass=new byte[10];
			System.out.println(name+"連到請求身分確認");
			
			while(true){
				if(a>=3)break;
				else if(name.compareTo(name1[a])>0) break;
				//counted.add(1);
				a++;}		
			if(name.compareTo(name1[a])==0)
			{		
				
				System.arraycopy(password1[a].getBytes("GBK"), 0, sendpass, 0, password1[a].length());
				outclientpassword.write(sendpass);
				s.close();
				System.out.println("送出密碼"+password1[a]);
			}
			else
			{
			System.arraycopy(wrongpassword.getBytes("GBK"), 0, sendpass, 0, wrongpassword.length());
			outclientpassword.write(sendpass);
			System.out.println("送出錯誤密碼"+wrongpassword);}
			a=0;
			//counted.clear(1);
			s.close();}	
		}catch(Exception e){System.out.println("Error:"+e);}
	}
	private void toCharArray() {
		// TODO Auto-generated method stub
		
	}
}
	
public class Server{
	 public static void main (String[] args){	 
	Thread a=new clientdata();
	a.start();
	}
	}

	

