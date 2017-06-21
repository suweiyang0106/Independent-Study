package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.helper.Get_Order_Infor;
import info.androidhive.loginandregistration.helper.ListView_Adapter;
import info.androidhive.loginandregistration.helper.MyDBHelper;
import info.androidhive.loginandregistration.helper.No_Dish_Fragment;
import info.androidhive.loginandregistration.helper.With_Dish_Fragment;

public class OrderActivity extends AppCompatActivity {
    private Context mContext;
    private Spinner store,meal;//spinner 店家案餐點
    ArrayList<String> StoreList=new ArrayList<String>();//店家名稱 中文
    ArrayList<String>CONTENTLIST=new ArrayList<String>();//餐點名稱 中文
    ArrayList<String>STORELABEL=new ArrayList<String>();//label  s1 s2 s3 s4
    ArrayList<String>LABEL=new ArrayList<String>();//label   store1  -  : order1
    ArrayAdapter<String> storeList; //店家spinner adapter
    ArrayAdapter<String> contentlist;//餐點spinner adapter
    private EditText amount;
    ListView lv;
    Cursor cursor;
    MyDBHelper controller;
    List<Get_Order_Infor> order_list = new ArrayList<Get_Order_Infor>();
    private ListView_Adapter adapter;
    private ArrayAdapter<String> listAdapter;
    private String chinese="", english="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mContext = this.getApplicationContext();
        setTitle("點餐");
        spinnergetstore();
        load_dialog(7000);
        store=(Spinner) findViewById(R.id.store);
        meal=(Spinner)findViewById(R.id.meal);
       storeList = new ArrayAdapter<String>(OrderActivity.this, android.R.layout.simple_gallery_item,StoreList);
        amount=(EditText)findViewById(R.id.editText) ;
        lv=(ListView)findViewById(R.id.listView);
        setRegion(amount);
        android.app.FragmentManager fragmentManager=getFragmentManager();//初始化fragment  也可在點完餐後才出現
        android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        No_Dish_Fragment no_dish=new No_Dish_Fragment();
        fragmentTransaction.add(R.id.fragment_container,no_dish);
        fragmentTransaction.commit();
        controller=new MyDBHelper(this,"",null,1);
        cursor = controller.list_all();//顯示listview
        order_list = new ArrayList<Get_Order_Infor>();//顯示listview
        Adapter(cursor);//顯示listview
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.d("tag","延長兩秒再做");//延長兩秒讓lLIST有時間載入
                store.setAdapter(storeList);
                //meal.setAdapter(contentlist);
            }},3000);
        store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
              //  Toast.makeText(mContext,StoreList.get(position)+"  and  "+STORELABEL.get(position),Toast.LENGTH_SHORT).show();
                if(position==1)
                {
                    android.app.FragmentManager fragmentManager=getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    With_Dish_Fragment with_dish=new With_Dish_Fragment();
                    fragmentTransaction.replace(R.id.fragment_container,with_dish);
                    fragmentTransaction .commit();
                    load_dialog(4000);
                }
                else
                {
                    android.app.FragmentManager fragmentManager=getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    No_Dish_Fragment no_dish=new No_Dish_Fragment();
                    fragmentTransaction.replace(R.id.fragment_container,no_dish);
                    fragmentTransaction .commit();
                    load_dialog(4000);
                }
                getcontent(STORELABEL.get(position));
                Handler handler1 = new Handler();

                contentlist=new ArrayAdapter<String>(OrderActivity.this,android.R.layout.simple_gallery_item,CONTENTLIST);
                handler1.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Log.d("tag","延長dfdfd兩秒再做");//延長兩秒讓lLIST有時間載入
                        meal.setAdapter(contentlist);
                    }}, 3000);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        meal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
               // Toast.makeText( mContext,CONTENTLIST.get(position)+"  and  "+LABEL.get(position),Toast.LENGTH_SHORT).show();
               english=LABEL.get(position).toString();
               chinese=CONTENTLIST.get(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView c = (TextView)view.findViewById(R.id.dish);
                final String text = c.getText().toString();
                // Toast.makeText(getApplicationContext(), "你選擇的是" + position+text ,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog=new AlertDialog.Builder(OrderActivity.this);
                dialog.setTitle("輸入修改數量");
                final EditText new_amount=new EditText(OrderActivity.this);
                new_amount.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialog.setView(new_amount);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( !new_amount.getText().toString().equals(""))
                        {   controller.update(text,
                                Integer.parseInt(new_amount.getText().toString()));
                            cursor=controller.list_all();
                            order_list = new ArrayList<Get_Order_Infor>();
                            Adapter(cursor);}
                        else
                        {
                            Toast.makeText(getApplicationContext(), "修改數字不可空白"  ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("捨棄", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.delete(text);
                        cursor=controller.list_all();
                        order_list = new ArrayList<Get_Order_Infor>();
                        Adapter(cursor);
                    }
                });
                dialog.show();
            }
        });

    }

    public void spinnergetstore()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";
        System.out.println("段妳妹");
