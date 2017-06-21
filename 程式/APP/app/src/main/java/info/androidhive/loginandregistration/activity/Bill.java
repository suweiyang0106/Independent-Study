package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.BService;
import info.androidhive.loginandregistration.helper.SessionManager;

public class Bill extends AppCompatActivity {

    private static final String TAG = Bill.class.getSimpleName();
    private  Thread thread;
    private Socket clientSocket;
    private DataInputStream bw;
    private DataOutputStream br;
    private RequestQueue mQueue;
    SessionManager session;
    String[] settablewait;//有哪幾種點餐 從settablewait[1]開始為第一樣餐點
    int[] settablenumber;//和哪幾種點餐相對應那樣餐有幾樣 從settablenumber[1]開始為第一樣餐點有幾分
    String[] passstr=new String[50];
    String[] tmp=new String[11];
    int passnum=0;
    int num=1;
   int[] num1=new int[5];//要預估時間時得到的數量
    int[] num2=new int[5];
    int[] num3=new int[5];
    int[] num4=new int[5];
    ProgressDialog dialog;
    int time=-1;//等候時間預估
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        session = new SessionManager(getApplicationContext());//讓session有初值，不然會null pointer exception
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = this.getIntent().getExtras();
        passnum=bundle.getInt("passnumber");
        passstr=bundle.getStringArray("passstring");
        tmp=bundle.getStringArray("passstring");
        passstr[passnum+1]="null";
        passstr[0]=session.getAccout();
        exchange(tmp,passnum);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void send(View view) {
        settable();//見表
       thread=new Thread(Connection);
        thread.start();//送出
        Toast.makeText(Bill.this, "已送出", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Bill.this,BService.class);
        startService(intent);//推波打開
    }

