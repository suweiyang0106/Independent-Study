package info.androidhive.loginandregistration.app;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aspire on 2016/12/17.
 */

public class PopulateService extends Service {


    public void loadpopulat()
    {
        RequestQueue queue = Volley.newRequestQueue(this);//requestqueu 給初始值安安安安
        System.out.println("");
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfig.population,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        ArrayList<String> items = new ArrayList<String>();
                        System.out.println("ㄇㄇㄇ");
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            System.out.println("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("situation"));
                                items.add(jsonData.getString("seat"));
                            }
                   /*    if(items.get(0).toString().equals("very crowded"))
                            {ManuTest.crowded.setText("非常擁擠");}//非常擁擠 very crowd
                            else if(items.get(0).toString().equals("crowded")){ ManuTest.crowded.setText("擁擠");}//擁擠 crowd
                            else if(items.get(0).toString().equals("normal")){ ManuTest.crowded.setText("一般");}// 一般 normal
                            else if(items.get(0).toString().equals("few")){ ManuTest.crowded.setText("鬆散");}//鬆散 few
                            else if(items.get(0).toString().equals("very few")){ ManuTest.crowded.setText("非常鬆散");}//非常鬆散 very few*/
                            System.out.print("改");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        queue.add(stringRequest1);
    }
    private Handler handler = new Handler();

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
            Log.i("時間:", new Date().toString());
            handler.postDelayed(this, 1000);
        }
    };
}