// Request a string response from the provided URL.  http://192.168.1.221/customer/time1.php
        //StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.137.1/get_store/store.php",
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfig.Spinner_Store,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        System.out.println("aaaaaa");
                        //  ArrayList<String> items=new ArrayList<String>();
                        JSONArray jsonArray = null;
                        System.out.println("第個");
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                StoreList.add(jsonData.getString("content"));
                                STORELABEL.add(jsonData.getString("label"));
                            }
                            System.out.println("店家1: "+STORELABEL.get(2).toString());
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
    private Runnable Set_Content =new Runnable() {
        @Override
        public void run() {

        }
    };
    public void getcontent(final String store)
    {
        CONTENTLIST=new ArrayList<String>();
        LABEL=new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";
        System.out.println("段妳妹");
// Request a string response from the provided URL.  http://192.168.1.221/customer/time1.php
      //  StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.137.1/get_store/content.php",
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfig.Spinner_Content,
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
                                CONTENTLIST.add(jsonData.getString("content"));
                                LABEL.add(jsonData.getString("label"));
                            }
                            System.out.println(CONTENTLIST.get(2)+"aafdfdfdfd");
                            System.out.println(LABEL.get(2)+"  lable");
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
                params.put("store",store);
                return params;
            }
        };
        queue.add(stringRequest1);
    }
    private int MIN_MARK = 1;
    private int MAX_MARK = 10;
    private void setRegion( final EditText et)
    {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1)
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK)
                        {
                            s = String.valueOf(MAX_MARK);
                            et.setText("");
                        }
                        else if(num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s)
            {
                if (s != null && !s.equals(""))
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal > MAX_MARK)
                        {
                            Toast.makeText(getBaseContext(), "份數不能超過5", Toast.LENGTH_SHORT).show();
                           // et.setText(String.valueOf(MIN_MARK));
                            et.setText("");
                        }
                        return;
                    }
                }
                else
                { et.setText(String.valueOf(MIN_MARK));return;}
            }
        });
    }
    public void Adapter(Cursor cursor)
    {
        if(cursor!=null&&cursor.getCount()>=0)
        {
            ArrayList<String> get=new ArrayList<>();
            while (cursor.moveToNext())
            {
                get.add(cursor.getString(1)+"\t\t\t\t\t\t\t\t\t"+Integer.toString(cursor.getInt(2)));
                order_list.add(new Get_Order_Infor(cursor.getString(2),cursor.getInt(3)));
            }
            adapter = new ListView_Adapter(this,order_list);
            lv.setAdapter(adapter);
        }
    }
    public void updatetest(Cursor cursor, String dishname,int amount)//按下加入購物車
    {
        int flag=0;
        if(cursor!=null&&cursor.getCount()>=0)
        {
            while (cursor.moveToNext())
            {
                if(cursor.getString(2).toString().equals(dishname))
                {
                    //如果已經有了 就用更新的方式
                    flag=1;
                    break;
                }
            }
            if(flag==1)
                Toast.makeText(this,"您已經點過這樣菜了，請點選下面修改",Toast.LENGTH_SHORT).show();
               // controller.update(chinese.toString(), amount);
            else
                controller.insert(english.toString(),chinese.toString(), amount);
        }

    }
    public void Add_Amount(View view) {
        try {
            if(!amount.getText().toString().equals(""))
            {
                cursor = controller.list_all();
                updatetest(cursor, chinese.toString(), Integer.parseInt(amount.getText().toString()));
                cursor = controller.list_all();
                order_list = new ArrayList<Get_Order_Infor>();
                Adapter(cursor);
            }
            else
            {
                Toast.makeText(this,"數字不可空白",Toast.LENGTH_SHORT).show();
            }

        }catch (SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void load_dialog(int t)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        System.out.print("標標標");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.d("tag","延長兩秒再做");//延長兩秒讓lLIST有時間載入
                progressDialog.dismiss();
                //meal.setAdapter(contentlist);
            }},t);
    }

}
