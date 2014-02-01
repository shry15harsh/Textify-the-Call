<? 
require 'google_speech.php';

$s = new cgoogle_speech('AIzaSyBViiWQWIg5QZe3wL6BYhX780LRzkFbnOw'); 
//$audio=fopen("/home/harsh/upload/app.flac","r");
$arr=array();
//$arr =array('1'=>$s->process('@app.flac', 'en-US',8000);,'2'=>$s->process('@app.flac', 'en-US',8000););
$arr['first']=$s->process('@app.flac', 'en-US',8000);
//$arr['second']=$s->process('@app.flac', 'en-US',8000);
//$output = $s->process('@hello.flac', 'en-US',44100);      

print_r($arr);
//print_r($arr['first']['0']['alternative']['0']['transcript']);
?> 
