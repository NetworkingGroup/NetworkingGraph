<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CSC 445:3</title>
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="">
                Networking Group
            </a>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row" style="margin-top: 80px">

        <div id="allmovies" class="col-sm-12">
            <div id="groups" class="row">
                <div><!-- IV4 Results -->
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                Results
                            </div>
                            <div class="panel-body">
                                <div id="pc-to-pi-chart-1"
                                     style="width: 100%; height: 600px; background-color: #FFFFFF;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>

<!--<script src="./445_2/javascripts/lib/jquery.min.js"></script>-->
<script src="https://code.jquery.com/jquery-2.2.3.min.js"
        integrity="sha256-a23g1Nt4dtEYOj7bR+vTu7+T8VP13humZFBJNIYoEJo=" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://www.amcharts.com/lib/3/amcharts.js"></script>
<script type="text/javascript" src="http://cdn.amcharts.com/lib/3/pie.js"></script>
<script type="text/javascript" src="http://www.amcharts.com/lib/3/themes/dark.js"></script>
<script>
    var chart = AmCharts.makeChart("pc-to-pi-chart-1",
            {
                "type": "pie",
                "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
                "titleField": "Category",
                "valueField": "Count",
                "autoMargins": false,
                "marginLeft": 0,
                "marginRight": 0,
                "pullOutRadius": 0,
                "fontSize": 14,
                "theme": "default",
                "allLabels": [],
                "balloon": {},
                "titles": [],
                "dataProvider": []
            }
    );

    function load() {

        $.ajax({
            url: "/data",
            type: "GET",
            dataType: 'json',
            success: function (result) {
                const resData = result["Data"];
                if (resData.length > 0) {
                    chart.dataProvider = resData;
                    chart.validateData();
                } else {
                    chart.dataProvider = [
                        {
                            "Category": "Still No Data Yet",
                            "Count": 100
                        },
                        {
                            "Category": "No Data Yet",
                            "Count": 100
                        }
                    ];
                    chart.validateData();
                }
            },
            error: function (error) {
                console.log(error);
            }
        });

        setTimeout(function () {
            $.ajax({
                url: "/data",
                type: "GET",
                dataType: 'json',
                success: function (result) {
                    const resData = result["Data"];
                    if (resData.length > 0) {
                        chart.dataProvider = resData;
                        chart.validateData();
                    }
                },
                error: function (error) {
                    console.log(error);
                },
                complete: load
            });
        }, 2000);
    }
    load();
</script>
</body>
</html>
