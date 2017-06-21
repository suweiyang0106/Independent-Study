package info.androidhive.loginandregistration.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
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

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.BService;
import info.androidhive.loginandregistration.helper.Get_Order_Infor;
import info.androidhive.loginandregistration.helper.ListView_Adapter;
import info.androidhive.loginandregistration.helper.MyDBHelper;
import info.androidhive.loginandregistration.helper.SessionManager;

public class Bill_Check extends AppCompatActivity {
    private static final String TAG = Bill_Check.class.getSimpleName();
    Cursor cursor;
    MyDBHelper controller;
    ListView lv;
    private  Thread thread;
    private  Thread getnumber_thread;
    private Thread gettime_thread;
    private ListView_Adapter adapter;
    List<Get_Order_Infor> order_list = new ArrayList<Get_Order_Infor>();
    private RequestQueue mQueue;
    SessionManager session;
    String account="";
    String[] settablewait=new String[11];
    int[] settablenumber= new int[11];
    private Socket clientSocket;
    private DataOutputStream br;
    int[] num1=new int[5];//要預估時間時得到的數量
    int[] num2=new int[5];
    int[] num3=new int[5];
    int[] num4=new int[5];
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill__check);
        setTitle("結帳");
        session = new SessionManager(getApplicationContext());//讓session有初值，不然會null pointer exception
        account=session.getAccout().toString();
       controller=new MyDBHelper(this,"",null,1);;
       cursor =controller.list_all();
        lv=(ListView)findViewById(R.id.listView);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        for(int i=0;i<11;i++)
        {
            settablewait[i]="";
        }

       Adapter( cursor);


    }
    public void send(View view) {//
    if(!isMyServiceRunning(BService.class)){
        intent=new Intent(this,BService.class);
        getnumber_thread =new Thread(Getnumber_Thread);
        getnumber_thread.start();//用共同變數設  但是太多工作了 會壞
        setAlertDialog1Event();
    }
        else {
        Toast.makeText(this, "餐點完成後才能進行下一次點餐", Toast.LENGTH_SHORT).show();
    }
    }
    private void setAlertDialog1Event(){
        new AlertDialog.Builder(Bill_Check.this)
                .setTitle("確定送出?")
                .setMessage("確定送出?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settable();
                       thread=new Thread(Connection);
                     thread.start();//送出
                       startService(intent);//推波打開
                        gettime_thread =new Thread(GetTime_Thread);
                        gettime_thread.start();//用共同變數設  但是太多工作了 會壞

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {//確認service有沒有再進行
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void Adapter(Cursor cursor)//listview
    {
        int count=1;
        if(cursor!=null&&cursor.getCount()>=0)
        {
            System.out.println("eeeeeeeeeeee");
            for(int i=0;i<11;i++)
                settablewait[i]="null";
            while (cursor.moveToNext())
            {
                order_list.add(new Get_Order_Infor(cursor.getString(2),cursor.getInt(3)));

                    settablewait[count]=cursor.getString(1);//將資料庫讀進陣列(英文和數字)
                    settablenumber[count]=cursor.getInt(3);
                   System.out.println(settablewait[count]+"陣列李"+settablenumber[count]);
                    System.out.println(count+"技術");
                count++;
            }

            System.out.println("dfdfdfdfdfdfd");
            adapter = new ListView_Adapter(this,order_list);
           lv.setAdapter(adapter);
        }
    }

    private void settable() {//建立表格
       // StringRequest strReq = new StringRequest(Request.Method.POST, "http://192.168.137.1/customer/insert.php"
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.SetTable
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
                map.put("account", account);
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
    private Runnable Connection=new Runnable() {//排程伺服器連線
        @Override
        public void run() {
            try{
                cursor =controller.list_all();//更新cursor一次不然movetonext會回不來
                if(cursor!=null&&cursor.getCount()>=0)
                {
                System.out.println("pppppp");
                while (cursor.moveToNext())
                { System.out.println("aaaaa");
                    for(int i=0;i<cursor.getInt(3);i++)
                    {
                        System.out.println("bbbbb");
                       clientSocket=new Socket(InetAddress.getByName(AppConfig.Connection),5051);
                        br=new DataOutputStream(clientSocket.getOutputStream());
                        br.writeUTF(cursor.getString(1));
                        clientSocket.close();
                        System.out.println(cursor.getString(1)+"pppppp");
                    }
                }
                }
            }catch(Exception e){System.out.println("Error"+e);
            }
        }
    };
    private Runnable Getnumber_Thread=new Runnable() {//取得數量
        @Override
        public void run() {
            RequestQueue queue = Volley.newRequestQueue(Bill_Check.this);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfig.Store1_Number,
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
                                    System.out.println("店家1: "+num1[0]+" item是"+items);
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
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, AppConfig.Store2_Number,
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
            StringRequest stringRequest3 = new StringRequest(Request.Method.POST, AppConfig.Store3_Number,
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
            StringRequest stringRequest4 = new StringRequest(Request.Method.POST, AppConfig.Store4_Number,
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
    };
    private Runnable GetTime_Thread =new Runnable() {//取得時間
        @Override
        public void run() {

            String[] tmp=new String[3];//用來辨認點餐有哪幾樣的暫存器
            int[] tmp1=new int[5];//辨認需要用到哪幾家的時間
         int time=0;//等候時間回傳直，也要比較自己的點餐有餐與到哪些餐廳以及自己的時間
            int time1,time2,time3,time4=-1;
            int[] selfnum1=new int[5],selfnum2=new int[5],selfnum3=new int[5],selfnum4=new int[5];
            time1=Math.max(0,num1[0]+num1[1]*2+num1[2]*3+num1[3]*4);//time1為得到代做食物後計算的時間(自己的要另外算)
            time2=num2[0]+num2[1]+num2[2]+num2[3];
            time3=num3[0]+num3[1]+num3[2]+num3[3];
            time4=num4[0]+num4[1]+num4[2]+num4[3];
            System.out.println("自己數量: "+settablenumber[1]);
            System.out.println("他人數量: "+num1[0]);
            int timeself=0;
      /*  time=Math.max(time1,time2);
        time=Math.max(time,time3);
        time=Math.max(time,time4);*/
            for(int i=1;i<11;i++)
            { //用settablewait[]來分溪有點了哪幾種菜 以便得知需要那些店的等候時間 ex: store1  -  : order1
                tmp=settablewait[i].split(":");
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
            timeself=Math.max(0,selfnum1[0]+selfnum1[1]*2+selfnum1[2]*3+selfnum1[3]*4);
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
           System.out.println(time+"時間是這個"+"數量測試:"+num1[0]+"\t"+num1[1]+"\t"+num1[2]+"\t"+num1[3]);
            System.out.println("aaaaaa");

            final int finalTime = time+10;
           // ManuTest.time.setText(finalTime+"aaaa");
            Bill_Check.this.runOnUiThread(new Runnable() {
               public void run() {
                   Toast.makeText(Bill_Check.this, "時間為: "+ finalTime +"秒", Toast.LENGTH_SHORT).show();
                    }
            });
            Intent toStart=new Intent(getApplicationContext(),ManuTest.class);
            toStart.putExtra("time",finalTime+"秒");
           toStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toStart);
        }

    };



    //...................................................以下函釋 庫沒用到................................................................................................
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

    public void get_time(View view) {
        gettime_thread=new Thread(GetTime_Thread);
         gettime_thread.start();
    }
}
