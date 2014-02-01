<?php
$target_path = "uploads/";
if ($_FILES["uploadedfile"]["error"] > 0)
  {
  echo "Error - either not uploaded or ". $_FILES["uploadedfile"]["error"] . "<br>";
  }
else
  {  $target_path = $target_path . basename( $_FILES['uploadedfile']['name']); 
  
      move_uploaded_file($_FILES["uploadedfile"]["tmp_name"],
      $target_path);
  }
  $target_path="uploads/";
  $name=$target_path.basename($_FILES["uploadedfile"]["name"],".amr");
  $word=shell_exec('sox '.$name.'.amr '.$name.'.flac');
  require 'google_speech.php';

  $s = new cgoogle_speech('AIzaSyBViiWQWIg5QZe3wL6BYhX780LRzkFbnOw'); 
  //$word=shell_exec('bash trim.sh '.$name);		//for biggger file size
  $arr=array();
  $data='';
    $arr[$count]=$s->process('@'.$name.'.flac', 'en-US',8000);
    $data=$data.$arr[$count]['0']['alternative']['0']['transcript'];
  file_put_contents($f , $data);
  echo $data;
?>