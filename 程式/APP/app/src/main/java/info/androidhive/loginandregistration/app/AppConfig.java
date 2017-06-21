package info.androidhive.loginandregistration.app;

public class AppConfig {
	public static String ip="140.118.134.107";
	// Server user login url
	public static String URL_LOGIN = "http://"+ip+"/android_login_api/login.php";
	// Server user register url
	public static String URL_REGISTER = "http://"+ip+"/android_login_api/register.php";
	//下拉式選單選店家
	public static String Spinner_Store="http://"+ip+"/get_store/store.php";
	//下拉式選單選餐點
	public static String Spinner_Content="http://"+ip+"/get_store/content.php";
	//推播
	public static String BroadCast="http://"+ip+"/customer/select3.php";
	//建表格
	public static String SetTable="http://"+ip+"/customer/insert.php";
	//送至server
	public static String Connection=ip;
	//得知點餐數量(大家的)
	public static String Store1_Number="http://"+ip+"/customer/time1.php";
	public static String Store2_Number="http://"+ip+"/customer/time2.php";
	public static String Store3_Number="http://"+ip+"/customer/time3.php";
	public static String Store4_Number="http://"+ip+"/customer/time4.php";
	//得知自己數量
	public static  String Self_Order="";
	//得知人潮情況
	public static String population="http://"+ip+"/population_situation/population.php";
}
