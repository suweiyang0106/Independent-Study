package info.androidhive.loginandregistration.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.activity.ManuTest;
import info.androidhive.loginandregistration.helper.SessionManager;

/**
 * Created by Aspire on 2016/9/20.
 */
public class BService extends Service {
    private Handler handler = new Handler();
    int check=0;
    SessionManager session;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(showTime, 1000);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(showTime);
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
            Log.i("time:", new Date().toString());
            if(check==1){//廣播
                Toast.makeText(BService.this, "餐點已完成", Toast.LENGTH_SHORT).show();//每隔一段時間去做check
                inform();//check好了再坐回傳
                Intent intent=new Intent(BService.this,BService.class);
                stopService(intent);//把stop拿掉後就算清記憶體也不會被刪掉(不知道為啥
            };
         Thread thread = new Thread(mutiThread);
            thread.start();//Only the original thread that created a view hierarchy can touch its views. <-錯誤  找出table再放的時候有什麼問題  一下有一下沒有的顯示
            handler.postDelayed(this, 1000);
        }
    };
    public  void inform(){
        final int notifyID = 1; // 通知的識別號碼
        final int requestCode = notifyID;
        final boolean autoCancel = true; // 點擊通知後是否要自動移除掉通知
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        final Intent intent = new Intent(BService.this, ManuTest.class); // 目前Activity的Intent
        final int flags = PendingIntent.FLAG_CANCEL_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, flags); // 取得PendingIntent
        final Notification.InboxStyle inboxStyle = new Notification.InboxStyle(); // 建立InboxStyle
        final String[] lines = new String[] { " 餐點做好了，請前往拿取" }; // InboxStyle要顯示的字串內容
        inboxStyle.setBigContentTitle("您有餐點訊息："); // 當InboxStyle顯示時，用InboxStyle的setBigContentTitle覆蓋setContentTitle的設定
        inboxStyle.setSummaryText("更多新訊息......"); // InboxStyle的底部訊息
        for (int i = 0; i < lines.length; i++) {
            inboxStyle.addLine(String.format("%d: %s", i + 1, lines[i])); // 將字串加入InboxStyle
        }
        final Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_resturant).setContentIntent(pendingIntent).setStyle(inboxStyle)
                .setAutoCancel(autoCancel)// 建立通知
                .setDefaults(Notification.DEFAULT_LIGHTS |Notification.DEFAULT_VIBRATE)
                .build();
        notificationManager.notify(notifyID, notification); // 發送通知

    }
    public void check()
    {
            try
            {
                String result = DBConnector.executeQuery("SELECT * FROM customer");
                        /*SQL 結果有多筆資料時使用JSONArray
只有一筆資料時直接建立JSONObject物件
     JSONObject jsonData = new JSONObject(result);*/
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    if(jsonData.getString("number1").equals(jsonData.getString("complete1"))&&
                            jsonData.getString("number2").equals(jsonData.getString("complete2"))&&
                            jsonData.getString("number3").equals(jsonData.getString("complete3"))&&
                            jsonData.getString("number4").equals(jsonData.getString("complete4"))&&
                            jsonData.getString("number5").equals(jsonData.getString("complete5"))&&
                            jsonData.getString("number6").equals(jsonData.getString("complete6"))&&
                            jsonData.getString("number7").equals(jsonData.getString("complete7"))&&
                            jsonData.getString("number8").equals(jsonData.getString("complete8"))&&
                            jsonData.getString("number9").equals(jsonData.getString("complete9"))&&
                            jsonData.getString("number10").equals(jsonData.getString("complete10")))
                    {
                        check=1;
                    }
                }

            }
            catch (Exception e)
            {
                Log.e("MainACTIVITY 的 log_tag", e.toString());
            }
    }
    private Runnable mutiThread = new Runnable(){
        public void run() {
           // check();
            getcontent();
        }
    };
    public void getcontent( )
    {
        session = new SessionManager(getApplicationContext());//讓session有初值，不然會null pointer exception
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";
        System.out.println("段妳妹");
// Request a string response from the provided URL.  http://192.168.1.221/customer/time1.php
       // StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.137.1/customer/select2.php",
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfig.BroadCast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        System.out.println("aaaaaa");
                        //  ArrayList<String> items=new ArrayList<String>();
                        JSONArray jsonArray = null;
                        System.out.println("依第二個");
                        try {
                            jsonArray = new JSONArray(response);
                            System.out.println("fuck");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                if(jsonData.getString("number1").equals(jsonData.getString("complete1"))&&
                                        jsonData.getString("number2").equals(jsonData.getString("complete2"))&&
                                        jsonData.getString("number3").equals(jsonData.getString("complete3"))&&
                                        jsonData.getString("number4").equals(jsonData.getString("complete4"))&&
                                        jsonData.getString("number5").equals(jsonData.getString("complete5")))
                                {
                                    check=1;
                                }
                            }
                            //  System.out.println("店家1: "+items.get(2).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("account",session.getAccout().toString());
                return params;
            }
        };
        queue.add(stringRequest1);
    }
}
