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
    // Clear canvas
    /*var c = document.getElementById(canvasId);
    var ctx = c.getContext("2d");
    ctx.beginPath();
    ctx.rect(0, 0, c.width, c.height);
    ctx.fillStyle = '#ddf';
    ctx.fill();
    */
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
    line.Set('chart.axesontop', true);
    //
    // Now call the .Draw() method to draw the chart.
    //RGraph.Redraw();
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
