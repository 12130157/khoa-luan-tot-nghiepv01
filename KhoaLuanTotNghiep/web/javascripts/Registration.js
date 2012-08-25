/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var JSON_CALLBACK_URL = 'http://' + Config.serverAddress + ':8080/KhoaLuanTotNghiep/RegistryController?jsoncallback=?';
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

/**
*   Update status (number student registrated) for each trainclass
* This should be call every 10 s
* {TrainClass}: tcs list trainclass need to update
* tcs[i]: [tcId, semester, year]
* 
* Return: tcsStatus as an array
* tcsStatus[i]: [tcId, semester, year, numberRegistrated]
*/
function updateNumberRegistrationStatus(tcs) {
   var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doUpdateNumberRegistrated",
        data : JSON.stringify(tcs) // an json array, share be parse at server side
   });
   
   if (json != null) {
       updateNumberRegistratedDisplay(json);
       console.log('Data recived' + json);
   } else {
       console.log('No data received');
   }
}

/**
 * After data received from server
 * the page should be updated
 */
function updateNumberRegistratedDisplay(json) {
   var row1 = $('#detail tr:gt(0)');
   var row2 = $('#ext-detail tr:gt(0)');
   var rows = $.merge(row1, row2);
   for (var i = 0; i < json.length; i++) {
       for (var j = 0; j < rows.length; j++) {
       //rows.each(function() {
           var selectTc = {
                tcId: '',
                semester: 0,
                year: ''
            };
        
            // find data on each row
            //selectTc.tcId = $(this).find('.tcId').val();
            selectTc.tcId = $(rows[j]).find('.tcId').val();
            selectTc.semester = $(rows[j]).find('.semester').val();
            selectTc.year = $(rows[j]).find('.year').val();
        
            if ((json[i].tcId == selectTc.tcId)
                    && (json[i].semester == selectTc.semester)
                    && (json[i].year == selectTc.year)) {
                var newValue = json[i].registrated;
                $(rows[j]).find('.current_number_reg').html(newValue);
                
                // if max --> disable checkbox (do if and only if I've not registered this yes)
                var maxNumber = $(rows[j]).find('.number_max').html();
                if (isTCRegByMe(selectTc.tcId) == false) {
                    if (newValue >= maxNumber) {
                        $(rows[j]).find('input:checkbox').attr("checked", false);
                        $(rows[j]).find('input:checkbox').attr("disabled", true);
                    } else {
                        $(rows[j]).find('input:checkbox').attr("disabled", false);
                    }
                }
                
                break;
            }
       }
   }
}

function isTCRegByMe(tc) {
    var myListStr = $('#my_list').val();
    if (myListStr == null || myListStr.length==0) {
        return false;
    }
    var length = myListStr.length;
    myListStr = myListStr.substring(1, length-1);
    var myList = myListStr.split(',');
    if (myList != null && myList.length > 0) {
        for (var i = 0; i < myList.length; i++) {
            if (myList[i].trim() == tc) {
                return true;
            }
        }
    }
    return false;
}

/**
 * Retrieved current number registrated status
 */
function getNumberRegistrated() {
    var jsons = new Array();
    // Find on main table
    //<input type="hidden" class="tcId" value="<%=tc.getId().getClassCode() %>"/>
    //<input type="hidden" class="semester" value="<%=tc.getId().getSemester() %>"/>
    //<input type="hidden" class="year" value="<%=tc.getId().getYear() %>"/>
    var row1 = $('#detail tr:gt(0)');
    var row2 = $('#ext-detail tr:gt(0)');
    var rows = $.merge(row1, row2);
    var count = 0;
    rows.each(function() {
        var tc = {
          tcId: '',
          semester: 0,
          year: ''
        };
        
        // find data on each row
        tc.tcId = $(this).find('.tcId').val();
        tc.semester = $(this).find('.semester').val();
        tc.year = $(this).find('.year').val();
        
        jsons[count] = tc;
        count++;
    });
    
    return jsons;
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
    subHistoryChart.Set('chart.units.post', '%');
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
    lecturerHistory.Set('chart.units.post', '%');
    //lecturerHistory.Set('chart.labels.ingraph', toolTips);
    lecturerHistory.Set('chart.title.xaxis', 'môn học');
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
        chartLabels[i] = json[i].subjectName;
        //toolTips[i] = json[i].subjectName;
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
    lineChart.reSetData(false, data);
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
