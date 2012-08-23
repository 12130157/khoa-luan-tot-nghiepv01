/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var JSON_CALLBACK_URL = 'http://localhost:8080/KhoaLuanTotNghiep/RegistryController?jsoncallback=?';
function bindSupportEvent(subjectId, lecturerId) {
    //var loadListStudent = true; // This load by default
    var loadListPreSubject = false;
    var loadSubjectHistory = false;
    var loadLecturerHistory = false;
    var lastDisplay = $('#lst-student-reg');
    // Bind event
   $('#hide-support').live('click', function(){
       $('#tbl-support-control span').removeClass('option-selected');
       $('#lst-reg-student span').addClass('option-selected');
       $('.support').hide();
       $('#lst-student-reg').show();
       lastDisplay = $('#lst-student-reg');
       hideMe('popup-reg-support');
   });
   $('#lst-reg-student').live('click', function() {
       //$('#popup-reg-support .support').fadeOut('fast');
       if (lastDisplay != null)
           $(lastDisplay).fadeOut('fast');
       $('#lst-student-reg').fadeIn('slow');
       lastDisplay = $('#lst-student-reg');
       return;
   });
   $('#lst-pre-subject').live('click', function() {
       if (!loadListPreSubject) {
           doLoadListPreSubject(subjectId);
           loadListPreSubject = true;
       }
       
       //$('#popup-reg-support .support').fadeOut('fast');
       if (lastDisplay != null)
           $(lastDisplay).fadeOut('fast');
       $('#lst-pre-subject').fadeIn('slow');
       lastDisplay = $('#lst-pre-subject');
       return;
   });
   $('#subject-history').live('click', function() {
       //test();
       if (!loadSubjectHistory) {
           doLoadSubjectHistory(subjectId);
           loadSubjectHistory = true;
       }
       //$('#popup-reg-support .support').fadeOut('fast');
       if (lastDisplay != null)
           $(lastDisplay).fadeOut('fast');
       $('#chart-subject').fadeIn('slow');
       lastDisplay = $('#chart-subject');
       return;
   });
   $('#subject-lecturer-history').live('click', function() {
       if (!loadLecturerHistory) {
           doLoadLecturerHistory(lecturerId);
           loadLecturerHistory = true;
       }
       //$('#popup-reg-support .support').fadeOut('fast');
       if (lastDisplay != null)
           $(lastDisplay).fadeOut('fast');
       $('#chart-lecturer-subject').fadeIn('slow');
       lastDisplay = $('#chart-lecturer-subject');
       return;
   });

   $('#tbl-support-control .atag1').live('click', function() {
       $('#tbl-support-control .atag1').removeClass('option-selected');
       if ($(this).attr('id') != 'hide-support')
           $(this).addClass('option-selected');
       return;
   });
   $('#select-year').live('change', function() {
       doLoadLecturerHistory(lecturerId);
   })
}

function doLoadListPreSubject(subjectId) {
   var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doLoadListPreSubject",
        data : subjectId
   });
   
   if (json != null) {
       //fill list pre subject
       var ulPreSub = $('#lst-pre-subject .support-data ul');
       for (var i = 0; i < json.length; i++) {
           $("<li>"+ (i+1)+" - ["+ json[i].id + "] "+ json[i].name +"</li>").appendTo(ulPreSub);
       }
   }
   
}

function doLoadSubjectHistory(subjectId) {
    var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doLoadSubjectHistory",
        data : subjectId
   });
   if (json != null) {
        drawSubjectChart(json);
   } else {
       console.log("No data received");
   }
}

function doLoadLecturerHistory(lecturerId) {
    // get selected year
    var year = $('#select-year').val();
    console.log('Year selected: ' +year);
    
    // search cac mon do GV day nam gan nhat de ve bieu do
    var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doLoadLecturerHistory",
        year : year,
        data : lecturerId
   });
   if (json != null) {
        drawLecturerChart(json);
   } else {
       console.log("No data received");
   }
}

