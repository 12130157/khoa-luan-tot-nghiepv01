<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report</title>
        <style media="all" type="text/css">
            #result-bound {
                
            }
            #open-class {
                
            }
            #closed-class {
                
            }
            #cancel-class {
                
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
            <div id="content"><!--Main Contents-->
                
                <div id="title">
                    THỐNG KÊ ĐKHP
                </div>
                <div class="clear"></div>
                <div id="form-search">
                    Năm học 
                    <select id="select-year" class="input-minwidth" onchange="reloadTrainClass()">
                        <option> All </option>
                        <%
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        int start = 2007;
                        int numberYear = year - start;
                        for (int i = numberYear; i > 0; i--) {
                            String description = (start + i - 1) + " - " + (start + i);
                            String value = description.replace(" ", "");
                            %>
                            <option value="<%= value%>"> <%= description %> </option>
                            <%
                        }
                        %>
                    </select>
                    Hoc kỳ 
                    <select id="select-semeter" onchange="reloadTrainClass()">
                        <option>All</option>
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                    </select>
                </div>
                <%-- Root panel --%>
                <div id="result-bound">
                    <%-- Root panel --%>
                    <div class="range">
                        <h3><span id="btn-open-class" class="atag">
                            Các lớp đang mở
                            </span></h3>
                        <div id="tbl-open-class" style="display: none;">
                            <%----%>
                        </div>
                    </div>
                    <%-- Root panel - Closed class --%>
                    <div class="range">
                        <h3><span id="btn-close-class" class="atag">
                            Các lớp đã đóng
                        </span></h3>
                        <div id="tbl-close-class" style="display: none;">
                            a<%----%>
                        </div>
                    </div>
                    <%-- Root panel - Canceled class --%>
                    <div class="range">
                        <h3><span id="btn-cancel-class" class="atag">
                            Các lớp đã hủy
                        </span></h3>
                        <div id="tbl-cancel-class" style="display: none;">
                            <%----%>
                        </div>
                    </div>
                </div>
                <br /><br />
                <%-- Thogn ke theo SV --%>
                <div class="range">
                    <h3><span id="btn-student-report" class="atag" >Thống kê SV</span></h3>
                    <div id="form-student-report">
                        <div id="search" style="clear: both; margin-top: 10px;">
                            <table>
                                <tr>
                                    <td>
                                        <b>Tìm kiếm</b>
                                        <input id="search-student" type="text" placeholder="Nhập tên SV" value="" onKeyPress="keypressed()"/>
                                    </td>
                                    <td>
                                        <input type="button" onclick="SendRequestFindStudent()" value="Tìm"/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    
                        <div id="list-student" style="float: left;">
                        </div>
                        <div id="student-detail" style="float: left; margin-top: -25px; padding-left: 16px;">
                        </div>
                    </div>
                <div class="clear"></div>
                <br /><br />
                </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <script  type = "text/javascript" >

        var http = createRequestObject();
        var loadOpenClass = false;
        var loadCloseClass = false;
        var loadCancelClass = false;

        $("#btn-open-class").click(function () {
            $('#tbl-open-class').slideToggle(500);
            if (!loadOpenClass) {
                doLoadOpenTrainClassList();
                loadOpenClass = true;
            }
        });
        
        $("#btn-close-class").click(function () {
            $('#tbl-close-class').slideToggle(500);
            if (!loadCloseClass) {
                doLoadClosedTrainClassList();
                loadCloseClass = true;
            }
        });
        
        $("#btn-cancel-class").click(function () {
            $('#tbl-cancel-class').slideToggle(500);
            if (!loadCancelClass) {
                doLoadCancelTrainClassList();
                loadCancelClass = true;
            }
        });
        
        $("#btn-student-report").click(function () {
            $('#form-student-report').slideToggle(500);
        });

        // ++LOAD OPEN TRAINCLASS
        function doLoadOpenTrainClassList() {
            var year = document.getElementById("select-year").value;
            var semeter = document.getElementById("select-semeter").value;
            var status = "Opened";
            if (http) {
                http.open("GET", "../../ReportController?action=trainclass-report&year="
                    + year + "&semeter=" + semeter + "&status=" + status, true);
                http.onreadystatechange = loadOpenTrainClassHandler;
                http.send(null);
            }
        }
        
        function loadOpenTrainClassHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tbl-open-class");
                detail.innerHTML=http.responseText;
            }
        }
        // LOAD CLOSED TRAINCLASS ++
        function doLoadClosedTrainClassList() {
            var year = document.getElementById("select-year").value;
            var semeter = document.getElementById("select-semeter").value;
            var status = "Closed";
            if (http) {
                http.open("GET", "../../ReportController?action=trainclass-report&year="
                    + year + "&semeter=" + semeter + "&status=" + status, true);
                http.onreadystatechange = loadCloseTrainClassHandler;
                http.send(null);
            }
        }
        
        function loadCloseTrainClassHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tbl-close-class");
                detail.innerHTML=http.responseText;
            }
        }
        // LOAD CLOSED TRAINCLASS --
        
        // LOAD CANCEL TRAINCLASS ++
        function doLoadCancelTrainClassList() {
            var year = document.getElementById("select-year").value;
            var semeter = document.getElementById("select-semeter").value;
            var status = "Canceled";
            if (http) {
                http.open("GET", "../../ReportController?action=trainclass-report&year="
                    + year + "&semeter=" + semeter + "&status=" + status, true);
                http.onreadystatechange = loadCancelTrainClassHandler;
                http.send(null);
            }
        }
        
        function loadCancelTrainClassHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tbl-cancel-class");
                detail.innerHTML=http.responseText;
            }
        }
        // LOAD CANCELED TRAINCLASS --
        function reloadTrainClass() {
            doLoadCancelTrainClassList();
            doLoadClosedTrainClassList();
            doLoadOpenTrainClassList();
        }
        //
        //
        // Search Student
        //
        function keypressed() { 
            if(event.keyCode=='13') {
                SendRequestFindStudent();
            }
        }
        
        function SendRequestFindStudent(){
            var search = document.getElementById("search-student").value;
            if (http) {
                http.open("GET", "../../ReportController?action=search_student&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
            }
        }
        
         function handleResponseFindStudent() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("list-student");
                 detail.innerHTML=http.responseText;
             }
         }
         
         //
         // Get student Report
         //
         function getDetailStudentReport(mssv) {
            //var search = document.getElementById("search-student").value;
             if (http) {
                http.open("GET", "../../ReportController?action=student-report&value="
                    + mssv, true);
                http.onreadystatechange = handleResponseStudentReport;
                http.send(null);
              }
         }
         
         function sortTrainClass(by, type) {
             if (http) {
                http.open("GET", "../../ReportController?action=sort-student-report"
                    + "&by=" + by
                    + "&type=" + type, true);
                http.onreadystatechange = handleResponseStudentReport;
                http.send(null);
              }
         }
         
         function handleResponseStudentReport() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("student-detail");
                 detail.innerHTML=http.responseText;
             }
         }
         
         //
         // Get TrainClass report
         //
         function getTrainClassReport() {
            var year = document.getElementById("year").value;
            var semeter = document.getElementById("semeter").value;
            if (http) {
                http.open("GET", "../../ReportController?action=class-report"
                    + "&year=" + year
                    + "&semeter=" + semeter, true);
                http.onreadystatechange = handleResponseTrainClassReport;
                http.send(null);
              }
         }
         
         function handleResponseTrainClassReport() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("trainclass-report");
                 detail.innerHTML=http.responseText;
             }
         }
         
    </script>
</html>