    public void back(View view) {
        Intent intent=new Intent(Bill.this,StoreOrderActivity.class);
        startActivity(intent);
        finish();
    }
    private Runnable Connection=new Runnable() {
        @Override
        public void run() {
            try{
                for(num=1;num<=passnum+1;num++)
                {
                    clientSocket=new Socket(InetAddress.getByName("192.168.1.213"),5051);
                    br=new DataOutputStream(clientSocket.getOutputStream());
                    br.writeUTF(passstr[num]);
                    clientSocket.close();
                }
            }catch(Exception e){System.out.println("Error"+e);
            }
        }
    };
    private void exchange(String[] str,int i){
        settablewait=new String[11];
        int rem=0;
        /*for(int d=0;d<=11;d++)
            settablewait[d]="null";//把null插進去是看看這樣能不能沒填滿建表*/
        settablenumber=new int[11];
        int h=1;
        int flag=0;
           settablewait[1]=str[1];
           settablenumber[1]++;
        for(int j=2;j<=i;j++)
        {
          for(int k=1;k<=h;k++)
          {
             if(str[j].equals(settablewait[k])){
                  settablenumber[k]++;
                 flag=1;
                 break;
              }
          }
            if(flag==0){
                h++;
                settablewait[h]=tmp[j];
                settablenumber[h]++;
            }
            flag=0;
        }
        for(int a=1;a<11;a++){
            System.out.println("點了："+settablewait[a]+"有"+settablenumber[a]+"份喔");
            if(settablewait[a]==null)
            {
                rem=a;
                break;
            }
        }
        for(int x=rem;x<=10;x++)
            settablewait[x]="null";

    };
    private void settable() {
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://192.168.1.213/customer/insert.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                Map<Integer, Integer> map1 = new HashMap<Integer, Integer>();
                Log.d(TAG, "幹咧不適從邊船嗎");
                map.put("account", passstr[0]);
                map.put("order1", settablewait[1]);
                map.put("number1", String.valueOf(settablenumber[1]));
                map.put("order2", settablewait[2]);
                map.put("order3", settablewait[3]);
                map.put("order4", settablewait[4]);
                map.put("order5", settablewait[5]);
                map.put("order6", settablewait[6]);
                map.put("order7", settablewait[7]);
                map.put("order8", settablewait[8]);
                map.put("order9", settablewait[9]);
                map.put("order10", settablewait[10]);
                map.put("number2", String.valueOf(settablenumber[2]));
                map.put("number3", String.valueOf(settablenumber[3]));
                map.put("number4", String.valueOf(settablenumber[4]));
                map.put("number5", String.valueOf(settablenumber[5]));
                map.put("number6", String.valueOf(settablenumber[6]));
                map.put("number7", String.valueOf(settablenumber[7]));
                map.put("number8", String.valueOf(settablenumber[8]));
                map.put("number9", String.valueOf(settablenumber[9]));
                map.put("number10", String.valueOf(settablenumber[10]));
                return map;
            }
        };
        mQueue.add(strReq);
    }

    public void getnumberfortime()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.1.213/customer/time1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        GridView gv=(GridView)findViewById(R.id.gridView);
                        ArrayList<String> items=new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for(int j=0;j<4;j++)
                                {
                                    num1[j]=Integer.valueOf(items.get(j));
                                }
                                System.out.println("店家1: "+num1[0]);
                            }
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
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.1.213/customer/time2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        ArrayList<String> items=new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for(int j=0;j<4;j++)
                                {
                                    num2[j]=Integer.valueOf(items.get(j));
                                }
                                System.out.println("店家2: "+items);
                            }
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
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, "http://192.168.1.213/customer/time3.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        GridView gv=(GridView)findViewById(R.id.gridView);
                        ArrayList<String> items=new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for(int j=0;j<4;j++)
                                {
                                    num3[j]=Integer.valueOf(items.get(j));
                                }
                                System.out.println("店家3: "+items);
                            }
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
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, "http://192.168.1.213/customer/time4.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        GridView gv=(GridView)findViewById(R.id.gridView);
                        ArrayList<String> items=new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for(int j=0;j<4;j++)
                                {
                                    num4[j]=Integer.valueOf(items.get(j));
                                }
                                System.out.println("店家4: "+items);

                            }
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
        queue.add(stringRequest2);
        queue.add(stringRequest3);
        queue.add(stringRequest4);
    }
    public int counttime()//算屋時間預估，得出廚房做菜時間以及自己點餐後要做菜的時間
    {
        String[] tmp=new String[3];//用來辨認點餐有哪幾樣的暫存器
        int[] tmp1=new int[5];//辨認需要用到哪幾家的時間
        int time=0;//等候時間回傳直，也要比較自己的點餐有餐與到哪些餐廳以及自己的時間
        int time1,time2,time3,time4=-1;
        int[] selfnum1=new int[5],selfnum2=new int[5],selfnum3=new int[5],selfnum4=new int[5];
        time1=Math.max(num1[0]+num1[1]*2,num1[2]*3+num1[3]*4);//time1為得到代做食物後計算的時間(自己的要另外算)
        time2=num2[0]+num2[1]+num2[2]+num2[3];
        time3=num3[0]+num3[1]+num3[2]+num3[3];
        time4=num4[0]+num4[1]+num4[2]+num4[3];
       int timeself=0;
      /*  time=Math.max(time1,time2);
        time=Math.max(time,time3);
        time=Math.max(time,time4);*/
        for(int i=1;i<11;i++)
        { //用settablewait[]來分溪有點了哪幾種菜 以便得知需要那些店的等候時間 ex: store1  -  : order1
            tmp=settablewait[i].split("  -  : ");
            if(tmp[0].equals("Store1"))//從這裡著手
            {
                tmp1[0]=1;
                if(tmp[1].equals("order1"))
                    selfnum1[0]=settablenumber[i];
                else if(tmp[1].equals("order2"))
                    selfnum1[1]=settablenumber[i];
                else if(tmp[1].equals("order3"))
                    selfnum1[2]=settablenumber[i];
                else if(tmp[1].equals("order4"))
                    selfnum1[3]=settablenumber[i];
            }
           else if(tmp[0].equals("Store2"))
            {
                tmp1[1]=1;
                if(tmp[1].equals("order1"))
                    selfnum2[0]=settablenumber[i];
                else if(tmp[1].equals("order2"))
                    selfnum2[1]=settablenumber[i];
                else if(tmp[1].equals("order3"))
                    selfnum2[2]=settablenumber[i];
                else if(tmp[1].equals("order4"))
                    selfnum2[3]=settablenumber[i];
            }
           else if(tmp[0].equals("Store3"))
            {
                tmp1[2]=1;
                if(tmp[1].equals("order1"))
                    selfnum3[0]=settablenumber[i];
                else if(tmp[1].equals("order2"))
                    selfnum3[1]=settablenumber[i];
                else if(tmp[1].equals("order3"))
                    selfnum3[2]=settablenumber[i];
                else if(tmp[1].equals("order4"))
                    selfnum3[3]=settablenumber[i];
            }
            else if(tmp[0].equals("Store4"))
            {
                tmp1[3]=1;
                if(tmp[1].equals("order1"))
                    selfnum4[0]=settablenumber[i];
                else if(tmp[1].equals("order2"))
                    selfnum4[1]=settablenumber[i];
                else if(tmp[1].equals("order3"))
                    selfnum4[2]=settablenumber[i];
                else if(tmp[1].equals("order4"))
                    selfnum4[3]=settablenumber[i];
            }
        }
        timeself=Math.max(selfnum1[0]+selfnum1[1]*2,selfnum1[2]*3+selfnum1[3]*4);
        timeself=Math.max(timeself,selfnum2[0]+selfnum2[1]+selfnum2[2]+selfnum2[3]);
        timeself=Math.max(timeself,selfnum3[0]+selfnum3[1]+selfnum3[2]+selfnum3[3]);
        timeself=Math.max(timeself,selfnum4[0]+selfnum4[1]+selfnum4[2]+selfnum4[3]);
       if(tmp1[0]==1)
           time=Math.max(time,time1);
        if(tmp1[1]==1)
            time=Math.max(time,time2);
        if(tmp1[2]==1)
            time=Math.max(time,time3);
        if(tmp1[3]==1)
            time=Math.max(time,time4);
        time=timeself+time;
        return time;
    }
    public void build(View view) {
        //擺完後再建表
        //settable();

        dialog = ProgressDialog.show(Bill.this,
                "處理中", "資料處理中，請稍等...", true);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    getnumberfortime();
                    Thread.sleep(500);//上下隔秒可以等候讀取
                    time=counttime();
                    Thread.sleep(100);
                    System.out.println(time + " 數量喔");
                    Bill.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Bill.this, "預估時間為: "+time, Toast.LENGTH_SHORT).show();//toast要放在thread裡要有特別的thread來包住它
                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally{
                    dialog.dismiss();
                }
            }
        }).start();

    }
    public void service(View view) {
        Intent intent=new Intent(Bill.this,BService.class);
        startService(intent);
    }
}
