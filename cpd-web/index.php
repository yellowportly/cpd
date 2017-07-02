<?php
$db = new SQLite3('/home/chip/cpd.db');

$results = $db->query("SELECT id, action, value, date FROM actions WHERE action = '/cpd/dht/temp' GROUP BY SUBSTR(date,1, 13) ORDER BY date ASC");
$dates = "";
$vals = "";
$colours = "";
$col = "";

while ($row = $results->fetchArray()) {
        if ($row['value'] > -20) {
                if (strlen($vals) > 0) {
                        $vals = $vals . ", ";
                }
                if (strlen($dates) > 0) {
                        $dates = $dates . ", ";
                }
                if (strlen($colours) > 0) {
                        $colours = $colours . ", ";
                }
                if ($row['value'] > 20) {
                        $col = "rgba(255,99,132,0.5)";
                } else if ($row['value'] > 15) {
                        $col = "rgba(255,159,64,0.5)";
                } else if ($row['value'] > 10) {
                        $col = "rgba(54,162,235,0.5)";
                }

                $dates = $dates . ("\"" . $row['date'] . "\"");
                $vals = $vals . ("\"" . $row['value'] . "\"");
                $colours = $colours . ("\"" . $col . "\"");
        }
}
?><html>
<head>
  <script src='./js/Chart.bundle.js' type='text/javascript'></script>
</head>
<body>
  <canvas id="myChart" width="400" height="400"></canvas>
<script>
var ctx = document.getElementById("myChart");
var myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: [ <?php echo $dates; ?> ],

        datasets: [{
            label: 'Temperature',
            backgroundColor: [ <?php echo $colours; ?> ],
            data: [ <?php echo $vals; ?>],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
</script>
</body>
</html>
