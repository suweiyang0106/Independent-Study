package storeorder;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TimerDemo {

	public TimerDemo() {
		// TODO Auto-generated constructor stub
	}

	 public static void main(String[] args) {
		  
		         TimerDemo timerDemo = new TimerDemo();
		         // timerDemo.testScheduleDelay();
		         timerDemo.store1timer();
		        //  timerDemo.testScheduleDelayAndPeriod();
		        //  timerDemo.testScheduleDateAndPeriod();
		    }
	/* void testScheduleDelay(){
		  
		 java.util.Timer timer = new java.util.Timer();
		          System.out.println("Delay：3秒");
		          System.out.println("In testScheduleDelay：" + new Date());
		  
		          // schedule(TimerTask task, long delay)
		          timer.schedule(new Timer(), 3000);		          
		         try {
		             Thread.sleep(3000);
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		         System.out.println("End testScheduleDelay："+ new Date());
		      }*/	 
	 
	 void store1timer(){
		  
		 TimerStore1 timers = new TimerStore1();
		 Timer timer=new Timer();
		          System.out.println("Delay：3秒");
		          System.out.println("In testScheduleDelay：" + new Date());
		  
		          // schedule(TimerTask task, long delay)
		          timer.schedule(timers, 3000);		          
		         try {
		             Thread.sleep(3000);
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		         System.out.println("End testScheduleDelay："+ new Date());
		      }
	 
	 
/*	 void testScheduleDelayAndPeriod(){
		  
		 java.util.Timer timer = new java.util.Timer();
		          System.out.println("Delay：3秒, Period：2秒");
		          System.out.println("In testScheduleDelayAndPeriod：" 
		              + new Date());
		          
		          // schedule(TimerTask task, long delay, long period)
		          timer.schedule(new Timer(), 3000, 2000);
		          
		         try {
		              Thread.sleep(10000);
		          }
		              catch(InterruptedException e) {
		          }
		  
		          timer.cancel();
		          System.out.println("End testScheduleDelayAndPeriod：" 
		              + new Date() + "\n");
		      }
	  void testScheduleDateAndPeriod(){
		   
		  java.util.Timer timer = new java.util.Timer();
		           
		           // 設定填入schedule中的 Date firstTime為現在的15秒後
		          Calendar calendar = Calendar.getInstance();
		           calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)+15);
		           Date firstTime = calendar.getTime();
		           
		           // 也可用 simpleDateFormat 直接設定 firstTime的精確時間
		           // SimpleDateFormat dateFormatter = 
		           //      new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
		           // Date firstTime = dateFormatter.parse("2011/12/25 13:30:00");
		           
		           System.out.println("In testScheduleDateAndPeriod：" 
		               + new Date());
		           System.out.println("設定執行 Date 為15秒後：" 
		               + firstTime +", Period：10秒");
		                   
		           // schedule(TimerTask task, Date firstTime, long period)
		           timer.schedule(new Timer(), firstTime, 10000);		                   
		           try {
		               Thread.sleep(30000);
		          }
		               catch(InterruptedException e) {
		          }		   
		           timer.cancel();
		           System.out.println("End testScheduleDateAndPeriod：" 
		              + new Date() + "\n");
		       }*/
	
	
}
