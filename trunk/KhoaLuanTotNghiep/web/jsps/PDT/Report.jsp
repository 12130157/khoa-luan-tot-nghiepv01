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
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    THỐNG KÊ THEO SINH VIÊN
                </div>
                <div id="search">
                    <table>
                        <tr>
                            <td>
                                <b>Thông tin tìm kiếm sinh viên </b>
                                <input id="search-student" type="text" value="" onKeyPress="keypressed()"/>
                            </td>
                            <td>
                                <input type="button" onclick="SendRequestFindStudent()" value="Tìm"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div>
                    <div id="list-student">
                        <table>
                            <tr>
                                <td>

                                </td>
                            </tr>
                        </table>
                    </div>
                    <div id="student-detail">
                        <table>
                            <tr>
                                <td>

                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="clear"></div>
                <hr/><hr/><br>

                <%--Form add new Train subject--%>
                           
                <div id = "report-range">
                    THỐNG KÊ THEO LỚP HỌC
                    <table>
                        <tr>
                            <th>
                                <b>Năm học: </b>
                                <select id="year" name="year" onchange="">
                                <option value="*"> All </option>
                                <%
                                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                                if (currentYear <= 2007)
                                    currentYear = 2050;
                                int delta = currentYear - 2007;
                                for(int i = 0; i < delta; i++){
                                    String year = Integer.toString(2007 + i) +
                                            "-" + Integer.toString(2007 + i + 1);
                                %>
                                <option value="<%= year %>"><%= year %></option>
                                <%}
                                %>
                                </select>
                            </th>
                            <th>
                                <b>Học Kỳ: </b>
                                <select id="semeter" name="semeter" onchange="">
                                    <option value="*"> All </option>
                                    <option value="1"> 1 </option>
                                    <option value="2"> 2 </option>
                                </select>
                            </th>
                            <th>
                                <input type="button" value="Tìm" onclick="getTrainClassReport()" />
                            </th>
                        </tr>
                    </table>
                    
                    <div id="trainclass-report">
                        <table>
                            <tr>
                                <td>Tổng số lớp đã tạo: </td>
                                <td>120</td>
                            </tr>
                            <tr>
                                <td>Tổng số lớp hủy do không đủ điều kiện: </td>
                                <td>100</td>
                            </tr>
                            <tr>
                                <td>Tổng số lớp chưa kết thúc: </td>
                                <td>20</td>
                            </tr>
                        </table>
                    </div>
                </div>
                
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
