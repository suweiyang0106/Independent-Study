<?php
$host='localhost';//ex:120.105.132.80
$name='root';
$pwd='12345';
$db='test';
$con=mysqli_connect($host,$name,$pwd,$db) or die("connection failed");
//mysql_select_db($db,$con) //or die("db selection failed");
mysqli_query($con,'SET NAMES utf8');//網站上看亂碼，但轉過去是有的
if(isset($_POST['account']))
{
$account=$_POST['account'];
$result=mysqli_query($con,"SELECT 
(select trans1 from content_trans where content_trans.order1=(select order1 FROM customertable WHERE account='$account' AND customertable.id = (SELECT MAX(customertable.id) as a FROM customertable Where account='$account'))) as 'A',
number1,
complete1,
(select trans2 from content_trans where content_trans.order2=(select order2 FROM customertable WHERE account='$account' AND customertable.id = (SELECT MAX(customertable.id) as a FROM customertable Where account='$account'))) as 'B',
number2,
complete2,
(select trans3 from content_trans where content_trans.order3=(select order3 FROM customertable WHERE account='$account' AND customertable.id = (SELECT MAX(customertable.id) as a FROM customertable Where account='$account'))) as 'C',
number3,
complete3,
(select trans4 from content_trans where content_trans.order4=(select order4 FROM customertable WHERE account='$account' AND customertable.id = (SELECT MAX(customertable.id) as a FROM customertable Where account='$account'))) as 'D',
number4,
complete4,
(select trans5 from content_trans where content_trans.order5=(select order5 FROM customertable WHERE account='$account' AND customertable.id = (SELECT MAX(customertable.id) as a FROM customertable Where account='$account'))) as 'E',
number5,
complete5,
order6
FROM customertable WHERE account='$account' AND customertable.id = (SELECT MAX(customertable.id) FROM customertable Where account='$account' )");//user名稱
 while($row=mysqli_fetch_assoc($result)){
 $tmp[]=$row;
 }
 echo json_encode($tmp);
 mysqli_close($con);
}
else
{
echo("miss account");
}
?>
