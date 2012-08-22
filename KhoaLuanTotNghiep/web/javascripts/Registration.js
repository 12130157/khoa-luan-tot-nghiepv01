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
    // Bind event
   $('#hide-support').bind('click', function(){
       hideMe('popup-reg-support');
   });
   $('#lst-reg-student').live('click', function() {
       $('#popup-reg-support .support').fadeOut('fast');
       $('#lst-student-reg').fadeIn('slow');
   });
   $('#lst-pre-subject').live('click', function() {
       if (!loadListPreSubject) {
           doLoadListPreSubject(subjectId);
           loadListPreSubject = true;
       }
       $('#popup-reg-support .support').fadeOut('fast');
       $('#lst-pre-subject').fadeIn('slow');
   });
   $('#subject-history').live('click', function() {
       //test();
       if (!loadSubjectHistory) {
           doLoadSubjectHistory(subjectId);
           loadSubjectHistory = true;
       }
       $('#popup-reg-support .support').fadeOut('fast');
       $('#chart-subject').fadeIn('slow');
   });
   $('#subject-lecturer-history').live('click', function() {
       if (!loadLecturerHistory) {
           doLoadLecturerHistory(lecturerId);
           loadLecturerHistory = true;
       }
       $('#popup-reg-support .support').fadeOut('fast');
       $('#chart-lecturer-subject').fadeIn('slow');
   });

   $('#tbl-support-control .atag1').live('click', function() {
       $('#tbl-support-control .atag1').removeClass('option-selected');
       $(this).addClass('option-selected');
   });
}

function doLoadListPreSubject(subjectId) {
   var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doLoadPreSubject",
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
    var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doLoadLecturerHistory",
        data : lecturerId
   });
   if (json != null) {
        drawLecturerChart(json);
   } else {
       console.log("No data received");
   }
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
    drawChart("sub_history_chart", data, chartLabels, toolTips);
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
    drawChart("lecturer_chart", data, chartLabels, toolTips);
}
function drawChart(canvasId, data, chartLabels, toolTips) {
    // Create the Line chart object. The arguments are the canvas ID and the data array.
    var line = new RGraph.Line(canvasId, data);

    // Configure the chart to appear as you wish.
    line.Set('chart.background.barcolor1', 'white');
    line.Set('chart.background.barcolor2', 'white');
    line.Set('chart.background.grid.color', 'rgba(238,238,238,1)');
    line.Set('chart.colors', ['#666']);
    line.Set('chart.linewidth', 2);
    line.Set('chart.filled', false);
    line.Set('chart.hmargin', 10);
    line.Set('chart.ymax', 100);
    line.Set('chart.labels.above', true);
    line.Set('chart.labels.ingraph', toolTips);
    line.Set('chart.title.xaxis', 'Học kỳ / năm học');
    line.Set('chart.title.yaxis', 'tỉ lệ đậu (%)');
    line.Set('chart.gutter.bottom', 50);
    line.Set('chart.gutter.left', 50);
    line.Set('chart.labels', chartLabels);
    
    // Now call the .Draw() method to draw the chart.
    line.Draw();
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
