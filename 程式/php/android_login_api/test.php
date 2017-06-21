<?php   
 $dbhost = 'localhost';   
 $dbuser = 'root';   
 $dbpass = '12345';   
 $dbname = 'android_api';   

$number = $_POST['number'];//android將會傳值到number

 $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die('Error with MySQL connection');
  
  mysql_query("SET NAMES 'utf8'");
  mysql_select_db($dbname);   
  $sql = "select * from store1 where number = ".$number;
  $result = mysql_query($sql) or die('MySQL query error');
  
  while($row = mysql_fetch_array($result))
  {
   echo $row['name']." ";
   echo $row['class']."<br>";   
  }
?>