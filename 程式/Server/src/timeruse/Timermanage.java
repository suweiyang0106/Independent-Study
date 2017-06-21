package timeruse;

import java.util.Date;
import java.util.Timer;

import storeorder.TimerDemo;
import storeorder.TimerStore1;

public class Timermanage {
	String[] order={"order1","order2","order3","order4"};
	 public static void main(String[] args) {		  
		 Timermanage timerDemo = new Timermanage();
         // timerDemo.testScheduleDelay();
        // timerDemo.store1timer();
        //  timerDemo.testScheduleDelayAndPeriod();
        //  timerDemo.testScheduleDateAndPeriod();
    }
	public  void store1timer(int i,int j,int time){//超過20個計時開始做		  
		 Store1timer timers = new Store1timer();
		 Timer timer=new Timer();
		      //    System.out.println("Delay：3秒");
		          //System.out.println("In testScheduleDelay：" + new Date());		  
		          // schedule(TimerTask task, long delay)
		 		timers.accetproder(order[i], j);
		 		System.out.println("Store1做"+order[i]+"要做的有"+j);
		          timer.schedule(timers, j*time);		          
		         try {
		             Thread.sleep(j*(time+100));//sleep時間要比等待(schedule)長，確保能計時完畢		             
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		        System.out.println("做完一項載："+ new Date());
		      }
	public  void store2timer(int i,int j){//超過20個計時開始做
		  
		 Store2timer timers = new Store2timer();
		 Timer timer=new Timer();
		      //    System.out.println("Delay：3秒");
		        //  System.out.println("In testScheduleDelay：" + new Date());
		  
		          // schedule(TimerTask task, long delay)
		 		timers.accetproder(order[i],j);
		 		System.out.println("Store2做"+order[i]+"要做的有"+j);
		          timer.schedule(timers, j*1000);		          
		         try {
		             Thread.sleep(j*1100);
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		         System.out.println("End testScheduleDelay："+ new Date());
		      }
	public void store3timer(int i,int j){//超過20個計時開始做
		  
		 Store3timer timers = new Store3timer();
		 Timer timer=new Timer();
		      //    System.out.println("Delay：3秒");
		        //  System.out.println("In testScheduleDelay：" + new Date());
		  
		          // schedule(TimerTask task, long delay)
		 		timers.accetproder(order[i],j);
		          timer.schedule(timers, j*1000);		 
		          System.out.println("Store3做"+order[i]+"要做的有"+j);
		         try {
		             Thread.sleep(j*1100);
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		         System.out.println("End testScheduleDelay："+ new Date());
		      }
	public void store4timer(int i,int j){//超過20個計時開始做
		  
		 Store4timer timers = new Store4timer();
		 Timer timer=new Timer();
		      //    System.out.println("Delay：3秒");
		        //  System.out.println("In testScheduleDelay：" + new Date());
		  
		          // schedule(TimerTask task, long delay)
		 		timers.accetproder(order[i],j);
		          timer.schedule(timers, j*1000);	
		          System.out.println("Store4做"+order[i]+"要做的有"+j);
		         try {
		             Thread.sleep(j*1100);
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		         System.out.println("End testScheduleDelay："+ new Date());
		      }
	
	public void ordersendtimer(){//計時到優先權加滿送至資料庫
		 Ordersendtimer timers = new Ordersendtimer();
		 Timer timer=new Timer();
		          //System.out.println("Delay：3秒");
		          System.out.println("計時要送出：" + new Date());
		  
		           //schedule(TimerTask task, long delay)
		          timer.schedule(timers, 10000);		          
		         try {
		             Thread.sleep(10000);
		          }
		              catch(InterruptedException e) {
		          }		  
		          timer.cancel();
		         System.out.println("計時時間到："+ new Date());
		 
	 }

}
