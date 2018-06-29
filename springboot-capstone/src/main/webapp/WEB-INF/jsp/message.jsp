<html>
  <head>
 	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <title>Real time data processing</title>
  </head>
  <style>
	h1 { color: #000; font-family: 'Righteous', cursive; font-size: 65px; font-weight: normal; line-height: 60px; margin: 10px 0 20px; text-transform: uppercase; text-shadow: 2px 2px 0 #000, margin: 10px 0 24px; text-align: center; } 
	
  </style>
  <body>
    <div style="text-align:center">
	    <br/>
	    <h1 class="container-fluid bg-2 text-center">Streaming Demo</h1>
	    <br/><br/>
	    <form action="/cam-kafka/producer" method="GET">
	      <input type="text" id="message" name="message" /> &nbsp 
	      <input type="submit"class="btn btn-success" value="Submit" />
	    </form>
	    <br/>
	     <a href="/cam-kafka/startTxt" class="btn btn-info" role="button">Start streaming from text file</a>
	     <br/><br/>
	     <a href="/cam-kafka/startXlsx" class="btn btn-info" role="button">Start streaming from excel file</a>
	     <br/><br/>
	     <a href="/cam-kafka/startTwitter" class="btn btn-info" role="button">Start streaming from Twitter</a> 
	     <br/><br/>  
	     <a href="/cam-kafka/startAlphaVantage" class="btn btn-info" role="button">Start streaming Microsoft data from AlphaVantage</a> 
  	</div>
  </body>
</html>