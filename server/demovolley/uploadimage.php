<?php
mysql_connect("localhost","root","");
mysql_select_db("demovolley");

$ten = $_POST["TEN"];
$hinh = $_POST["HINH"];

$path = "upload/$ten.png";

$url = "http://192.168.1.100/demovolley/$path";

$qr = "INSERT INTO image VALUES(null,'$ten','$url')";

if(mysql_query($qr)){
  file_put_contents($path,base64_decode($hinh));
}else{
  echo "Loi";
}

?>
