<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="java.util.Calendar"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý đăng ký</title>
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
            
            #list-student {
                float: left;
                
                height:250px;
                overflow:auto;
            }
            #student-detail {
                float: none;
                padding-left: 12px;
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

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    Quản lý đăng ký học phần
                </div>
                <div id="search">
                    <table>
                        <tr>
                            <td>
                                <b>Thông tin tìm kiếm </b>
                                <input id="search-student" type="text" value=""/>
                            </td>
                            <td>
                                <input type="button" onclick="FindStudent()" value="Tìm"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div>
                    <div id="list-student">
                        <table id ="list-studentFind" name = "list-studentFind">
                           
                        </table>
                    </div>
                    <div id="student-detail">
                        
                    </div>
                </div>
                <div class="clear"></div>
                <hr/><hr/><br>

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
        function FindStudent(){
            var search = document.getElementById("search-student").value;
             if (http) {
                http.open("GET", "../../RegistrationManager?action=search_student&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
              }
            }
        
         function handleResponseFindStudent() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("list-studentFind");
                 detail.innerHTML=http.responseText;
             }
         }
         
         //
         // Get student Report
         //
         function getDetailStudentReport(mssv) {
            //var search = document.getElementById("search-student").value;
             if (http) {
                http.open("GET", "../../RegistrationManager?action=student-report&value="
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
         
                 
         
    </script>
</html>
