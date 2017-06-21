package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.Order;
import info.androidhive.loginandregistration.helper.MyAdapter;
import info.androidhive.loginandregistration.helper.SessionManager;

public class SelectOrderActivity extends AppCompatActivity {
    SessionManager session;
    // GridView gv;
    ListView lv;
    List<Order> order_list = new ArrayList<Order>();
    MyAdapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("訂單查詢");
        lv=(ListView)findViewById(R.id.ListView1) ;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        System.out.print("標標標");
        Get_Self();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.d("tag","延長兩秒再做");//延長兩秒讓lLIST有時間載入
                progressDialog.dismiss();
                //meal.setAdapter(contentlist);
            }},3000);
    }
    public boolean Get_Self()
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
                        ArrayList<String> items=new ArrayList<String>();
                        order_list.add(new Order("餐點","數量","完成數量"));
                        try {
                            jsonArray = new JSONArray(response);
                            System.out.println("fuck");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                order_list.add(new Order(jsonData.getString("A"),jsonData.getString("number1"),jsonData.getString("complete1")));
                                if(jsonData.getString("B").equals("null"))
                                {
                                    System.out.print("跳跳跳");
                                    myadapter=new MyAdapter(SelectOrderActivity.this,order_list);
                                    lv.setAdapter(myadapter);
                                    break;
                                }
                                order_list.add(new Order(jsonData.getString("B"),jsonData.getString("number2"),jsonData.getString("complete2")));
                                if(jsonData.getString("C").equals("null")) {
                                    System.out.print("跳跳跳");
                                    myadapter=new MyAdapter(SelectOrderActivity.this,order_list);
                                    lv.setAdapter(myadapter);
                                    break;
                                }
                                order_list.add(new Order(jsonData.getString("C"),jsonData.getString("number3"),jsonData.getString("complete3")));
                                if(jsonData.getString("D").equals("null"))
                                {
                                    System.out.print("跳跳跳");
                                    myadapter=new MyAdapter(SelectOrderActivity.this,order_list);
                                    lv.setAdapter(myadapter);
                                    break;
                                }
                                order_list.add(new Order(jsonData.getString("D"),jsonData.getString("number4"),jsonData.getString("complete4")));
                                if(jsonData.getString("E").equals("null"))
                                {
                                    System.out.print("跳跳跳");
                                    myadapter=new MyAdapter(SelectOrderActivity.this,order_list);
                                    lv.setAdapter(myadapter);
                                    break;
                                }
                                order_list.add(new Order(jsonData.getString("E"),jsonData.getString("number5"),jsonData.getString("complete5")));
                                if(jsonData.getString("order6").equals("null"))
                                {
                                    System.out.print("跳跳跳");
                                    myadapter=new MyAdapter(SelectOrderActivity.this,order_list);
                                    lv.setAdapter(myadapter);
                                    break;
                                }
                            }
                            //  System.out.println("店家1: "+items.get(2).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.print("錯誤： "+e.getMessage());
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
        return true;
    }
}
