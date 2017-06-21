package info.androidhive.loginandregistration.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.PopulateService;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class ManuTest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button logout;
    private SQLiteHandler db;
    private SessionManager session;
    private Dialog selectdia;
    private  Thread thread;
    private Socket clientSocket;
    private DataInputStream bw;
    private DataOutputStream br;
    private RadioGroup mRadstore;
    private RadioButton mRadstore1,mRadstore2,mRadstore3,mRadstore4;
    private String selectstorename="Store1";
    int num=0;
    String[] selectnum=new String[10];
    String[] selectnum2=new String[10];
    String[] selectnum3=new String[10];
    String[] selectnum4=new String[10];
   public static TextView crowded,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        crowded=(TextView)findViewById(R.id.textView6);

        time=(TextView)findViewById(R.id.textView8);
        setTitle("主選單");

            Intent n = getIntent();
            String mrng = getIntent().getStringExtra("time");
        if(mrng==null)
            time.setText("等候時間");
            else
            time.setText(mrng);
      //  if(mrng.equals(""))
       // {time.setText("等待時間");}
      //  else{time.setText(mrng);}

        db = new SQLiteHandler(getApplicationContext());
        logout=(Button)findViewById(R.id.btn_logout);
        // session manager
        session = new SessionManager(getApplicationContext());
        String account=session.getAccout();
        //Toast.makeText(ManuTest.this,account,Toast.LENGTH_SHORT).show();
        if (!session.isLoggedIn()) {
            logoutUser();
        }
     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView AC=(TextView)header.findViewById(R.id.ACCOUNT);
        AC.setText(account);
       loadpopulation();//先load人潮情況近來

        crowded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadpopulation();
                Intent intent = new Intent(ManuTest.this, PopulateService.class);
                startService(intent);
            }
        });



    }



    public void setText(int time)
    {
        this.time.setText(time);
    }
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(ManuTest.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void logout(View view) {
       logoutUser();
    }
    public void storeorder(View view) {
        Intent intent = new Intent(ManuTest.this, OrderActivity.class);
        startActivity(intent);
    }
    public void situation(View view) {
        selectdia = new Dialog(this);
        selectdia.setTitle("查詢點餐數量");
        selectdia.setCancelable(false);
        selectdia.setContentView(R.layout.selectdialog);
        selectdia.show();
        mRadstore= (RadioGroup) selectdia.findViewById(R.id.radGrpStore);
        mRadstore1=(RadioButton)selectdia.findViewById(R.id.radstore1);
        mRadstore2=(RadioButton)selectdia.findViewById(R.id.radstore2);
        mRadstore3=(RadioButton)selectdia.findViewById(R.id.radstore3);
        mRadstore4=(RadioButton)selectdia.findViewById(R.id.radstore4);
        getstore1number();
        getstore2number();
        getstore3number();
        getstore4number();

    }
    public  void loadpopulation()
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
                            if(items.get(0).toString().equals("very crowded"))
                            {crowded.setText("非常擁擠");}//非常擁擠 very crowd
                            else if(items.get(0).toString().equals("crowded")){ crowded.setText("擁擠");}//擁擠 crowd
                            else if(items.get(0).toString().equals("normal")){ crowded.setText("一般");}// 一般 normal
                            else if(items.get(0).toString().equals("few")){ crowded.setText("鬆散");}//鬆散 few
                            else if(items.get(0).toString().equals("very few")){ crowded.setText("非常鬆散");}//非常鬆散 very few

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
    public void backsitua(View view) {
        selectdia.cancel();
    }
    public void selectsure(View view) {
        switch (mRadstore.getCheckedRadioButtonId()){
            case R.id.radstore1:
                Toast.makeText(ManuTest.this,"店家1\n餐點1有:  "+selectnum[0]+
                                "\n餐點2有: "+selectnum[1]+
                                "\n餐點3有: "+selectnum[2]+
                                "\n餐點4有: "+selectnum[3],
                        Toast.LENGTH_SHORT).show();
                getstore1number();
                break;
            case R.id.radstore2:
                Toast.makeText(ManuTest.this,"店家2\n餐點1有:  "+selectnum2[0]+
                                "\n餐點2有: "+selectnum2[1]+
                                "\n餐點3有: "+selectnum2[2]+
                                "\n餐點4有: "+selectnum2[3],
                        Toast.LENGTH_SHORT).show();
                getstore2number();
                break;
            case R.id.radstore3:
                Toast.makeText(ManuTest.this,"店家3\n餐點1有:  "+selectnum3[0]+
                                "\n餐點2有: "+selectnum3[1]+
                                "\n餐點3有: "+selectnum3[2]+
                                "\n餐點4有: "+selectnum3[3],
                        Toast.LENGTH_SHORT).show();
                getstore3number();
                break;
            case R.id.radstore4:
                Toast.makeText(ManuTest.this,"店家4\n餐點1有:  "+selectnum4[0]+
                                "\n餐點2有: "+selectnum4[1]+
                                "\n餐點3有: "+selectnum4[2]+
                                "\n餐點4有: "+selectnum4[3],
                        Toast.LENGTH_SHORT).show();
                getstore4number();
                break;
        }
    }
    private Runnable Connection=new Runnable() {
        @Override
        public void run() {
            try{
                clientSocket=new Socket(InetAddress.getByName("140.118.134.146"),5052);
                br=new DataOutputStream(clientSocket.getOutputStream());
                bw=new DataInputStream(clientSocket.getInputStream());
                br.writeUTF(selectstorename);
                while(true) {
                    selectnum[num]= bw.readUTF();
                    System.out.println(selectnum[num]);
                    num++;
                    if(num==4){
                        num=0;
                        System.out.println("有結束嗎");
                        break;
                    }
                }
                System.out.println("有結束嗎2");
                clientSocket.close();
            }catch(Exception e){System.out.println("Error"+e);
            }

        }

    };
    public void getstore1number() {
        System.out.println("西西西西");
        RequestQueue queue = Volley.newRequestQueue(this);//requestqueu 給初始值安安安安
        System.out.println("");
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfig.Store1_Number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        GridView gv = (GridView) findViewById(R.id.gridView);
                        ArrayList<String> items = new ArrayList<String>();
                        System.out.println("ㄇㄇㄇ");
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            System.out.println("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for (int j = 0; j < 4; j++) {
                                    selectnum[j] = items.get(j);
                                }
                                System.out.println("店家1: " + selectnum[0]);
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
    }
    public void getstore2number() {
        RequestQueue queue = Volley.newRequestQueue(this);//requestqueu 給初始值
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, AppConfig.Store2_Number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        ArrayList<String> items = new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for (int j = 0; j < 4; j++) {
                                    selectnum2[j] = items.get(j);
                                }
                                System.out.println("店家2: " + items);
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
        queue.add(stringRequest2);
    }
    public void getstore3number() {
        RequestQueue queue = Volley.newRequestQueue(this);//requestqueu 給初始值
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, AppConfig.Store3_Number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        GridView gv = (GridView) findViewById(R.id.gridView);
                        ArrayList<String> items = new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for (int j = 0; j < 4; j++) {
                                    selectnum3[j] = items.get(j);
                                }
                                System.out.println("店家3: " + items);
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
        queue.add(stringRequest3);
    }
    public void getstore4number() {
        RequestQueue queue = Volley.newRequestQueue(this);//requestqueu 給初始值
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, AppConfig.Store4_Number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Toast.makeText(this,"Response is: " + response.substring(0, 500),Toast.LENGTH_SHORT).show();
                        // System.out.println("Response is: " + response.substring(0, 500));
                        GridView gv = (GridView) findViewById(R.id.gridView);
                        ArrayList<String> items = new ArrayList<String>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                items.add(jsonData.getString("order1"));
                                items.add(jsonData.getString("order2"));
                                items.add(jsonData.getString("order3"));
                                items.add(jsonData.getString("order4"));
                                for (int j = 0; j < 4; j++) {
                                    selectnum4[j] = items.get(j);
                                }
                                System.out.println("店家4: " + items);

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
        queue.add(stringRequest4);
    }

    public void foodmateria(View view) {
        /*  Intent intent=new Intent(ManuTest.this,Foodmateria.class);//先留著  因為這網頁webview仔不進來  正常瀏覽器也有限制
            startActivity(intent);*/
        Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fatraceschool.moe.gov.tw/frontend/"));
        startActivity(ie);
    }
    public void select(View view){
        Intent intent=new Intent(ManuTest.this,SelectOrderActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {
            Intent intent = new Intent(ManuTest.this, OrderActivity.class);
            startActivity(intent);
            // Handle the camera action
        }  else if (id == R.id.nav_amount) {
            selectdia = new Dialog(ManuTest.this);
            selectdia.setTitle("查詢點餐數量");
            selectdia.setCancelable(false);
            selectdia.setContentView(R.layout.selectdialog);
            selectdia.show();
            mRadstore= (RadioGroup) selectdia.findViewById(R.id.radGrpStore);
            mRadstore1=(RadioButton)selectdia.findViewById(R.id.radstore1);
            mRadstore2=(RadioButton)selectdia.findViewById(R.id.radstore2);
            mRadstore3=(RadioButton)selectdia.findViewById(R.id.radstore3);
            mRadstore4=(RadioButton)selectdia.findViewById(R.id.radstore4);
        } else if (id == R.id.nav_foodmateria) {
          /*  Intent intent=new Intent(ManuTest.this,Foodmateria.class);//先留著  因為這網頁webview仔不進來  正常瀏覽器也有限制
            startActivity(intent);*/
            Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fatraceschool.moe.gov.tw/frontend/"));
            startActivity(ie);

        }  else if (id == R.id.nav_logout) {
            Toast.makeText(ManuTest.this,"會登出喔",Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
