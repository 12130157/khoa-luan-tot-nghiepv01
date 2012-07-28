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
        <title>Thống kê</title>
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
                            <p>Chưa có thông tin</p>
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
