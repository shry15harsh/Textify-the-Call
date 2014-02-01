<?php
if ($_FILES["file"]["error"] > 0)
  {
  echo "Error: " . $_FILES["file"]["error"] . "<br>";
  }
else
  {
  echo "Upload: " . $_FILES["file"]["name"] . "<br>";
  echo "Type: " . $_FILES["file"]["type"] . "<br>";
  echo "Size: " . ($_FILES["file"]["size"] / 1024) . " kB<br>";
  echo "Stored in: " . $_FILES["file"]["tmp_name"]."\n";
  if (file_exists($_FILES["file"]["name"]))
      {
      echo $_FILES["file"]["name"] . " already exists. ";
      }
    else
      {
      move_uploaded_file($_FILES["file"]["tmp_name"],
      $_FILES["file"]["name"]);
      }
  }
  require 'google_speech.php';

  $s = new cgoogle_speech('AIzaSyBViiWQWIg5QZe3wL6BYhX780LRzkFbnOw'); 
  //$output = shell_exec('./trim.sh');
  //$num=(int) $output;
  //echo $num;
  //echo shell_exec('./trim.sh');
  //echo $output;
  $arr=array();
  $arr['first']=$s->process('@app.flac', 'en-US',8000);
  //print_r($arr);
  echo($arr['first']['0']['alternative']['0']['transcript']);
?>