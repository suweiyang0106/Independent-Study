<?php
$host='localhost';//ex:120.105.132.80
$name='root';
$pwd='12345';
$db='android_api';
$con=mysqli_connect($host,$name,$pwd,$db) or die("connection failed");
//mysql_select_db($db,$con) //or die("db selection failed");
$result=mysqli_query($con,"SELECT order1,order2,order3,order4  FROM store2");//user¦WºÙ
 while($row=mysqli_fetch_assoc($result)){
 $tmp[]=$row;
 }
 echo json_encode($tmp);
 mysqli_close($con);
?>
