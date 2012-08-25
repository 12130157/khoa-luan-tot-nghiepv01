/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var JSON_CALLBACK_URL = 'http://' + Config.serverAddress + ':8080/KhoaLuanTotNghiep/ReportController?jsoncallback=?';
function bindSupportEvent() {
}

/**
 * time:    hky1: 1
 *          hky2: 2
 *          hky3: 3
 *          Canam: 0
 *          
 */
function doLoadReportPassFailData(startYear, endYear, time) {
   var json = getJSON_SYNC(JSON_CALLBACK_URL, {
        action : "doGetReportPassFailData",
        startYear : startYear,
        endYear : endYear,
        time: time
   });
   /**
    * json object: an array
    * [
    *   startYear
    *   numberYear
    *   [year1, cnpm.pass, mmt.pass, ktmt.pass, khmt.pass, httt.pass]
    *   [year2, cnpm.pass, mmt.pass, ktmt.pass, khmt.pass, httt.pass]
    *   [year3, cnpm.pass, mmt.pass, ktmt.pass, khmt.pass, httt.pass]
    *   [yearn, cnpm.pass, mmt.pass, ktmt.pass, khmt.pass, httt.pass]
    *   ...
    * ]
    */
   console.log(json);
   if (json != null && json.length > 0) {
       var labels = new Array();
       var dataCNPM = new Array();
       var dataMMT = new Array();
       var dataHTTT = new Array();
       var dataKTMT = new Array();
       var dataKHMT = new Array();

       for (var i = 0; i < json.length; i++) {
           labels[i] = json[i].year;
           
           dataCNPM[i] = json[i].cnpm_pass;
           dataMMT[i] = json[i].mmt_pass;
           dataHTTT[i] = json[i].httt_pass;
           dataKTMT[i] = json[i].ktmt_pass;
           dataKHMT[i] = json[i].khmt_pass;
       }

       drawPassFailChart(labels, dataCNPM, dataMMT, dataHTTT, dataKTMT, dataKHMT);
   } else {
        console.log("No data received");
   }
}

var line1 = new RGraph.Line('pass-fail-chart-1', [], [], [], [], []);
function drawPassFailChart(labels, dataCNPM, dataMMT, dataHTTT, dataKTMT, dataKHMT) {
    /*var data01 = [10, 20, 30, 69, 83, 72];
    var data02 = [50, 20, 80, 59, 23, 72];
    var data03 = [10, 60, 40, 39, 43, 82];
    var data04 = [40, 80, 80, 59, 53, 72];
    var data05 = [10, 30, 40, 69, 43, 92];*/
    clearCanvase('pass-fail-chart-1');
    //var line1 = new RGraph.Line('pass-fail-chart-1', dataCNPM, dataMMT, dataHTTT, dataKTMT, dataKHMT);
    line1.reSetData(false, dataCNPM, dataMMT, dataHTTT, dataKTMT, dataKHMT);
    line1.Set('chart.background.grid', true);
    line1.Set('chart.linewidth', 3);
    line1.Set('chart.gutter.left', 35);
    line1.Set('chart.hmargin', 5);

    if (!document.all || RGraph.isIE9up()) {
            line1.Set('chart.shadow', true);
    }
    line1.Set('chart.tickmarks', null);
    line1.Set('chart.units.post', '%');
    line1.Set('chart.hmargin', 10);
    line1.Set('chart.gutter.bottom', 50);
    line1.Set('chart.gutter.left', 50);
    line1.Set('chart.ymax', 100);
    line1.Set('chart.title.xaxis', 'thời gian');
    //line1.Set('chart.title.yaxis', 'tỉ lệ đậu (%)');
    line1.Set('chart.colors', ['red', 'green', 'blue', 'yellow', 'black']);
    //line1.Set('chart.curvy', 1);
    line1.Set('chart.curvy.factor', 0.25);
    //line1.Set('chart.labels',['Jan','Feb','Mar','Apr','May','Jun']);
    line1.Set('chart.labels', labels);

    line1.Set('chart.background.grid.hlines', false);
    line1.Set('chart.background.grid.autofit.numvlines', 11);
    line1.Set('chart.animation.unfold.initial', 0);

    RGraph.isOld() ? line1.Draw() : RGraph.Effects.Line.Unfold(line1);
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
