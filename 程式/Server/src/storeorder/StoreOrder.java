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
public class StoreOrder {		
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
						time.store1timer(0,store1selecttodo[0],2000);							 				 
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
			if(storeorderassigment[i]>0)
					 {						 
						 System.out.println("店家做餐點order"+i+1+" 完成:"+storeorderassigment[i]);//開始作分配!!!!!!!!!						 
						 for(int k=0;k<6;k++){
							storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量  
							 //order1到order10							
							check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store1ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
								//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
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
				 int flag=0;//每五次玲個以上作分配一次
				 while(true)
				 { 	
				int i=0;				
				 store1selecttodo=sub.store1checkorder("Store4");
				 storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量 
				 if(flag>=6)flag=0;
				 while(i<4)
				 	{
					// System.out.println(store1selecttodo[i]);
					 if(store1selecttodo[i]>0)//判斷數量大於20的先做
					 {		
						 time.store4timer(i,store1selecttodo[i]);
						 //呼叫計時器開始做//計時器則是扣20加20
						 //每隔1分鐘做好20個
					 }
					 if(storeorderassigment[i]>5&&flag<5)//都是由五個以上才做分配，每五次會有一次是玲個以上就做分配
					 {						 
						 //System.out.println("店家做餐點order"+i+" 剩下:"+storeorderassigment[i]);//開始作分配!!!!!!!!!						 
						 for(int k=0;k<10;k++){
							storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
							 //order1到order10	
							//System.out.println(i+"and"+storeorderassigment[i]);							
							check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
							}						 
					 }
					 else if(storeorderassigment[i]>0&&flag>=5)
					 {
						 for(int k=0;k<10;k++){
								storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
								 //order1到order10	
								//System.out.println(i+"and"+storeorderassigment[i]);							
								check.selectnum(str[i],order[k],number[k],complete[k],storeorderassigment[i]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
								}	
					 }
					
					 i++;					 					 
				 	}
				 flag++;
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
 static Thread threadtest1=new Thread(new Runnable(){//store1的店家server(當成像水餃店，有可能會同樣菜多數量的菜一起做處理)
	 public void run(){
		 try {			 
			 jdbcmysql sub=new jdbcmysql();
			 Arrange send=new Arrange();
			 Timermanage time=new Timermanage();
			 Checkcustomer check=new Checkcustomer();
			 String[] str={"'Store1:order1'","'Store1:order2'","'Store1:order3'","'Store1:order4'"};
			 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量				
			 int[] order_fix=new int[4];//每樣菜有無卡住
			int[] order_number_fix=new int[4];//每樣蔡固定數量不同
			 int[] order_fix_position=new int[4];//每樣菜卡在不同位置
			while(true)
			 { 				
			 store1selecttodo= sub.store1checkorder("Store1");
			 storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量 								
				 if(store1selecttodo[0]>0)//判斷數量大於0的先做
				 {
					time.store1timer(0,store1selecttodo[0],2000);							 				 
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
		if(storeorderassigment[0]>0)//一號餐分配
				 {						 
					 System.out.println("店家做餐點order1 "+" 完成:"+storeorderassigment[0]);//開始作分配!!!!!!!!!						 
					 for(int k=order_fix_position[0];k<6;k++){
						 if(order_fix[0]==0)
						 {
						storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量  
						 //order1到order10							
						order_number_fix[0]=check.selectnum(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store1ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
							//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
						 	if(order_number_fix[0]>0)
						 			{
						 				order_fix_position[0]=k;//標記卡在哪一個column
						 				order_fix[0]=1;//標記卡住
						 				System.out.print("有客戶被跳過 已鎖定");
						 				break;
						 			}
						 	if(order_number_fix[0]==0&&order_fix_position[0]==5)
						 	{
						 		order_fix_position[0]=0;
						 	}
						 }
						 else
						 {
							 if(storeorderassigment[0]>=order_number_fix[0])
							 {
								 check.selectnum2(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store1ordercomplete ");
								 order_fix[0]=0;
								 order_number_fix[0]=0;
								 System.out.println("該客戶已拿到餐點");
							 }
							 else
							 {
								 System.out.println("須等到該客戶拿到餐點才會繼續下去");
								 break;
							 }
						 }						 
					}		
			 	}
		if(storeorderassigment[1]>0)//二號餐分配
		 {						 
			 System.out.println("店家做餐點order2 "+" 完成:"+storeorderassigment[1]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[1];k<6;k++){
				 if(order_fix[1]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[1]=check.selectnum(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store1ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[1]>0)
				 			{
				 				order_fix_position[1]=k;//標記卡在哪一個column
				 				order_fix[1]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[1]==0&&order_fix_position[1]==5)
				 	{
				 		order_fix_position[1]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[1]>=order_number_fix[1])
					 {
						 check.selectnum2(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store1ordercomplete ");
						 order_fix[1]=0;
						 order_number_fix[1]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}
		if(storeorderassigment[2]>0)//三號餐分配
		 {						 
			 System.out.println("店家做餐點order3 "+" 完成:"+storeorderassigment[2]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[2];k<6;k++){
				 if(order_fix[2]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[2]=check.selectnum(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store1ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[2]>0)
				 			{
				 				order_fix_position[2]=k;//標記卡在哪一個column
				 				order_fix[2]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[2]==0&&order_fix_position[2]==5)
				 	{
				 		order_fix_position[2]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[2]>=order_number_fix[2])
					 {
						 check.selectnum2(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store1ordercomplete ");
						 order_fix[2]=0;
						 order_number_fix[2]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}			
	 	}	
		if(storeorderassigment[3]>0)//四號餐分配
		 {						 
			 System.out.println("店家做餐點order4 "+" 完成:"+storeorderassigment[3]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[3];k<6;k++){
				 if(order_fix[3]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store1ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[3]=check.selectnum(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store1ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[3]>0)
				 			{
				 				order_fix_position[3]=k;//標記卡在哪一個column
				 				order_fix[3]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[3]==0&&order_fix_position[3]==5)
				 	{
				 		order_fix_position[3]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[3]>=order_number_fix[3])
					 {
						 check.selectnum2(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store1ordercomplete ");
						 order_fix[3]=0;
						 order_number_fix[3]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}	
			 threadtest1.sleep(100);
			 }					
		 	}catch(Exception e){System.out.println("Error"+e);}
	 	}
 }
  );
 static Thread threadtest2=new Thread(new Runnable(){//store2的店家servertest
	 public void run(){
		 try {			 
			 jdbcmysql sub=new jdbcmysql();
			 Arrange send=new Arrange();
			 Timermanage time=new Timermanage();
			 Checkcustomer check=new Checkcustomer();
			 String[] str={"'Store2:order1'","'Store2:order2'","'Store2:order3'","'Store2:order4'"};
			 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量				
			 int[] order_fix=new int[4];//每樣菜有無卡住
			int[] order_number_fix=new int[4];//每樣蔡固定數量不同
			 int[] order_fix_position=new int[4];//每樣菜卡在不同位置
			
			while(true)
			 { 	
				int i=0;			
			 store1selecttodo= sub.store1checkorder("Store2");
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
				 i++;
			 	}
			 
		if(storeorderassigment[0]>0)//一號餐分配
				 {						 
					 System.out.println("店家2做餐點order1 "+" 完成:"+storeorderassigment[0]);//開始作分配!!!!!!!!!						 
					 for(int k=order_fix_position[0];k<6;k++){
						 if(order_fix[0]==0)
						 {
						storeorderassigment=sub.storecheckcomp("store2ordercomplete"); //得到店家做完成數量  
						 //order1到order10							
						order_number_fix[0]=check.selectnum(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store2ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
							//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
						 	if(order_number_fix[0]>0)
						 			{
						 				order_fix_position[0]=k;//標記卡在哪一個column
						 				order_fix[0]=1;//標記卡住
						 				System.out.print("有客戶被跳過 已鎖定");
						 				break;
						 			}
						 	if(order_number_fix[0]==0&&order_fix_position[0]==5)
						 	{
						 		order_fix_position[0]=0;
						 	}
						 }
						 else
						 {
							 if(storeorderassigment[0]>=order_number_fix[0])
							 {
								 check.selectnum2(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store2ordercomplete ");
								 order_fix[0]=0;
								 order_number_fix[0]=0;
								 System.out.println("該客戶已拿到餐點");
							 }
							 else
							 {
								 System.out.println("須等到該客戶拿到餐點才會繼續下去");
								 break;
							 }
						 }						 
					}		
			 	}
		if(storeorderassigment[1]>0)//二號餐分配
		 {						 
			 System.out.println("店家2做餐點order2 "+" 完成:"+storeorderassigment[1]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[1];k<6;k++){
				 if(order_fix[1]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store2ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[1]=check.selectnum(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store2ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[1]>0)
				 			{
				 				order_fix_position[1]=k;//標記卡在哪一個column
				 				order_fix[1]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[1]==0&&order_fix_position[1]==5)
				 	{
				 		order_fix_position[1]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[1]>=order_number_fix[1])
					 {
						 check.selectnum2(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store2ordercomplete ");
						 order_fix[1]=0;
						 order_number_fix[1]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}
		if(storeorderassigment[2]>0)//三號餐分配
		 {						 
			 System.out.println("店家2做餐點order3 "+" 完成:"+storeorderassigment[2]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[2];k<6;k++){
				 if(order_fix[2]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store2ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[2]=check.selectnum(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store2ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[2]>0)
				 			{
				 				order_fix_position[2]=k;//標記卡在哪一個column
				 				order_fix[2]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[2]==0&&order_fix_position[2]==5)
				 	{
				 		order_fix_position[2]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[2]>=order_number_fix[2])
					 {
						 check.selectnum2(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store2ordercomplete ");
						 order_fix[2]=0;
						 order_number_fix[2]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}			
	 	}	
		if(storeorderassigment[3]>0)//四號餐分配
		 {						 
			 System.out.println("店家2做餐點order4 "+" 完成:"+storeorderassigment[3]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[3];k<6;k++){
				 if(order_fix[3]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store2ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[3]=check.selectnum(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store2ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[3]>0)
				 			{
				 				order_fix_position[3]=k;//標記卡在哪一個column
				 				order_fix[3]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[3]==0&&order_fix_position[3]==5)
				 	{
				 		order_fix_position[3]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[3]>=order_number_fix[3])
					 {
						 check.selectnum2(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store2ordercomplete ");
						 order_fix[3]=0;
						 order_number_fix[3]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}	
			 threadtest2.sleep(100);
			 }					
		 	}catch(Exception e){System.out.println("Error"+e);}
	 	}
	 }
	 );
 static Thread threadtest3=new Thread(new Runnable(){//store3的店家servertest
	 public void run(){
		 try {			 
			 jdbcmysql sub=new jdbcmysql();
			 Arrange send=new Arrange();
			 Timermanage time=new Timermanage();
			 Checkcustomer check=new Checkcustomer();
			 String[] str={"'Store3:order1'","'Store3:order2'","'Store3:order3'","'Store3:order4'"};
			 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量				
			 int[] order_fix=new int[4];//每樣菜有無卡住
			int[] order_number_fix=new int[4];//每樣蔡固定數量不同
			 int[] order_fix_position=new int[4];//每樣菜卡在不同位置
			
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
				 i++;
			 	}
			 
		if(storeorderassigment[0]>0)//一號餐分配
				 {						 
					 System.out.println("店家2做餐點order1 "+" 完成:"+storeorderassigment[0]);//開始作分配!!!!!!!!!						 
					 for(int k=order_fix_position[0];k<6;k++){
						 if(order_fix[0]==0)
						 {
						storeorderassigment=sub.storecheckcomp("store3ordercomplete"); //得到店家做完成數量  
						 //order1到order10							
						order_number_fix[0]=check.selectnum(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store3ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
							//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
						 	if(order_number_fix[0]>0)
						 			{
						 				order_fix_position[0]=k;//標記卡在哪一個column
						 				order_fix[0]=1;//標記卡住
						 				System.out.print("有客戶被跳過 已鎖定");
						 				break;
						 			}
						 	if(order_number_fix[0]==0&&order_fix_position[0]==5)
						 	{
						 		order_fix_position[0]=0;
						 	}
						 }
						 else
						 {
							 if(storeorderassigment[0]>=order_number_fix[0])
							 {
								 check.selectnum2(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store3ordercomplete ");
								 order_fix[0]=0;
								 order_number_fix[0]=0;
								 System.out.println("該客戶已拿到餐點");
							 }
							 else
							 {
								 System.out.println("須等到該客戶拿到餐點才會繼續下去");
								 break;
							 }
						 }						 
					}		
			 	}
		if(storeorderassigment[1]>0)//二號餐分配
		 {						 
			 System.out.println("店家2做餐點order2 "+" 完成:"+storeorderassigment[1]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[1];k<6;k++){
				 if(order_fix[1]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store3ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[1]=check.selectnum(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store3ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[1]>0)
				 			{
				 				order_fix_position[1]=k;//標記卡在哪一個column
				 				order_fix[1]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[1]==0&&order_fix_position[1]==5)
				 	{
				 		order_fix_position[1]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[1]>=order_number_fix[1])
					 {
						 check.selectnum2(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store3ordercomplete ");
						 order_fix[1]=0;
						 order_number_fix[1]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}
		if(storeorderassigment[2]>0)//三號餐分配
		 {						 
			 System.out.println("店家2做餐點order3 "+" 完成:"+storeorderassigment[2]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[2];k<6;k++){
				 if(order_fix[2]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store3ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[2]=check.selectnum(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store3ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[2]>0)
				 			{
				 				order_fix_position[2]=k;//標記卡在哪一個column
				 				order_fix[2]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[2]==0&&order_fix_position[2]==5)
				 	{
				 		order_fix_position[2]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[2]>=order_number_fix[2])
					 {
						 check.selectnum2(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store3ordercomplete ");
						 order_fix[2]=0;
						 order_number_fix[2]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}			
	 	}	
		if(storeorderassigment[3]>0)//四號餐分配
		 {						 
			 System.out.println("店家2做餐點order4 "+" 完成:"+storeorderassigment[3]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[3];k<6;k++){
				 if(order_fix[3]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store3ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[3]=check.selectnum(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store3ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[3]>0)
				 			{
				 				order_fix_position[3]=k;//標記卡在哪一個column
				 				order_fix[3]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[3]==0&&order_fix_position[3]==5)
				 	{
				 		order_fix_position[3]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[3]>=order_number_fix[3])
					 {
						 check.selectnum2(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store3ordercomplete ");
						 order_fix[3]=0;
						 order_number_fix[3]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}	
			 threadtest3.sleep(100);
			 }					
		 	}catch(Exception e){System.out.println("Error"+e);}
	 	}
	 }
	 );
 static Thread threadtest4=new Thread(new Runnable(){//store2的店家servertest
	 public void run(){
		 try {			 
			 jdbcmysql sub=new jdbcmysql();
			 Arrange send=new Arrange();
			 Timermanage time=new Timermanage();
			 Checkcustomer check=new Checkcustomer();
			 String[] str={"'Store4:order1'","'Store4:order2'","'Store4:order3'","'Store4:order4'"};
			 int[] store1selecttodo=new int[10];//店家欲查詢點餐的數量				
			 int[] order_fix=new int[4];//每樣菜有無卡住
			int[] order_number_fix=new int[4];//每樣蔡固定數量不同
			 int[] order_fix_position=new int[4];//每樣菜卡在不同位置
			
			while(true)
			 { 	
				int i=0;			
			 store1selecttodo= sub.store1checkorder("Store4");
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
				 i++;
			 	}
			 
		if(storeorderassigment[0]>0)//一號餐分配
				 {						 
					 System.out.println("店家4做餐點order1 "+" 完成:"+storeorderassigment[0]);//開始作分配!!!!!!!!!						 
					 for(int k=order_fix_position[0];k<6;k++){
						 if(order_fix[0]==0)
						 {
						storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
						 //order1到order10							
						order_number_fix[0]=check.selectnum(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
							//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
						 	if(order_number_fix[0]>0)
						 			{
						 				order_fix_position[0]=k;//標記卡在哪一個column
						 				order_fix[0]=1;//標記卡住
						 				System.out.print("有客戶被跳過 已鎖定");
						 				break;
						 			}
						 	if(order_number_fix[0]==0&&order_fix_position[0]==5)
						 	{
						 		order_fix_position[0]=0;
						 	}
						 }
						 else
						 {
							 if(storeorderassigment[0]>=order_number_fix[0])
							 {
								 check.selectnum2(str[0],order[k],number[k],complete[k],storeorderassigment[0]," store4ordercomplete ");
								 order_fix[0]=0;
								 order_number_fix[0]=0;
								 System.out.println("該客戶已拿到餐點");
							 }
							 else
							 {
								 System.out.println("須等到該客戶拿到餐點才會繼續下去");
								 break;
							 }
						 }						 
					}		
			 	}
		if(storeorderassigment[1]>0)//二號餐分配
		 {						 
			 System.out.println("店家4做餐點order2 "+" 完成:"+storeorderassigment[1]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[1];k<6;k++){
				 if(order_fix[1]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[1]=check.selectnum(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[1]>0)
				 			{
				 				order_fix_position[1]=k;//標記卡在哪一個column
				 				order_fix[1]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[1]==0&&order_fix_position[1]==5)
				 	{
				 		order_fix_position[1]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[1]>=order_number_fix[1])
					 {
						 check.selectnum2(str[1],order[k],number[k],complete[k],storeorderassigment[1]," store4ordercomplete ");
						 order_fix[1]=0;
						 order_number_fix[1]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}
		if(storeorderassigment[2]>0)//三號餐分配
		 {						 
			 System.out.println("店家4做餐點order3 "+" 完成:"+storeorderassigment[2]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[2];k<6;k++){
				 if(order_fix[2]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[2]=check.selectnum(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[2]>0)
				 			{
				 				order_fix_position[2]=k;//標記卡在哪一個column
				 				order_fix[2]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[2]==0&&order_fix_position[2]==5)
				 	{
				 		order_fix_position[2]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[2]>=order_number_fix[2])
					 {
						 check.selectnum2(str[2],order[k],number[k],complete[k],storeorderassigment[2]," store4ordercomplete ");
						 order_fix[2]=0;
						 order_number_fix[2]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}			
	 	}	
		if(storeorderassigment[3]>0)//四號餐分配
		 {						 
			 System.out.println("店家4做餐點order4 "+" 完成:"+storeorderassigment[3]);//開始作分配!!!!!!!!!						 
			 for(int k=order_fix_position[3];k<6;k++){
				 if(order_fix[3]==0)
				 {
				storeorderassigment=sub.storecheckcomp("store4ordercomplete"); //得到店家做完成數量  
				 //order1到order10							
				order_number_fix[3]=check.selectnum(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store4ordercomplete ");//把完成的餐點(store1ordercomplete)扣掉加到客戶建表單上
					//str store:1:order1 order order1 number 數量 complete待完成數量   storeorderassigment完成的數量
				 	if(order_number_fix[3]>0)
				 			{
				 				order_fix_position[3]=k;//標記卡在哪一個column
				 				order_fix[3]=1;//標記卡住
				 				System.out.print("有客戶被跳過 已鎖定");
				 				break;
				 			}
				 	if(order_number_fix[3]==0&&order_fix_position[3]==5)
				 	{
				 		order_fix_position[3]=0;
				 	}
				 }
				 else
				 {
					 if(storeorderassigment[3]>=order_number_fix[3])
					 {
						 check.selectnum2(str[3],order[k],number[k],complete[k],storeorderassigment[3]," store4ordercomplete ");
						 order_fix[3]=0;
						 order_number_fix[3]=0;
						 System.out.println("該客戶已拿到餐點");
					 }
					 else
					 {
						 System.out.println("須等到該客戶拿到餐點才會繼續下去");
						 break;
					 }
				 }						 
			}		
	 	}	
			 threadtest4.sleep(100);
			 }					
		 	}catch(Exception e){System.out.println("Error"+e);}
	 	}
	 }
	 );
	 public static void main(String[] args)
	{	//jdbcmysql test = new jdbcmysql();
		//test.update("Store2","order1",20);
		//test.startover();
		thread1.start();
		thread2.start();			
		thread4.start();
	//	thread5.start();
	//	thread6.start();
		//thread7.start();
		//thread8.start();
		threadtest1.start();
		threadtest2.start();
		threadtest3.start();
		threadtest4.start();

	}  
}  


