<?php
$host='localhost';//ex:120.105.132.80
$name='root';
$pwd='12345';
$db='test';
$con=mysqli_connect($host,$name,$pwd,$db) or die("connection failed");
//mysql_select_db($db,$con) //or die("db selection failed");
$result=mysqli_query($con,"SELECT account,order1,number1,complete1,order2,number2,complete2,order3,number3,complete3,order4,
number4,complete4,order5,number5,complete5,order6,number6,complete6,order7,number7,complete7,order8,number8,complete8,order9,number9,complete9,order10,number10,complete10  FROM customertable");//user¦WºÙ
 while($row=mysqli_fetch_assoc($result)){
 $tmp[]=$row;
 }
 echo json_encode($tmp);
 mysqli_close($con);
?>
