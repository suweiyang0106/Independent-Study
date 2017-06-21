package timeruse;

import java.util.Date;
import java.util.TimerTask;

import storeorder.Arrange;

public class Ordersendtimer extends TimerTask {
	//@ Override
      public void run() {
	          System.out.println(" Task 執行時間：把優先權坐滿" + new Date());
	          //把優先權坐滿
	          Arrange add=new Arrange();
	         add.timeradd();
	          };
}
