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
    List<String> clazz = (List<String>) session.getAttribute("list-subject");
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
            
            #btn-submit-score {
                margin-left: 175px;
                text-align: center;
                width: 100px;
                background: #175F6E;
            }
            
            #btn-submit-score:hover{
                background: #22de33;
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
                <div id="title">
                    Trang quản lý điểm Sinh Viên
                </div>
                <%--List file--%>
                <br />
                <div id="list-file">
                    <%-- File from Lecturer --%>
                    <h4>Bảng điểm GV đã gửi 
                    <span class="atag" onclick="getFileFromLecturer()">(Refresh)</span></h4>
                    <div id="file-from-lecturer" class="list-file-tobe-process">
                        <div id="file-from-lecturer-sub">
                            <table id="tbl-file-from-lecturer">
                                
                            </table>
                        </div>
                    </div>
                    <%-- TrainClass detail information --%>
                    <div id="train-class-detail" >
                    </div>
                </div>

                <%-- Result --%>
                <div class="clear"> <br /> </div>
                <div id="btn-submit-score">
                    <span class="atag" onclick="doImportScoreFromFile('tbl-file-from-lecturer')" >Submit</span>
                </div>
                
                <%-- Result --%>
                <div class="clear"></div>
                <div id="import-result">
                    KQua: Tcông --> Show bảng điểm
                    Thất bại: --> Show chi tiết lỗi.
                </div>
                
                <%-- Manual input --%>
                <div id="btn-manual-input">
                    <span class="atag"
                          onclick="showFormInputManual('form-manul-input',
                              'btn-manual-input',
                              '<u><b>Nhap thu cong</b></u>',
                              '<u><b>Nhap thu cong</b></u>')">
                        <u><b>Nhap thu cong</b></u>
                    </span>
                </div>
                <div id="form-manul-input" style="display: none;">
                    Chon lop:
                    <select id="lst-trainclass" onchange="trainclassChanged(this.value)" >
                    </select>
                    <div id="manual-input-main">
                    </div>
                </div>
                
                <div class="clear"></div>
                <%-- Request lecturer re-send score --%>
                <div id="btn-request-lecturer-resend">
                    <span class="atag"
                          onclick="showFormRequestLecturerResendScore('form-request-lecturer-resend',
                              'btn-request-lecturer-resend',
                              '<u><b>YC GV gui lai bang diem</b></u>',
                              '<u><b>YC GV gui lai bang diem</b></u>')">
                        <u><b>YC GV gui lai bang diem</b></u>
                    </span>
                </div>
                <div id="form-request-lecturer-resend" style="display: none;">
                    <div id="list-request-lecturer-resend-info">
                        <%-- Dynamic data --%>
                    </div>
                    <%-- Ket qua gui yeu cau --%>
                    <div id="send-request-result">
                    </div>
                    <input type="button" onclick="sendRequest()" value="Gửi yêu cầu"/>
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
        <script  type = "text/javascript" >
            var http = createRequestObject();
            
            var loadLecturerList = false;
            var loadClassList = false;
            
            //
            // Manual input process...
            //
            function showFormInputManual(stuffId, btnId, btnShowLabel, btnHideLabel) {
                var btnHidden = "<span onclick=\"hideFormInputManual('" + stuffId + "','" + btnId + "','" + btnShowLabel + "','" + btnHideLabel + "')\" class=\"atag\">"
                                + btnHideLabel + "</span>";
                document.getElementById(btnId).innerHTML = btnHidden;
                document.getElementById(stuffId).style.display = 'block';
                
                if (!loadClassList) {
                    doLoadOpenTrainClassList();
                    loadClassList = true;
                }
            }
            
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
                alert("Tobe implemented...");
            }
            
            //
            // Request lecturer send score file.
            //
            function hideFormInputManual(stuffId, btnId, btnShowLabel, btnHideLabel) {
                var btnShow = "<span onclick=\"showFormInputManual('" + stuffId + "','" + btnId + "','" + btnShowLabel + "','" + btnHideLabel + "')\" class=\"atag\">"
                    + btnShowLabel + "</span>";
                document.getElementById(btnId).innerHTML = btnShow;
                document.getElementById(stuffId).style.display = 'none';
            }
            
            function showFormRequestLecturerResendScore(stuffId, btnId, btnShowLabel, btnHideLabel) {
                var btnHidden = "<span onclick=\"hideFormRequestLecturerResendScore('" + stuffId + "','" + btnId + "','" + btnShowLabel + "','" + btnHideLabel + "')\" class=\"atag\">"
                                + btnHideLabel + "</span>";
                document.getElementById(btnId).innerHTML = btnHidden;
                document.getElementById(stuffId).style.display = 'block';
                
                if (!loadLecturerList) {
                    doLoadLecturerList();
                    loadLecturerList = true;
                }
            }
            
            function hideFormRequestLecturerResendScore(stuffId, btnId, btnShowLabel, btnHideLabel) {
                var btnShow = "<span onclick=\"showFormRequestLecturerResendScore('" + stuffId + "','" + btnId + "','" + btnShowLabel + "','" + btnHideLabel + "')\" class=\"atag\">"
                    + btnShowLabel + "</span>";
                document.getElementById(btnId).innerHTML = btnShow;
                document.getElementById(stuffId).style.display = 'none';
            }
            
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
                
                alert("Enter SendRequest, " + lecturerId + ", trainclass: " + trainClassId);
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
                /*var chkbox = document.getElementById('chbox-list' + 0);
                if (chkbox.checked) {
                    data += chkbox.value;
                }*/
                
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
                    http.onreadystatechange = importScoreHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
                // Step 3: handle respone
            }
            
            function importScoreHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("import-result");
                    detail.innerHTML = http.responseText;
                }
            }
        
            //==> Shared be remove
            function submitInsertScoreFromFile(pagename){
                var clazz = document.getElementById("select_class").value;
                var course = document.getElementById("select_course").value;
                var year = document.getElementById("select_year").value;
                var datas = "&clazz=" + clazz;
                datas += "&course=" + course;
                datas += "&year=" + year;
                var controller = pagename + datas;
            
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = handleResponse;
                    http.send(null);
                
                }
            }
        
            function handleResponse() {
                if(http.readyState == 4 && http.status == 200){
                    var detail=document.getElementById("import-result");
                    detail.innerHTML=http.responseText;
                }
            }
        </script>
    </body>
</html>
