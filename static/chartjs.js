var CHART = document.getElementById("lineChart").getContext('2d');
project();
function project(){
  let lineChart = new Chart(CHART ,{
    type : 'line',
    data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [{
            label: '# of Votes',
            fill: false,
            lineTension:0.1,
            data: [20,16,19,13,15,22,23],   
            backgroundColor: [
                'rgba(255, 255, 0, 0.2)'
            ],
            borderColor: [
                'rgba(0,0,153,1)'
            ],
            borderWidth: 2
        },
        {
            label: '# of Votes',
            data: [15, 20, 13, 17, 22, 23],
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
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }


});
}
