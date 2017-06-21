package info.androidhive.loginandregistration.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class ManueActivity extends AppCompatActivity {
    private Button logout;
    private SQLiteHandler db;
    private SessionManager session;
    private String[] select={"Store1","Store2","Store3","Store4"};
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
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        logout=(Button)findViewById(R.id.btn_logout);
        // session manager
        session = new SessionManager(getApplicationContext());
        String account=session.getAccout();
        Toast.makeText(ManueActivity.this,account,Toast.LENGTH_SHORT).show();
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(ManueActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void logout(View view) {
      logoutUser();
    }
    public void storeorder(View view) {
       // Intent intent = new Intent(ManueActivity.this, StoreOrderActivity.class);
        Toast.makeText(this,"膽點點",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ManueActivity.this, OrderActivity.class);
        startActivity(intent);
    }
    public void situation(View view) {
        selectdia = new Dialog(ManueActivity.this);
        selectdia.setTitle("查詢點餐數量");
        selectdia.setCancelable(false);
        selectdia.setContentView(R.layout.selectdialog);
        selectdia.show();
        mRadstore= (RadioGroup) selectdia.findViewById(R.id.radGrpStore);
        mRadstore1=(RadioButton)selectdia.findViewById(R.id.radstore1);
        mRadstore2=(RadioButton)selectdia.findViewById(R.id.radstore2);
        mRadstore3=(RadioButton)selectdia.findViewById(R.id.radstore3);
        mRadstore4=(RadioButton)selectdia.findViewById(R.id.radstore4);

      //thread=new Thread(Connection);
      // thread.start();
    }
    public void backsitua(View view) {
        selectdia.cancel();
    }
    public void selectsure(View view) {
        switch (mRadstore.getCheckedRadioButtonId()){
            case R.id.radstore1:
                selectstorename="Store1";
                break;
            case R.id.radstore2:
                selectstorename="Store2";
                break;
            case R.id.radstore3:
                selectstorename="Store3";
                break;
            case R.id.radstore4:
                selectstorename="Store4";
                break;
        }
        thread=new Thread(Connection);
        thread.start();

        try {
            thread.join();
            thread.wait(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Toast.makeText(ManueActivity.this,"order1有:  "+selectnum[0]+
                        "\norder2有: "+selectnum[1]+
                "\norder3有: "+selectnum[2]+
                "\norder4有: "+selectnum[3],
                Toast.LENGTH_SHORT).show();
    }
    private Runnable Connection=new Runnable() {
        @Override
        public void run() {
            try{
                System.out.println("有結束嗎122");
                    clientSocket=new Socket(InetAddress.getByName("192.168.1.221"),5052);
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
    public void foodmateria(View view) {
        Intent intent=new Intent(ManueActivity.this,Foodmateria.class);
        startActivity(intent);
    }
    public void select(View view){
        Intent intent=new Intent(ManueActivity.this,SelectOrderActivity.class);
        startActivity(intent);
    }


}
