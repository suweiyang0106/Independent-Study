package storeorder;
import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.DataInputStream;
	import java.io.DataOutputStream;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.OutputStreamWriter;
	import java.io.PrintStream;
	import java.net.InetAddress;
	import java.net.ServerSocket;
	import java.net.Socket;
	import java.text.SimpleDateFormat;
	import java.util.ArrayList;
	import java.util.Date;

	import javax.tools.JavaFileObject;

	import db.Checkcustomer;
	import db.jdbcmysql;
	import timeruse.Timermanage;
public class Server {			
		static String msg;
		static String selectstr;
		static String[] selectsendstr=new String[10];//回傳給客戶的數值		
		static int[] storeorderassigment=new int[10];//查詢完成餐點好去跟顧客餐點做匹配
		static String[] order={"order1","order2","order3","order4","order5","order6","order7","order8","order9","order10",};
		static String[] number={"number1","number2","number3","number4","number5","number6","number7","number8","number9","number10"};//顧客建表後server查詢用
		 static String[] complete={"complete1","complete2","complete3","complete4","complete5","complete6","complete7","complete8","complete9","complete10"};//客戶建表server查詢用
		static Thread thread1=new Thread(new Runnable(){//接受點餐資訊並投入安排		
			public void run(){			
			try{			
				Arrange arg=new Arrange();			
				ServerSocket server=new ServerSocket(5051);
				System.out.println("伺服器啟動於: "+InetAddress.getLocalHost().getHostAddress()+"."+server.getLocalPort());
				Timermanage timerDemo = new Timermanage();
	 			Arrange timer=new Arrange();
	 			Arrange check=new Arrange();
				while(true){
								Socket s=server.accept();
								DataInputStream in=new DataInputStream(s.getInputStream());
								 msg=in.readUTF();							
								System.out.println("來自"+s.getInetAddress()+"接收到的訊息: "+msg);
								arg.Arrange(msg);								
						/*		if(timer.timeradd1())//thread4
					 				timerDemo.ordersendtimer();//thread4	
								if(check.check()>0)//thread2
								 {
									check.send();
									 System.out.println("已送出");
								 }		//thread2	*/							
							}
				}catch(Exception e){System.out.println("Error"+e);}	
			}
		});
		 static Thread thread2=new Thread(new Runnable(){//將餐點送出(10各為一個單位送出,不滿十個優先權大3送出)
			 public void run(){
				 try {			 
				Arrange check=new Arrange();	
			//	Timermanage timerDemo = new Timermanage();//thread4
	 		//	Arrange timer=new Arrange();//thread4
				while(true)
					 { 		
						 if(check.check()>0)
						 {
							 check.send();
							 System.out.println("已送出");
						 }		
					//	 if(timer.timeradd1())//thread4
				 	//			timerDemo.ordersendtimer();//thread4	
						 thread2.yield();
					 }
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });		
		 static Thread thread4=new Thread(new Runnable(){//thread4時間到了送至資料庫
			 	public void run(){
			 		try{
			 			Timermanage timerDemo = new Timermanage();
			 			Arrange timer=new Arrange();
			 			while(true)
			 			{	if(timer.timeradd1())
			 				timerDemo.ordersendtimer();		 				
			 			}
			 		}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });
		 
		 static Thread thread5=new Thread(new Runnable(){//store1的店家server(當成像水餃店，有可能會同樣菜多數量的菜一起做處理)
			 public void run(){
				 try {			 
					 jdbcmysql sub=new jdbcmysql();
					 Arrange send=new Arrange();
					 Timermanage time=new Timermanage();
					 Checkcustomer check=new Checkcustomer();
					 String[] str={"'Store1:order1'","'Store1:order2'","'Store1:order3'","'Store1:order4'"};
					 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量	
					 int flag=0;//做標記 每五次就零個以上作分配
					while(true)
					 { 	
					int i=0; 
					 store1selecttodo= sub.store1checkorder("Store1");
					 storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量 								
						 if(store1selecttodo[0]>0)//判斷數量大於0的先做
						 {
							time.store1timer(0,store1selecttodo[0],1000);							 				 
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個							 
						 }
						 else if(store1selecttodo[1]>0)
						 {
							time.store1timer(1, store1selecttodo[1], 2000);							
						 }
						 if(store1selecttodo[2]>0)//判斷數量大於0的先做
						 {
							time.store1timer(2,store1selecttodo[2],3000);
							 				 
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個						 
						 }
						 else if(store1selecttodo[3]>0)
						 {
							time.store1timer(3, store1selecttodo[3], 4000);						
						 } 	
					 while(i<4)
					 	{	
						// System.out.println(store1selecttodo[i]);
						/* if(store1selecttodo[i]>0&&i<2)//判斷數量大於0的先做
						 {
							time.store1timer(i,store1selecttodo[i],1000);
							 				 
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個
							 
						 }
						 else if(store1selecttodo[i]>0&&i>1)
						 {
							time.store1timer(i, store1selecttodo[i], 2000);
							
						}*/						
				if(storeorderassigment[i]>0)
						 {						 
							 System.out.println("店家做餐點order"+i+" 剩下:"+storeorderassigment[i]);//開始作分配!!!!!!!!!						 
							 for(int k=0;k<10;k++){
								storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量  
								 //order1到order10							
								check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store1ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
									
							 }					 
						 }			 					 
						 i++;
					 	}	
					 thread5.sleep(100);
					 }					
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });
		 static Thread thread6=new Thread(new Runnable(){//store2的店家server(當成動態郎或便當店有自己的一套sop，做起來像一個人處理)
			 public void run(){
				 try {			 
					 jdbcmysql sub=new jdbcmysql();
					 Arrange send=new Arrange();
					 Timermanage time=new Timermanage();
					 Checkcustomer check=new Checkcustomer();
					 String[] str={"'Store2:order1'","'Store2:order2'","'Store2:order3'","'Store2:order4'"};
					 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量
					 while(true)
					 { 	
					int i=0;			
					 store1selecttodo=sub.store1checkorder("Store2");
					 storeorderassigment=sub.storecheckcomp("store2ordercomplete"); //得到店家做完成數量 
					 while(i<4)
					 	{
						// System.out.println(store1selecttodo[i]);
						 if(store1selecttodo[i]>0)//判斷數量大於20的先做
						 {
							 time.store2timer(i,store1selecttodo[i]);
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個
						 }
						 if(storeorderassigment[i]>0)
						 {						 
							// System.out.println("店家做餐點order"+i+" 剩下:"+storeorderassigment[i]);//開始作分配!!!!!!!!!
							 for(int k=0;k<10;k++){							
								 //order1到order10	
								//System.out.println(i+"and"+storeorderassigment[i]);	
								  storeorderassigment=sub.storecheckcomp("store2ordercomplete"); //得到店家做完成數量  
								//  System.out.println(str[i]+order[k]+number[k]+complete[k]+storeorderassigment[i]+"  store2ordercomplete");
								check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store2ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上	
							 }					 
						 }
						 i++;
					 	}
					 thread6.sleep(100);
					 }
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });
		 static Thread thread7=new Thread(new Runnable(){//store3的店家server
			 public void run(){
				 try {			 
					 jdbcmysql sub=new jdbcmysql();
					 Arrange send=new Arrange();
					 Timermanage time=new Timermanage();
					 Checkcustomer check=new Checkcustomer();
					 String[] str={"'Store3:order1'","'Store3:order2'","'Store3:order3'","'Store3:order4'"};
					 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量
					 while(true)
					 { 	
					int i=0;	
					 store1selecttodo= sub.store1checkorder("Store3");
					 storeorderassigment=sub.storecheckcomp("store3ordercomplete"); //得到店家做完成數量 
					 while(i<4)
					 	{
						// System.out.println(store1selecttodo[i]);
						 if(store1selecttodo[i]>0)//判斷數量大於20的先做
						 {
							 time.store3timer(i,store1selecttodo[i]);
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個
							 
						 }
						 if(storeorderassigment[i]>0)
						 {						 
							 //System.out.println("店家做餐點order"+i+" 剩下:"+storeorderassigment[i]);//開始作分配!!!!!!!!!						 
							 for(int k=0;k<10;k++){
								storeorderassigment=sub.storecheckcomp("store3ordercomplete"); //得到店家做完成數量  
								 //order1到order10	
								//System.out.println(i+"and"+storeorderassigment[i]);							
								check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store3ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
								}					 
						 }
						 i++;						 
					 	}
					 thread7.sleep(100);
					 }
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });
		 static Thread thread8=new Thread(new Runnable(){//store4的店家server
			 public void run(){
				 try {			 
					 jdbcmysql sub=new jdbcmysql();
					 Arrange send=new Arrange();
					 Timermanage time=new Timermanage();
					 Checkcustomer check=new Checkcustomer();
					 String[] str={"'Store4:order1'","'Store4:order2'","'Store4:order3'","'Store4:order4'"};
					 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量
					 while(true)
					 { 	
					int i=0;				
					 store1selecttodo=sub.store1checkorder("Store4");
					 storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量 
					 while(i<4)
					 	{
						// System.out.println(store1selecttodo[i]);
						 if(store1selecttodo[i]>0)//判斷數量大於20的先做
						 {		
							 time.store4timer(i,store1selecttodo[i]);
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個
						 }
						 if(storeorderassigment[i]>0)
						 {						 
							 //System.out.println("店家做餐點order"+i+" 剩下:"+storeorderassigment[i]);//開始作分配!!!!!!!!!						 
							 for(int k=0;k<10;k++){
								storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
								 //order1到order10	
								//System.out.println(i+"and"+storeorderassigment[i]);							
								check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
								}					 
						 }
						 i++;
					 	}
					thread8.sleep(100);
					 }
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 }); 
		 static Thread thread9=new Thread(new Runnable(){//store1餐點1和2依序坐但同時3和4也在座(新計時器)
			 public void run(){
				 try {			 
					 jdbcmysql sub=new jdbcmysql();
					 Arrange send=new Arrange();
					 Timermanage time=new Timermanage();
					 Checkcustomer check=new Checkcustomer();
					 String[] str={"'Store1  -  : order1'","'Store1  -  : order2'","'Store1  -  : order3'","'Store1  -  : order4'"};
					 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量				 
					while(true)
					 { 	
					
					 store1selecttodo= sub.store1checkorder("Store1");
					 storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量 					
						 if(store1selecttodo[0]>0)//判斷數量大於0的先做
						 {
							time.store1timer(0,store1selecttodo[0],1000);							 				 
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個
							 
						 }
						 else if(store1selecttodo[1]>0)
						 {
							time.store1timer(1, store1selecttodo[1], 2000);
							
						 }
					}					 
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });
		 static Thread thread10=new Thread(new Runnable(){//store1餐點3和4依序坐但同時1和2也在座(新計時器)
			 public void run(){
				 try {			 
					 jdbcmysql sub=new jdbcmysql();
					 Arrange send=new Arrange();
					 Timermanage time=new Timermanage();
					 Checkcustomer check=new Checkcustomer();
					 String[] str={"'Store1  -  : order1'","'Store1  -  : order2'","'Store1  -  : order3'","'Store1  -  : order4'"};
					 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量				 
					while(true)
					 { 	
					
					 store1selecttodo= sub.store1checkorder("Store1");
					 storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量 					
						 if(store1selecttodo[2]>0)//判斷數量大於0的先做
						 {
							time.store1timer(2,store1selecttodo[2],3000);
							 				 
							 //呼叫計時器開始做//計時器則是扣20加20
							 //每隔1分鐘做好20個						 
						 }
						 else if(store1selecttodo[3]>0)
						 {
							time.store1timer(3, store1selecttodo[3], 4000);						
						} 		
					 	}					 
				 	}catch(Exception e){System.out.println("Error"+e);}
			 	}
		 });
	 static Thread threadsample=new Thread(new Runnable(){//store1餐點3和4依序坐但同時1和2也在座(新計時器)
		 public void run(){
			 System.out.println("Here is the starting point of Thread.");
			int i=1;
		        for (;;) { // infinite loop to print message
		        	i++;
		            System.out.println("User Created Thread1    " +i);
		    //    threadsample.yield();
		        }
		 }});
	 static Thread threadsample2=new Thread(new Runnable(){//store1餐點3和4依序坐但同時1和2也在座(新計時器)
		 public void run(){
			 System.out.println("Here is the starting point of Thread.");
			int i=1;
		        for (;;) { // infinite loop to print message
		        	i++;
		            System.out.println("User Created Thread2    " +i);
		      //  threadsample2.yield();
		        }
		 }});
	 static Thread threadsample3=new Thread(new Runnable(){//store1餐點3和4依序坐但同時1和2也在座(新計時器)
		 public void run(){
			 System.out.println("Here is the starting point of Thread.");
			int i=1;
		        for (;;) { // infinite loop to print message
		        	i++;
		            System.out.println("User Created Thread3    " +i);
		      //  threadsample2.yield();
		        }
		 }});
	 static Thread threadsample4=new Thread(new Runnable(){//store1餐點3和4依序坐但同時1和2也在座(新計時器)
		 public void run(){
			 System.out.println("Here is the starting point of Thread.");
			int i=1;
		        for (;;) { // infinite loop to print message
		        	i++;
		            System.out.println("User Created Thread4    " +i);
		      //  threadsample2.yield();
		        }
		 }});
	 static Thread threadsample5=new Thread(new Runnable(){//store1餐點3和4依序坐但同時1和2也在座(新計時器)
		 public void run(){
			 System.out.println("Here is the starting point of Thread.");
			int i=1;
		        for (;;) { // infinite loop to print message
		        	i++;
		            System.out.println("User Created Thread5    " +i);
		      //  threadsample2.yield();
		        }
		 }});
		 public static void main(String[] args)
		{	//jdbcmysql test = new jdbcmysql();
			//test.update("Store2","order1",20);
			//test.startover();
			thread1.start();
			thread2.start();			
			thread4.start();
			thread5.start();
			thread6.start();
			thread7.start();
			thread8.start();
	//	thread9.start();
		//thread10.start();
	/*		 threadsample.start();
			 threadsample2.start();
			 threadsample3.start();
			 threadsample4.start();
			 threadsample5.start();*/
		//	 int j=1;
		/*	 try {
				 threadsample.join(); // 等待t1結束
				 threadsample2.join(); // 等待t2結束
		        } catch (InterruptedException e) {}
		        for (int i=0;i < 5; i++) {
		            System.out.println("Main Thread     "+i);
		        }*/
		}    
	

}
