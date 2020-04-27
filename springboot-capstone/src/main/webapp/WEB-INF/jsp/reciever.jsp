<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
  
  	<script>
  	var req;

  	function reloadData()
  	{
  	   var now = new Date();
  	   url = 'liveData?' + now.getTime();

  	   try {
  	      req = new XMLHttpRequest();
  	   } catch (e) {
  	      try {
  	         req = new ActiveXObject("Msxml2.XMLHTTP");
  	      } catch (e) {
  	         try {
  	            req = new ActiveXObject("Microsoft.XMLHTTP");
  	         } catch (oc) {
  	            alert("No AJAX Support");
  	            return;
  	         }
  	      }
  	   }

  	   req.onreadystatechange = processReqChange;
  	   req.open("GET", url, true);
  	   req.send(null);
  	}

  	function processReqChange()
  	{
  	   // If req shows "complete"
  	   if (req.readyState == 4)
  	   {
  	      dataDiv = document.getElementById('currentData');

  	      // If "OK"
  	      if (req.status == 200)
  	      {
  	         // Set current data text
  	         dataDiv.innerHTML = req.responseText;

  	         // Start new timer (1 min)
  	         timeoutID = setTimeout('reloadData()', 500);
  	      }
  	      else
  	      {
  	         // Flag error
  	         dataDiv.innerHTML = '<p>There was a problem retrieving data: ' + req.statusText + '</p>';
  	      }
  	   }
  	}

  	</script>
    <title>Apache Spark Reciever</title>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="reloader.js"></script>
  	<style>
		h1 { color: #fff; font-family: 'Righteous', cursive; font-size: 65px; font-weight: normal; line-height: 60px; margin: 10px 0 20px; text-transform: uppercase; text-shadow: 2px 2px 0 #000, margin: 10px 0 24px; text-align: center; } 
	body {
  background: url(bg.png);
	}
	 p { color: #f6f6f6; font-family: 'Orienta', sans-serif; font-size: 25px; line-height: 24px; margin: 0 0 24px; text-align: center; }
	 #currentData { color: #f6f6f6; font-family: 'Orienta', sans-serif; font-size: 16px; line-height: 24px; margin: 0 0 24px; text-align: center; }
  	</style>
  </head>
  
  <body onload="reloadData()">
    <div style="text-align:center">
	    <br/>
	    <h1>Streaming Demo</h1>
	    <p>Message is: </p>
	    <br/><br/>
	     
  	</div>
  	<div id="currentData"> 
  	
  	</div>
  	
  </body>
</html>
