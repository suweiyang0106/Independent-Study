<?php
$host='localhost';//ex:120.105.132.80
$name='root';
$pwd='12345';
$db='test';
$con=mysqli_connect($host,$name,$pwd,$db) or die("connection failed");
if(isset($_POST['account'])&&isset($_POST['password']))
{
$account=$_POST['account'];
$password=$_POST['password'];
$sql="INSERT INTO user VALUES(10,  '$account'  ,  '$password' )";
mysqli_query($con,$sql);
echo json_encode(array('account'=> $account ,'password'=>$password));
mysqli_close($con);
}
else{
echo('miss value of account and password!!!!!!');
}
 
?>
