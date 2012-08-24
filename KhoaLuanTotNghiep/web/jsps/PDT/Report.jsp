<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="uit.cnpm02.dkhp.model.ReportBySemester"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    List<String> yearList = (List<String>) session.getAttribute("yearList");
    ReportBySemester report = (ReportBySemester) session.getAttribute("report");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../../javascripts/chart/RGraph.common.core.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.dynamic.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.effects.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.key.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.tooltips.js"> </script>
        <script src="../../javascripts/chart/RGraph.line.js"> </script>
        <title>Thống kê</title>
        <style media="all" type="text/css">
            <%-- CSS definition --%>
            .cnpm{
                background: red;
            }
            .mmt{
                background: green;
            }
            .httt{
                background: blue;
            }
            .ktmt {
                background: yellow;
            }
            .khmt{
                background: black;
            }
            .chart-note {
                width: 14px;
                height: 14px;
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

                <div id="main-title">
                    THỐNG KÊ ĐKHP
                </div>
                <br /><br />
                <div class="clear"></div>
                <div id="form-search">
                    Năm học 
                    <select id="select-year" class="input-minwidth" onchange="reloadReport()">
                        <%
                            for (int i = 0; i < yearList.size(); i++) {
                        %>
                        <option value="<%= yearList.get(i)%>"> <%= yearList.get(i)%> </option>
                        <%
                            }
                        %>
                    </select>
                    Hoc kỳ 
                    <select id="select-semeter" onchange="reloadReport()">
                        <option value="1">  1  </option>
                        <option value="2">  2  </option>
                    </select>
                </div>
                <%-- Root panel --%>
                <div id="result-bound">
                    <%-- Root panel --%>
                    <div class="range">
                        <h3>
                            <span id="btn-open-class" class="atag"> 
                                Kết quả thống kê
                            </span>
                        </h3>
                        <table id="tableReport" class="general-table" style="width: 660px; margin-top: 1px;">
                            <%if(report == null){%>
                            <%}else{
                                    DecimalFormat format = new DecimalFormat("#.##");
                                    float ever = (float) report.getNumOfReg() / report.getNumOfClassOpen();
                                    String everstr = format.format(ever);
                                    float percentPass = (float) report.getNumOfPassReg() / report.getNumOfReg() * 100;
                                    String percentPassSrt = format.format(percentPass);
                                    float percentNotPass = (float) report.getNumOfNotPassReg() / report.getNumOfReg() * 100;
                                    String percentNotPassSrt = format.format(percentNotPass);
                            %>
                            <tr>
                                <td>Số lớp đã mở:</td>
                                <td><%=report.getNumOfClassOpen()%></td> 
                            </tr>
                            <tr>
                                <td>Số đăng ký: </td>
                                <td><%=report.getNumOfReg()%></td>
                            </tr>
                            <tr>
                                <td>Trung bình: </td>
                                <td><%=everstr%> (ĐK/Lớp)</td>
                            </tr>
                            <tr>
                                <td>Lớp có đăng ký nhiều nhất: </td>
                                <td><%=report.getMaxRegClass().getId().getClassCode()%> (<%=report.getMaxRegClass().getNumOfStudentReg()%>)</td>
                            </tr>
                            <tr>
                                <td>Lớp có đăng ký ít nhất: </td>
                                <td><%=report.getMinRegClass().getId().getClassCode()%> (<%=report.getMinRegClass().getNumOfStudentReg()%>)</td>
                            </tr>
                            <tr>
                                <td>Số sinh viên đạt: </td>
                                <td><%=report.getNumOfPassReg()%> (<%=percentPassSrt%>%) </td>
                            </tr>
                            <tr>
                                <td>Số sinh viên không đạt: </td>
                                <td><%=report.getNumOfNotPassReg()%> (<%=percentNotPassSrt%>%) </td>
                            </tr>
                            <%}%>
                         </table>
                    </div>
                </div>
                 <div id="chart-pass-fail-1">
                     <b> Tỉ lệ SV đủ điểm qua môn các khoa </b>
                    <canvas id="pass-fail-chart-1" width="900" height="420">
                        Your browser does not support the HTML5 canvas tag.
                    </canvas>
                     <div class="clear"></div>
                     <div style="">
                         <%
                         int currentYear = Calendar.getInstance().get(Calendar.YEAR)-1;
                         int start = 2007;
                         %>
                         <select id="start-year" style="float: left; margin-left: 33px;">
                             <%
                             for (int i = 0; i < (currentYear-start); i++) {
                                 String year = (start + i) + "-" + (start + i + 1);
                            %>
                             <option value="<%= start +i %>"> <%= year %> </option>
                             <%}%>
                         </select>
                         <select id="end-year" style="float: right; margin-right: 81px;">
                             <%
                             int count = 0;
                             for (int j = currentYear; j > start; j--) {
                                 String year = (currentYear - count) + "-" + (currentYear - count +1);
                                 count++;
                            %>
                             <option value="<%= currentYear - count +1 %>"> <%= year %> </option>
                             <%}%>
                         </select>
                     </div>
                         <div class="clear"></div>
                     <div id="select-time" class="chart-info left" style="width: 163px;">
                        <input type="radio" name="time" value="1"> Học kỳ 1 <br />
                        <input type="radio" name="time" value="2"> Học kỳ 2 <br />
                        <input type="radio" name="time" value="3">  Học kỳ 3 <br />
                        <input type="radio" name="time" value="0" checked>  Cả năm <br />
                     </div>
                     <div class="chart-info right" style="width: 175px;">
                         <table>
                            <tr><td><div class="cnpm chart-note"></div></td><td>Khoa công nghệ phần mêm</td></tr>
                            <tr><td><div class="mmt chart-note"></div></td><td>Khoa mạng máy tính</td></tr>
                            <tr><td><div class="httt chart-note"></div></td><td>Khoa hệ thống thông tin</td></tr>
                            <tr><td><div class="ktmt chart-note"></div></td><td>Khoa kỹ thuật máy tính</td></tr>
                            <tr><td><div class="khmt chart-note"></div></td><td>Khoa khoa học máy tính</td></tr>
                         </table>
                     </div>
                     <br />
                 </div>
                <br /><br />
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <%--<script src="../../javascripts/AjaxUtil.js"></script>--%>
    <%--<script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>--%>
    <script type="text/javascript" src="../../javascripts/Report.js"></script>
    <script  type = "text/javascript" >
        window.onload = function() {
           // The data for the Line chart. Multiple lines are specified as seperate arrays.
           
           uploadChart();
           $('#end-year').live('change', function() {
                uploadChart();
           });
           $('#start-year').live('change', function() {
                uploadChart();
           });
           $('#select-time input:radio').live('click', function() {
               uploadChart();
           });
        }
        
        function uploadChart() {
            var startYear = $('#start-year').val();
            var endYear = $('#end-year').val();
            var time = $('#select-time input[checked]').val();
            doLoadReportPassFailData(startYear, endYear, time);
        }

        var http = createRequestObject();
              
        function reloadReport(){
            var year = document.getElementById("select-year").value;
            var semeter = document.getElementById("select-semeter").value;
            if (http) {
                http.open("GET", "../../ReportController?action=trainclass-report&year="
                    + year + "&semeter=" + semeter , true);
                http.onreadystatechange = loadReportHandler;
                http.send(null);
            }  
        }      
        function loadReportHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tableReport");
                detail.innerHTML=http.responseText;
            }
        }
    </script>
</html>
