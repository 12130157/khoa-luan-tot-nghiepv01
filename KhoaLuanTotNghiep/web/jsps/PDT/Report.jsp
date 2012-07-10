<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    List<Faculty> falculties = (List<Faculty>) session.getAttribute("faculties");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report</title>
        <style media="all" type="text/css">
            <%-- CSS definition --%>
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
                
                <div id="main-title">
                    THỐNG KÊ ĐKHP
                </div>
                <br /><br />
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
                        <option value="All">All</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                    </select>
                    Khoa
                    <select id="select-faculty" onchange="reloadTrainClass()">
                        <option>All</option>
                        <%
                        if ((falculties != null) && !falculties.isEmpty()) {
                            for (Faculty f : falculties) {
                        %>
                        <option value="<%= f.getId()%>"><%= f.getFacultyName() %></option>
                        <%}}%>
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
                    <%--
                    <div class="range">
                        <h3><span id="btn-cancel-class" class="atag">
                            Các lớp đã hủy
                        </span></h3>
                        <div id="tbl-cancel-class" style="display: none;">
                            
                        </div>
                    </div>
                    --%>
                </div>
                <br /><br />
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
        var http1 = createRequestObject();
        var loadOpenClass = false;
        var loadCloseClass = false;
        var resourceReady = true;

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
        
        // ++LOAD OPEN TRAINCLASS
        function doLoadOpenTrainClassList() {
            var year = document.getElementById("select-year").value;
            var semeter = document.getElementById("select-semeter").value;
            var faculty = document.getElementById("select-faculty").value;
            var status = "Opened";
            if (http) {
                http.open("GET", "../../ReportController?action=trainclass-report&year="
                    + year + "&semeter=" + semeter + "&faculty=" + faculty
                    + "&status=" + status, true);
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
            var faculty = document.getElementById("select-faculty").value;
            var status = "Closed";
            if (http1) {
                http1.open("GET", "../../ReportController?action=trainclass-report&year="
                    + year + "&semeter=" + semeter + "&faculty=" + faculty
                    + "&status=" + status, true);
                http1.onreadystatechange = loadCloseTrainClassHandler;
                http1.send(null);
            }
        }
        
        function loadCloseTrainClassHandler() {
            if(http1.readyState == 4 && http1.status == 200){
                var detail=document.getElementById("tbl-close-class");
                detail.innerHTML=http1.responseText;
            }
        }
        
        function reloadTrainClass() {
            doLoadClosedTrainClassList();
            doLoadOpenTrainClassList();
        }
    </script>
</html>