var subHistoryChart = new RGraph.Line('sub_history_chart', []);
var lecturerHistory = new RGraph.Line('lecturer_chart', []);
var chartSetup = false;
function setupChart() {
    subHistoryChart.Set('chart.background.barcolor1', 'white');
    subHistoryChart.Set('chart.background.barcolor2', 'white');
    subHistoryChart.Set('chart.background.grid.color', 'rgba(238,238,238,1)');
    subHistoryChart.Set('chart.colors', ['#666']);
    subHistoryChart.Set('chart.linewidth', 2);
    subHistoryChart.Set('chart.filled', false);
    subHistoryChart.Set('chart.hmargin', 10);
    subHistoryChart.Set('chart.ymax', 100);
    subHistoryChart.Set('chart.labels.above', true);
    //subHistoryChart.Set('chart.labels.ingraph', toolTips);
    subHistoryChart.Set('chart.title.xaxis', 'Học kỳ / năm học');
    subHistoryChart.Set('chart.title.yaxis', 'tỉ lệ đậu (%)');
    subHistoryChart.Set('chart.gutter.bottom', 50);
    subHistoryChart.Set('chart.gutter.left', 50);
    
    lecturerHistory.Set('chart.background.barcolor1', 'white');
    lecturerHistory.Set('chart.background.barcolor2', 'white');
    lecturerHistory.Set('chart.background.grid.color', 'rgba(238,238,238,1)');
    lecturerHistory.Set('chart.colors', ['#666']);
    lecturerHistory.Set('chart.linewidth', 2);
    lecturerHistory.Set('chart.filled', false);
    lecturerHistory.Set('chart.hmargin', 10);
    lecturerHistory.Set('chart.ymax', 100);
    lecturerHistory.Set('chart.labels.above', true);
    //lecturerHistory.Set('chart.labels.ingraph', toolTips);
    lecturerHistory.Set('chart.title.xaxis', 'năm học');
    lecturerHistory.Set('chart.title.yaxis', 'tỉ lệ đậu (%)');
    lecturerHistory.Set('chart.gutter.bottom', 50);
    lecturerHistory.Set('chart.gutter.left', 50);
}
function drawSubjectChart(json) {
    var data = [];
    var chartLabels = [];
    var toolTips = [];
    for (var i = 0; i < json.length; i++) {
        data[i] = json[i].passInPercent;
        chartLabels[i] = json[i].semeter +"/"+json[i].year;
        toolTips[i] = json[i].lecturer;
    }
    clearCanvase('sub_history_chart');
    drawChart(subHistoryChart, data, chartLabels, toolTips);
}

function drawLecturerChart(json) {
    var data = [];
    var chartLabels = [];
    var toolTips = [];
    for (var i = 0; i < json.length; i++) {
        data[i] = json[i].passInPercent;
        chartLabels[i] = json[i].year;
        toolTips[i] = json[i].subjectName;
    }
    clearCanvase('lecturer_chart');
    drawChart(lecturerHistory, data, chartLabels, toolTips);
}

function clearCanvase(canvasId) {
    // Clear canvas
    var c = document.getElementById(canvasId);
    var ctx = c.getContext("2d");
    ctx.beginPath();
    ctx.rect(0, 0, c.width, c.height);
    ctx.fillStyle = '#ddf';
    ctx.fill();
}

function drawChart(lineChart, data, chartLabels, toolTips) {
    if (chartSetup == false) {
        setupChart();
        chartSetup = true;
    }
    // Create the Line chart object. The arguments are the canvas ID and the data array.
    //var line = new RGraph.Line(canvasId, data);

    // Configure the chart to appear as you wish.
    
    lineChart.properties['chart.labels'] = chartLabels;
    //lineChart.properties['chart.labels'] = chartLabels;
    lineChart.properties['chart.labels.ingraph'] = toolTips;
    lineChart.reSetData(data);
    //line.Set('chart.labels', chartLabels);
    //line.Set('chart.axesontop', true);
    //
    // Now call the .Draw() method to draw the chart.
    //RGraph.Redraw();
    lineChart.Draw();
}

function getJSON_SYNC(url, data) {
    var result;
    $.ajax({
        url : url,
        dataType : 'json',
        data : data,
        async : false,
        success : function(json) {				
            result = json;
        },
        error: function(request,status,errorThrown) {
            console.log("Request: " + request + 
                "; status: " + status + 
                "; Error thrown: " + errorThrown)
        }
    });
    return result;
}

function test() {
    var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "test",
        data : "data"
    });
    if (json == null) {
        console.log('No data return');
    } else {
        console.log('OK');
    }
    return json;
}
