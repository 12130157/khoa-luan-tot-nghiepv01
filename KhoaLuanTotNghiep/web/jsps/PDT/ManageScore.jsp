<%-- 
    Document   : ManageScore
    Created on : 26-12-2011, 21:40:04
    Author     : LocNguyen
--%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    List<TrainClass> missingScoreClazzes
            = (List<TrainClass>) session.getAttribute("list-miss-core-class");
    String error = (String) session.getAttribute("error");
    if (error != null) {
        //Clear session
        session.removeAttribute("error");
    }

%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Điểm Sinh Viên</title>
        <style media="all" type="text/css">
            .list-file-tobe-process {
                float: left;
                border: 1px solid #DD3F22;
                width: 450px;
                min-height: 180px;
            }
            
            #train-class-detail {
                float: left;
                padding-left: 25px;
                min-height: 180px;
            }

            #btn-add-remove {
                float: left;
                margin-left: 25px;
                margin-right: 25px;
            }
            
            #import-from-file-result {
                font-size: 11px;
                max-height: 500px;
            }
            #import-manual-result {
                font-size: 11px;
                max-height: 500px;
            }
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%-- Title --%>
                <div id="main-title">
                    Trang quản lý điểm Sinh Viên
                </div>
                <%--List file--%>
                <br />
                <div id="list-file" class="range">
                    <%-- File from Lecturer --%>
                    <h3><span id="btn-list-file" class="atag">Bảng điểm GV đã gửi</span></h3> 
                    <div id="file-from-lecturer" style="padding-top: 25px; display: none;">
                        <div  class="list-file-tobe-process">
                            <table id="tbl-file-from-lecturer">
                            </table>
                        </div>
                        <%-- TrainClass detail information --%>
                        <div id="train-class-detail" >
                        </div>
                    
                        <%-- Result --%>
                        <div class="clear"> <br /> </div>
                        <div class="button-1">
                            <span class="atag" onclick="doImportScoreFromFile('tbl-file-from-lecturer')" ><img src="../../imgs/check.png"/>Submit</span>
                        </div>
                        <%-- Result --%>
                        <div class="clear"></div>
                        <br />
                        <div id="import-from-file-result">
                        </div>
                    </div>
                </div>

                <div class="clear"></div>
                <%-- Manual input --%>
                <div class="range">
                    <h3><span id="btn-manual-input" class="atag">Nhập thủ công</span></h3> 
                    <div id="form-manual-input" style="display: none;">
                    Chọn lớp:
                        <select id="lst-trainclass" onchange="trainclassChanged(this.value)" >
                        </select>
                        <div id="manual-input-main">
                        </div>
                        <%-- Result --%>
                        <div class="clear"></div>
                        <div id="import-manual-result">
                        </div>
                    </div>
                </div>
                <%-- Request lecturer re-send score --%>
                <br />
                <div class="range">
                    <h3><span id="btn-request-lecturer-resend" class="atag">Yêu cầu GV gửi bảng điểm</span></h3> 
                    <div id="form-request-lecturer-resend" style="display: none;" class="div-range">
                        <div id="list-request-lecturer-resend-info">
                         <%-- Dynamic data --%>
                        </div>
                        <%-- Ket qua gui yeu cau --%>
                        <br />
                        <div id="send-request-result">
                        </div>
                        <div class="button-1">
                            <span class="atag" onclick="sendRequest()" ><img src="../../imgs/check.png"/>Gửi yêu cầu</span>
                        </div>
                    </div>
                </div>
                <br />
                <%-- Danh sach lop chua cap nhat diem --%>
                <br />
                <div class="range">
                    <h3><span id="btn-list-class-not-update-score" class="atag">Các lớp chưa nhập điểm</span></h3> 
                    <div id="list-class-not-update-score" style="display: none;" class="div-range">
                        <%-- Danh sach cac lop chua cap nhat diem J --%>
                        <%if ((missingScoreClazzes == null) || missingScoreClazzes.isEmpty()) {%>
                            <i>Không có lớp</i>
                        <%} else {
                            int counter = 1;
                        %>
                        <table class="general-table" style="width: 720px;">
                                <tr>
                                    <th>STT</th>
                                    <th>Mã lớp</th>
                                    <th>Môn học</th>
                                    <th>GV</th>
                                </tr>
                                <%for (TrainClass clzz : missingScoreClazzes) {%>
                                    <tr>
                                        <td><%= counter++ %></td>
                                        <td> <%-- This will link to TrainClass Detail--%>
                                            <a href="../../ManageClassController?action=detail&classID=<%= clzz.getId().getClassCode()%>&year=<%= clzz.getId().getYear()%>&semester=<%= clzz.getId().getSemester()%>"><%= clzz.getId().getClassCode() %></a>
                                        </td>
                                        <td><%= clzz.getSubjectName() %></td>
                                        <td><%= clzz.getLectturerName() %></td>
                                    </tr>
                                <%}%>
                            </table>
                        <%}%>
                    </div>
                </div>
                <br />
            </div><!--End Contents-->

            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"> </script>
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            var http = createRequestObject();
            
            var loadLecturerList = false;
            var loadClassList = false;
            var loadFileFromLecturer = false;
            
            //
            // Manual input process...
            //
            $("#btn-list-file").click(function () {
                $('#file-from-lecturer').slideToggle(500);
                if (!loadFileFromLecturer) {
                    getFileFromLecturer();
                    loadFileFromLecturer = true;
                }
            });
            
            $("#btn-list-class-not-update-score").click(function () {
                $('#list-class-not-update-score').slideToggle(500);
            });
            
            function getFileFromLecturer() {
                var controller = "../../ManageScoreController?function=get-files";
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = getFileHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function getFileHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("tbl-file-from-lecturer");
                    detail.innerHTML = http.responseText;
                }
            }
            ////////////////////////
            $("#btn-manual-input").click(function () {
                $('#form-manual-input').slideToggle(500);
                if (!loadClassList) {
                    doLoadOpenTrainClassList();
                    loadClassList = true;
                }
            });
            function doLoadOpenTrainClassList() {
                // Load Open train class
                var controller = "../../ManageScoreController?function=load-trainclass";
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = loadTrainClassHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function loadTrainClassHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("lst-trainclass");
                    detail.innerHTML = http.responseText;
                }
            }
            ////////////////////////
            $("#btn-request-lecturer-resend").click(function () {
                $('#form-request-lecturer-resend').slideToggle(500);
                if (!loadLecturerList) {
                    doLoadLecturerList();
                    loadLecturerList = true;
                }
            });
            function doLoadLecturerList() {
                // Load lecturer, just the man, who has open train class
                var controller = "../../ManageScoreController?function=load-lecturer-list";
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = loadLecturerListHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function loadLecturerListHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("list-request-lecturer-resend-info");
                    detail.innerHTML = http.responseText;
                }
            }

            ////////////////////////////
            // key: classid-year-semeter
            function trainclassChanged(key) {
                // Initial manual input form
                var controller = "../../ManageScoreController?function=initial-manul-input-form&"
                                + "key=" + key;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = trainclassChangedHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function trainclassChangedHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("manual-input-main");
                    detail.innerHTML = http.responseText;
                }
            }
            
            function submitManul() {
                // Step 1: Retrieve data from form
                var table = document.getElementById('list-student-result');
                var rowCount = table.rows.length;
                if (rowCount <= 1) {
                    alert("Error, number row of table < 1");
                    return;
                }
                // Key: trainclassID; year; semeter
                var key = document.getElementById("lst-trainclass").value;
                var data = key;
                for(var i = 1; i < rowCount; i++) {
                    var cells = table.rows[i].cells;
                    var studentId = cells[1].innerHTML; // is id of score text box
                    var txtScore = document.getElementById(studentId);
                    var score = txtScore.value;
                    data += ";" + studentId + "," + score;
                }
                
                // Step 2: Send request
                var controller = "../../ManageScoreController?function=import-manual"
                    + "&data=" + data;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = importScoreManualHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            //
            // Request lecturer send score file.
            //
            
            
            function selectLecturer(){
                var lecturerId = document.getElementById('list-lecturer').value;
                var controller = "../../ManageScoreController?function=lecturer-change&lecturer_id="
                            + lecturerId;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = selectLecturerHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function selectLecturerHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("list-class-of-lecturer");
                    detail.innerHTML = http.responseText;
                }
            }

            function sendRequest() {
                var lecturerId = document.getElementById("list-lecturer").value;
                var trainClassId = document.getElementById("list-class-of-lecturer").value;
                
                var controller = "../../ManageScoreController?function=submit-resend-score&lecturer_id="
                            + lecturerId
                            + "&trainclass=" + trainClassId;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = sendRequestHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function sendRequestHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("send-request-result");
                    detail.innerHTML = http.responseText;
                }
            }
            
            function dowloadFile(classId) {
                alert("Entered Download file. Shall be implemented soon.");
            }
            
            
            
            function getClassDetail(trainclassId) {
                var controller = "../../ManageScoreController?function=get-trainclass-detail"
                    + "&id=" + trainclassId;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = getTrainClassDetailHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function getTrainClassDetailHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("train-class-detail");
                    detail.innerHTML = http.responseText;
                }
            }
            
            function doImportScoreFromFile(tableId) {
                // Step 1: Get data from table
                var table = document.getElementById('tbl-file-from-lecturer');
                var rowCount = table.rows.length;
                if (rowCount < 1) {
                    alert("Error, number row of table < 1");
                    return;
                }
                var data = "";
                for(var i = 0; i < rowCount; i++) {
                    chkbox = document.getElementById('chbox-list' + i)
                    if (chkbox.checked) {
                        if (data.length > 0)
                            data += ";";
                        data += chkbox.value;
                    }
                }
    
                // Step 2: Send request
                var controller = "../../ManageScoreController?function=import-score"
                    + "&data=" + data;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = importScoreFromFileHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
                // Step 3: handle respone
            }
            
            function importScoreFromFileHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("import-from-file-result");
                    detail.innerHTML = http.responseText;
                }
            }
            function importScoreManualHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("import-manual-result");
                    detail.innerHTML = http.responseText;
                }
            }
        </script>
    </body>
</html>
