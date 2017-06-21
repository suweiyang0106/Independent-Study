<?php
$host='localhost';//ex:120.105.132.80
$name='root';
$pwd='12345';
$db='test';
$con=mysqli_connect($host,$name,$pwd,$db) or die("connection failed");
if(isset($_POST['store']))
{
$store=$_POST['store'];
$sql="SELECT content,label FROM store_content Where store='$store'";
mysqli_query($con,'SET NAMES utf8');//網站上看亂碼，但轉過去是有的
$result=mysqli_query($con,$sql);
 while($row=mysqli_fetch_assoc($result)){
 $tmp[]=$row;
 }
 echo json_encode($tmp);
 mysqli_close($con);
}
else
{
echo("miss value of store");
}
?>
