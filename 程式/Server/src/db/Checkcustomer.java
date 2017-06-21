package db;

import java.awt.Cursor;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement;

import storeorder.Arrange;
public class Checkcustomer {//客戶等待分配
	jdbcmysql sub=new jdbcmysql();
	
	 private Connection con = null; //Database objects 
	 private Connection con1=null;
	  //連接object 
	  private Statement stat = null;
	  private Statement stat1=null;
	  //執行,傳入之sql為完整字串 
	 
	  private ResultSet rs = null; 
	 
	  //結果集 
	  private PreparedStatement pst = null; 
	  //執行,傳入之sql為預儲之字申,需要傳入變數之位置
	  private static String[] test1={"account","order1","number1","complete1"};
	  private static String test="CREATE TABLE customer (" + 
			    "    account     varchar(20) " + 
			    "  , order1    varchar(20) " + 
			    "  , number1  	INTEGER " +
			    "  , complete1		INTEGER	"+
			    "	, order2    varchar(20) " + 
			    "  , number2  	INTEGER " +
			    "  , complete2		INTEGER "+
			    "	, order3    varchar(20) " + 
			    "  , number3  	INTEGER " +
			    "  , complete3		INTEGER "+
			    "	, order4    varchar(20) " + 
			    "  , number4  	INTEGER " +
			    "  , complete4		INTEGER "+
			    "	, order5    varchar(20) " + 
			    "  , number5  	INTEGER " +
			    "  , complete5		INTEGER "+
			    "	, order6    varchar(20) " + 
			    "  , number6  	INTEGER " +
			    "  , complete6		INTEGER "+
			    "	, order7    varchar(20) " + 
			    "  , number7  	INTEGER " +
			    "  , complete7		INTEGER "+
			    ")";
	  
