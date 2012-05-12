<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="uit.cnpm02.dkhp.model.RegistrationTime"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
 RegistrationTime registrationTime = (RegistrationTime) session.getAttribute("registrationTime");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết lớp học</title>
        <style media="all" type="text/css">
          #table_mh{
                padding-left: 100px;
                text-align: left;
                width: 500px;

            }
            #table th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #table td{
                text-align: center;
                background-color: #5F676D;
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

            #error_code, #error_name, #error_tclt, #error_tcth {
                font-size: 10px;
                color: #cc0033;
            }
        </style>
        </style>
    </head>
    <body>
        <script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->

                <hr/><hr/>
                <div id="title">
                    <u><h3><b>Thời gian đăng ký cho học kỳ <%=registrationTime.getId().getSemester()%> năm học <%=registrationTime.getId().getYear()%></b></h3></u>
                </div>
                <hr/><hr/><br>

                <form id="" action="../../TimeController?action=update" method="post" >
                   <table id="table_mh" class="general-table">
                       <tr>
                           <td>Ngày bắt đầu đăng ký: </td>   
                           <td> 
                               <input type="text" id="startDate" name="startDate" readonly="readonly" value="<%=DateTimeUtil.format(registrationTime.getStartDate())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('startDate','YYMMMDD')" />
                           </td>
                       </tr>
                        <tr>
                           <td>Ngày kết thúc đăng ký: </td>   
                           <td>
                               <input type="text" id="endDate" name="endDate" readonly="readonly" value="<%=DateTimeUtil.format(registrationTime.getEndDate())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('endDate','YYMMMDD')" />
                           </td>
                       </tr>
                       <tr>
                             <td></td>
                             <td>
                             <input type="submit" id="update" name="update" value="  Hoàn thành  "/> 
                             </td>
                        </tr>
                    </table>
                        <div id="message"></div>
                </form>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
</html>
