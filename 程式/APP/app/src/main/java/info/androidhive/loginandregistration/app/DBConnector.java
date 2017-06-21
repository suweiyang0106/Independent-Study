package info.androidhive.loginandregistration.app;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DBConnector {
    public static String executeQuery(String query_string) {
        String result ="";
        HttpURLConnection urlConnection=null;
        InputStream is =null;
        try {
            URL url=new URL("http://192.168.1.213/customer/select.php");          //php的位置
            System.out.print("資料庫要打開");
            urlConnection=(HttpURLConnection) url.openConnection();//對資料庫打開連結
            urlConnection.setConnectTimeout(1000); // millis
            urlConnection.setDoOutput(true);
            System.out.print("連到f之前");
            urlConnection.setRequestMethod("POST");
            System.out.print("連到f之前222");
            urlConnection.connect();//接通資料庫
            System.out.print("連到f之後");
            is=urlConnection.getInputStream();//從database 開啟 stream
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            is.close();
            System.out.print("有連到後已關掉");
            result = builder.toString();
        } catch(Exception e) {
            Log.e("DB 的 log_tag", e.toString());
        }
        return result;
    }
}