	  private static String insterttest = "insert into customer(account,order1,number1,complete1,order2,number2,complete2,order3"+""
	  		+ ",number3,complete3) " + 
		      "value('abcde','store1_order3',2,0,'store2_order2',3,0,'store1_order4',1,0) ";
	  private static String insterttest1 = "insert into customer("+test1+") " + 
			      "value('abcde','store1_order3',2,0) ";
	  private static String instert="insert into customer("+""+")";//從這邊開始改，客戶端最耗處理過後分們別類，種類跟數量做好
	  public void createTable(String str) 
	  { 
	    try 
	    { 
	      stat = con.createStatement(); 
	      stat.executeUpdate(str); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("CreateDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	    	
	      Close();
	    } 
	  } 
	  public void insertTable(String str) 
	  { //insert into Store1(order1,order2,order3,order4) " + "value(0,0,0,0) "
	    try 
	    { 
	      pst = con.prepareStatement(str);      
	      pst.executeUpdate(); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("InsertDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	/*  public void selectnum(String ord,String order ,String num,String com,int storecom,String subcom){//找尋顧客點餐數量和待完成數量
		Checkcustomer add=new Checkcustomer();
			//  int i=0;														//ord=store1-order1,store-order2...//num=number1,number2...//com=complete1,complete2...				
		      String[] tmp1=new String[10];									//order 是order1-order10
		      String[] tmp2=new String[10];									//storecom是店家完成數量(order1到order4)
		      tmp1=ord.split(":");
		     // System.out.println(tmp1[0]);
		      tmp2=tmp1[1].split("'");			      
		  try 																//subcom是指定哪個完成表單ex.store1ordercomplete
		    {   			 	
			  stat = con.createStatement();			  
			  rs = stat.executeQuery("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);//這裡order1要改(是order1到order10)的ord
			//  System.out.println("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);	
			  while(rs.next()) 
		      { 	System.out.println("正在搜索資料庫做分配.......");	    	  
		    	// System.out.print(rs.getInt(num)+"\t\t"+rs.getInt(com)+"\n");
		    //	 if(rs.getInt(num)>rs.getInt(com)&&storecom>=rs.getInt(num))
		      if(rs.getInt(num)>rs.getInt(com))//有顧客是等待填滿的的
		    	 {		   
		    		 if(storecom>=rs.getInt(num))//待分配完成餐點大於等於該客戶餐點數量
		    		 { sub.sub(subcom, tmp2[0],rs.getInt(num));
		    		//System.out.println(subcom);
		    		add.update("customertable",com,rs.getInt(num),order,ord,rs.getInt("id")); //update 搭配select
		    		 //System.out.println(tmp2[0]+"做好："+storecom+"然後要減掉"+rs.getInt(num));
		    		 //要把扣掉的storecomlete再加回去
		    		 //System.out.println(rs.getInt("id"));		
		    		}
		    		 
		    		 //else if{標記有無走過這裡}{ 紀錄並回傳第一個跳掉的數量 還有做標記只能走過這裡一次}
		    		 
		    		 //else if (完成的餐點變零){就直接回傳零了}
		    	 }
		      }
			  //回傳跳過客戶的餐點數量	若剛好分完就回傳零    
		    } 
		    catch(SQLException e) 
		    { 
		      System.out.println("DropDB Exception :" + e.toString()); 
		   
		    } 
		    finally 
		    { 
		    	
		      Close(); 
		      
		    } 
	  }*/
	  public int selectnum(String ord,String order ,String num,String com,int storecom,String subcom){//找尋顧客點餐數量和待完成數量
			Checkcustomer add=new Checkcustomer();
				//  int i=0;														//ord=store1-order1,store-order2...//num=number1,number2...//com=complete1,complete2...				
			      String[] tmp1=new String[10];									//order 是order1-order10
			      String[] tmp2=new String[10];									//storecom是店家完成數量(order1到order4)
			      tmp1=ord.split(":");
			     // System.out.println(tmp1[0]);
			      tmp2=tmp1[1].split("'");
			      int order_number_fix=0;
			      int flag=0;
			  try 																//subcom是指定哪個完成表單ex.store1ordercomplete
			    {   			 	
				  stat = con.createStatement();			  
				  rs = stat.executeQuery("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);//這裡order1要改(是order1到order10)的ord
				//  System.out.println("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);	
				  while(rs.next()) 
			      { 	System.out.println("正在搜索資料庫做分配.......");	    	
			      if(rs.getInt(num)>rs.getInt(com))//有顧客是等待填滿的的
			    	 {		   
			    		 if(storecom>=rs.getInt(num))//待分配完成餐點大於等於該客戶餐點數量
			    		 { 
			    			 if(storecom-rs.getInt(num)>=0)
			    			 { 			    			 
			    			 			sub.sub(subcom, tmp2[0],rs.getInt(num));			    	
			    			 				add.update("customertable",com,rs.getInt(num),order,ord,rs.getInt("id")); //update 搭配select
			    			 					//System.out.println(tmp2[0]+"做好："+storecom+"然後要減掉"+rs.getInt(num));
			    			 					//要把扣掉的storecomlete再加回去			    		
			    			 					System.out.println(rs.getInt("id"));
			    			 					storecom=storecom-rs.getInt(num);
			    			 }
			    		}
			    		 
			    		else if(flag==0)
			    		{
			    			order_number_fix=rs.getInt(num);//遇到第一個被跳過的客戶記住並回傳他的數量
			    			flag=1;//標記在走完這回圈前都不會再回到這
			    		}			    		 
			    		 
			    	 }
			      }
				  return order_number_fix;
				  //回傳跳過客戶的餐點數量	若剛好分完就回傳零    
			    } 
			    catch(SQLException e) 
			    { 
			      System.out.println("DropDB Exception :" + e.toString()); 
			      return 0;			   
			    } 
			    finally 
			    { 
			    	
			      Close(); 
			      
			    } 
		  }
	  //下面為卡住時的分配
	  public int selectnum2(String ord,String order ,String num,String com,int storecom,String subcom){//找尋顧客點餐數量和待完成數量
			Checkcustomer add=new Checkcustomer();
				//  int i=0;														//ord=store1-order1,store-order2...//num=number1,number2...//com=complete1,complete2...				
			      String[] tmp1=new String[10];									//order 是order1-order10
			      String[] tmp2=new String[10];									//storecom是店家完成數量(order1到order4)
			      tmp1=ord.split(":");
			     // System.out.println(tmp1[0]);
			      tmp2=tmp1[1].split("'");	
			      int flag=0;//標記有無分到該客戶 0無  1有
			  try 																//subcom是指定哪個完成表單ex.store1ordercomplete
			    {   			 	
				  stat = con.createStatement();			  
				  rs = stat.executeQuery("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);//這裡order1要改(是order1到order10)的ord
				//  System.out.println("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);	
				  while(rs.next()) 
			      { 	System.out.println("2正在搜索資料庫做分配.......");	    	  
			    	// System.out.print(rs.getInt(num)+"\t\t"+rs.getInt(com)+"\n");
			    //	 if(rs.getInt(num)>rs.getInt(com)&&storecom>=rs.getInt(num))
			      if(rs.getInt(num)>rs.getInt(com))//有顧客是等待填滿的的
			    	 {		   
			    		 if(storecom>=rs.getInt(num))//待分配完成餐點大於等於該客戶餐點數量
			    		 { 
			    			 sub.sub(subcom, tmp2[0],rs.getInt(num));
			    		//System.out.println(subcom);
			    		add.update("customertable",com,rs.getInt(num),order,ord,rs.getInt("id")); //update 搭配select
			    		 //System.out.println(tmp2[0]+"做好："+storecom+"然後要減掉"+rs.getInt(num));
			    		 //要把扣掉的storecomlete再加回去
			    		 //System.out.println(rs.getInt("id"));	
			    		
			    		flag= 1;//表示分完
			    		break;
			    		}
			    		 else
			    		 {
			    			 flag=0;
			    			 break;
			    			//若沒分到就跳出  
			    		 }	    	
			    	 }
			      }
				  return flag;
				  //回傳跳過客戶的餐點數量	若剛好分完就回傳零    
			    } 
			    catch(SQLException e) 
			    { 
			      System.out.println("DropDB Exception :" + e.toString()); 
			   return 0;
			    } 
			    finally 
			    { 
			    	
			      Close(); 
			      
			    } 
		  }
	  
	  public void update(String store, String com, int count,String order,String ordername,int id)//將處理完餐點放到已完成餐點裡
	  {				//store表單名，com放在見表裏的數量(complete1-complete10)，count要加在complete的數量
		  try			//order是指定表單order1order2order3....order10，ordername指定表單名字
		  {
			 pst=con.prepareStatement("update customertable  set "+com+" = "+com+" + "+count+" WHERE "+order+"="+ordername+" and id="+id); 
			 pst.executeUpdate(); 
		  }
		  catch(SQLException e)
		  {
			  System.out.println("Update Exception: "+e.toString());
		  }
		  finally
		  {
			  Close();
		  }
	  }	  
	    
	  
	public Checkcustomer() 
	  { 
	    try { 
	      Class.forName("com.mysql.jdbc.Driver"); 
	      //註冊driver 
	      con = DriverManager.getConnection( 
	      "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5", 
	      "root","12345");
	      con1 = DriverManager.getConnection( 
	    	      "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5", 
	    	      "root","12345");
	      //取得connection	 
	//jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5
	//localhost是主機名,test是database名
	//useUnicode=true&characterEncoding=Big5使用的編碼      
	    } 
	    catch(ClassNotFoundException e) 
	    { 
	      System.out.println("DriverClassNotFound :"+e.toString()); 
	    }//有可能會產生sqlexception 
	    catch(SQLException x){ 
	      System.out.println("Exception :"+x.toString()); 
	    }
	    } 
	private void Close() 
	  { 
	    try 
	    { 
	      if(rs!=null) 
	      { 
	        rs.close();
	        rs = null; 
	      } 
	      if(stat!=null) 
	      { 
	        stat.close(); 
	        stat = null; 
	      } 
	      if(pst!=null) 
	      { 
	        pst.close(); 
	        pst = null; 
	      } 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("Close Exception :" + e.toString()); 
	    } 
	  }
	
	 public void select(){//找尋顧客點餐數量和待完成數量			   
			  try 																//subcom是指定哪個完成表單ex.store1ordercomplete
			    {   			 
				 	
				  stat = con.createStatement();			  
				  rs = stat.executeQuery("SELECT * FROM `human`");//這裡order1要改(是order1到order10)的ord
				//  System.out.println("SELECT id,"+num+","+com +" FROM customertable WHERE "+order+"="+ord);	
				  while(rs.next()) 
			      { 	System.out.println("有來嗎");	    	  
			    	 System.out.print(rs.getString("situation")+"\t\t"+rs.getString("seat")+"\n");
			    	 
			      }	
			    
			    } 
			    catch(SQLException e) 
			    { 
			      System.out.println("DropDB Exception :" + e.toString()); 
			   
			    } 
			    finally 
			    { 
			    	
			      Close(); 
			      
			    } 
		  }
	public static void main(String[] args) 
	  { 	  
	   Checkcustomer a=new Checkcustomer();
	    //a.createTable(test);
	  //  a.insertTable(insterttest1);
	  //  a.SelectTable("customer");
		//a.selectnum("'Store2  -  : order3'", "order1","number1","complete1",3,"store2ordercomplete");
	  // a.update("customertable","complete1",4,"order1","'Store1  -  : order1'",4);
	   a.select();
	
	  }

}
