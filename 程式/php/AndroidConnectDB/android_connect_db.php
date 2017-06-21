<?php
$db = mysql_pconnect("localhost","user","pwd");

mysql_query("SET CHARACTER SET 'UTF8';");
mysql_query('SET NAMES UTF8;');
mysql_query('SET CHARACTER_SET_CLIENT=UTF8;');
mysql_query('SET CHARACTER_SET_RESULTS=UTF8;');
mysql_select_db("database");

$sql = $_POST['query_string'];
$sql = stripslashes($sql);
$res = mysql_query($sql);
while($r = mysql_fetch_assoc($res))
    $output[] = $r;

print(json_encode($output));

mysql_close();

?>