<?php
$host='localhost';//ex:120.105.132.80
$name='root';
$pwd='12345';
$db='test';
$con=mysqli_connect($host,$name,$pwd,$db) or die("connection failed");
if(isset($_POST['order1'])&&isset($_POST['number1']))
{
$account=$_POST['account'];
$order1=$_POST['order1'];
$number1=$_POST['number1'];
$order2=$_POST['order2'];
$number2=$_POST['number2'];
$order3=$_POST['order3'];
$number3=$_POST['number3'];
$order4=$_POST['order4'];
$number4=$_POST['number4'];
$order5=$_POST['order5'];
$number5=$_POST['number5'];
$order6=$_POST['order6'];
$number6=$_POST['number6'];
$order7=$_POST['order7'];
$number7=$_POST['number7'];
$order8=$_POST['order8'];
$number8=$_POST['number8'];
$order9=$_POST['order9'];
$number9=$_POST['number9'];
$order10=$_POST['order10'];
$number10=$_POST['number10'];

$sql="INSERT INTO customertable(
account,order1,number1,complete1,order2,number2,complete2,order3,number3,complete3
	,order4,number4,complete4,order5,number5,complete5,order6,number6,complete6
	,order7,number7,complete7,order8,number8,complete8,order9,number9,complete9
	,order10,number10,complete10)VALUES('$account' ,  '$order1'  ,  '$number1', 0,
		'$order2', '$number2', 0, '$order3', '$number3', 0, '$order4', '$number4', 0,
		'$order5', '$number5', 0, '$order6', '$number6', 0, '$order7', '$number7', 0,
		'$order8', '$number8', 0, '$order9', '$number9', 0, '$order10', '$number10', 0 )";
mysqli_query($con,$sql);
echo json_encode(array('order1'=>$order1 ,'number1'=>$number1));
mysqli_close($con);
}
else{
echo('miss value of account and password!!!!!!');
}
 
?>
