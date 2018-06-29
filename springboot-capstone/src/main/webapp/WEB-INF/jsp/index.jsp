<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width , initial-scale=1.0" >
    <meta name="description" content="Handicapped Assistance">
    <meta name="author" content="NONE">
    <title>Spark Analysis</title>
    <link href="{{ url_for('static',filename='styles/bootstrap.min.css') }}" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="{{ url_for('static', filename='bootstrap.min.js')}}"></script>  
    <!-- <link href="main_css.css" type="text/css" rel="stylesheet"></link> -->
	<style type="text/css">
  .wrapper {
    text-align: center;
  }
  .button{
    /*position: absolute;*/
    top:50%;
  }
  .drop{
    text-align: center;
  }
  canvas {
    padding: 0;
    margin: auto;
    display: block;
    width: 800px;
}
button {
  margin:5em;
  float:right;
}
</style>

    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/0.9.16/socket.io.min.js"></script>
    <script type="text/javascript" charset="utf-8">
        $(document).ready(function(){
            namespace = ''; // change to an empty string to use the global namespace

            // the socket.io documentation recommends sending an explicit package upon connection
            // this is specially important when using the global namespace
            var socket = io.connect('http://' + 'localhost' + ':' + '8080' + namespace);
            
            socket.on('connect', function(msg) {
                socket.emit('my event', {data: 'I\'m connected!'});
            });

            var x = '';
            socket.on('message', function(msg){
                console.log(msg.time)
                x = x + '<p>' + msg + '</p>';
                $('#test').html(x);             
            });
          
        });
    </script>
</head>
<body onload="project()">
 <div class="container-fluid">
    <!-- FOR NAVBAR -->
      <nav class="navbar navbar-inverse navbar-fixed-top" style="margin-bottom: 0px">
          <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Spark Analysis</a>
            </div>
          </div>	
      </nav>  
	</div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.js" type="text/javascript"></script>
    <script src="https://code.jquery.com/jquery-1.10.2.js"></script>


    <div class="container-fluid">
      <div class="row">
        <div>
          <canvas id="lineChart" width="900px" height="500px"></canvas>
        </div>
      </div>
    </div>  

    <form method="POST" action="/analyse">
    <button type="submit" class="btn btn-primary">Analyse</button>
    </form>
    <script>
   
    function project(){
    var self = this;
    var CHART = document.getElementById("lineChart").getContext('2d');
      var lineChart = new Chart(CHART ,{
        type : 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'NSE Close Price',
                fill: false,
                lineTension:0.1,
                data: [],  
                backgroundColor: [
                    'rgba(255, 255, 0, 0.2)'
                ],
                borderColor: [
                    'rgba(0,0,153,1)'
                ],
                borderWidth: 2
            },
            // },
            {
                label: 'Moving Average',
                data: [],
                fill:false,
                lineTension:0.1,   
                backgroundColor: [
                    'rgba(255, 102, 102, 0.2)'
                ],
                borderColor: [
                    'rgba(255,0,0,1)'
                ],
                borderWidth: 1

            }
            ]
        },
        options: {
            responsive : false ,
            animation : false,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }


    });

       namespace = '/test'; // change to an empty string to use the global namespace

                // the socket.io documentation recommends sending an explicit package upon connection
                // this is specially important when using the global namespace
                var socket = io.connect('http://' + 'localhost' + ':' + '5000' + namespace);
               
                socket.on('connect', function(msg) {
                    socket.emit('my event', {data: 'I\'m connected!'});
                });

                var x = '';
                socket.on('message', function(msg){
                    x = msg.time+'';
                    console.log(x);
                    var splits = x.split(",");
                    lineChart.data.datasets[0].data.push(parseFloat(splits[1]))//addData([12] , splits[0]+"" )
                    lineChart.data.labels.push(splits[0]+"")//addData([12] , splits[0]+"" )
                    if(count>12)
                    {
                    // lineChart.data.datasets[0].data.pop()//addData([12] , splits[0]+"" )
                    // lineChart.data.labels.pop()//addData([12] , splits[0]+"" ) 
                    lineChart.data.labels.shift();
                    lineChart.data.datasets[0].data.shift();
                    }
                    count = count + 1;
                    // lineChart.addData([(splits[1]).toString(10)] , splits[0]+"" )
                    // self.lineChart().data
                    lineChart.update();
                            
                });

                socket.on('float_value', function(msg){
                    var count = 0;
                  
                    value = msg.movingavg+''
                    if( (value) != 'undefined')
                    {  
                        var splits = value.split(" ");
                       
                        lineChart.data.datasets[1].data.push(parseFloat(value))
                        // lineChart.data.labels.push(splits[0]+"")//addData([12] , splits[0]+"" )

                       
                        console.log(''+msg.movingavg+'');
                   
                    if(count>12)
                    {
                    // lineChart.data.datasets[0].data.pop()//addData([12] , splits[0]+"" )
                    // lineChart.data.labels.pop()//addData([12] , splits[0]+"" ) 
                    lineChart.data.labels.shift();
                    lineChart.data.datasets[1].data.shift();
                    }
                    count = count + 1;
                    }
                    lineChart.update();


                });
    }
    </script>

  </body>

    <p id='test'></p>
</html>

