<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.Calendar"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết giảng dạy</title>
        <style media="all" type="text/css">

            table {
                /*width: 100%;*/
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            table th {
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            table td {
                text-align: center;
                background-color: #5F676D;
            }

            #report-range {
                /*width: 100%;*/
                float: left;
                padding-left: 10px;
                padding-right: 10px;
                padding-bottom: 25px;
                text-align: center;

            }

            #list-Lecturer {
                float: left;

                height:250px;
                overflow:auto;
            }
            #Lecturer-detail {
                float: none;
                padding-left: 50px;
                border-width: 1px;
                border-color: #00ff00;
            }
            #report-range td {
                text-align: left;
            }

            #title{
                background-color: #2f4e3d;
                text-align: center;
                padding-top: 12px;
                padding-bottom: 10px;
            }
            #page{
                text-align: center;
            }
            #sidebar {
                height:250px;
                overflow:auto;
            }
            a {
                color: violet;
            }
            #createLabel{
                padding-right: 15px;
            }

        </style>
    </head>

    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Quản lý chi tiết giảng dạy
                </div>
                <div style="float: left;">
                    <div id="searchbox" action="#">
                        <input id="search" type="text" onKeyPress="keypressed()" placeholder="Search" />
                        <input type="button" id="submit" onclick="FindLecturer()" value="Tìm kiếm" />
                    </div>
                </div>
                <div class="clear"></div>
                <div>
                    <div id="list-Lecturer">
                        <br>
                        <table id ="list-LecturerFind" name = "list-LecturerFind" class="general-table">

                        </table>
                    </div>
                    <div id="Lecturer-detail">

                    </div>
                </div>
                <div class="clear"></div>
                <br><hr/><hr/>

            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >

        var http = createRequestObject();
        //
        // Search Student
        //
        function keypressed()
        { 
            if(event.keyCode=='13')
            {
                FindLecturer();
            } 
        }
        function FindLecturer(){
            var search = document.getElementById("search").value;
            if (http) {
                http.open("GET", "../../DetailTrainManager?action=search_lecturer&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
            }
        }
        
        function handleResponseFindStudent() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("list-LecturerFind");
                detail.innerHTML=http.responseText;
            }
        }
         
        //
        // Get student Report
        //
        function getDetailLecturerReport(lecturerCode) {
            //var search = document.getElementById("search-student").value;
            if (http) {
                http.open("GET", "../../DetailTrainManager?action=lecturer-report&value="
                    + lecturerCode, true);
                http.onreadystatechange = handleResponseStudentReport;
                http.send(null);
            }
        }
        function handleResponseStudentReport() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("Lecturer-detail");
                detail.innerHTML=http.responseText;
            }
        }
        function updateDetailTrain(){
           document.forms["form-update"].submit();
        }
     </script>
</html>
