<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.ViewTrainProgram"%>
<%@page import="uit.cnpm02.dkhp.model.Class "%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);
   
    ArrayList<ViewTrainProgram> pro = (ArrayList<ViewTrainProgram>) session.getAttribute("pro");
    Student student = (Student) session.getAttribute("student");
    Faculty faculty=(Faculty) session.getAttribute("faculty");
    Class classes=(Class) session.getAttribute("classes");
    int n = pro.size();
    int j = 0;
    int numSub = 0;
    int numTC = 0;
    int SumTC = 0;
    for (j = 0; j < n; j++) {
        if (pro.get(j).getMark() >= 5) {
            numSub++;
            numTC += pro.get(j).getNumTC();
        }
        SumTC += pro.get(j).getNumTC();
    }
  %>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chương trình đào tạo</title>
        <style media="all" type="text/css">
            <%-- CSS definition --%>
            #topleft{
                float: left;
                text-align: left;
            }
            #ropright{
                float: right;
                text-align: right;
                margin-right: 20px;
            }
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuSV.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">CHƯƠNG TRÌNH KHUNG</div>
                <div class="clear"></div>
                <br />
                <div>
                    <div id="topleft">
                        <b>Họ Tên: <%=student.getFullName()%></b><br>
                        <b>MSSV: <%=student.getId()%></b>
                    </div>
                    <div id="ropright">
                        <b>Lớp: <%=classes.getClassName()%> </b><br>
                        <b>Khoa: <%=faculty.getFacultyName()%></b>
                    </div>
                </div>
                <br><br>
               	<hr/><hr/>
                <p>
                    Tổng số môn đã hoàn thành: <%=numSub%>	<br/>
                    Tổng số tín chỉ đã tích lũy: <%=numTC%>    <br>
                    Tổng số tín chỉ cần tích lũy: <%=SumTC%><br>
                </p><br>
                <div style="font-size: 12px; font-weight: inherit;"><b><u>Chi tiết chương trình đào tạo</u></b></div>
                
                <table class="general-table" style="width:865px;">
                    <tr>
                        <th>STT</th><th>Học kỳ</th><th>Mã Môn</th><th>Tên môn</th><th>Số TC</th><th>LT</th><th>TH</th><th>Điểm</th><th>Đạt</th>
                    </tr>
                    <%
                            for (j = 0; j < n; j++) {%>
                    <tr>
                        <td><%=j + 1%></td><td><%=pro.get(j).getSemester()%></td><td><%=pro.get(j).getSubCode()%></td><td><%=pro.get(j).getSubName()%></td><td><%=pro.get(j).getNumTC()%></td><td><%=pro.get(j).getNumTCLT()%></td><td><%=pro.get(j).getNumTCTH()%></td>
                        <%if (pro.get(j).getMark() > 0) {%>
                        <td><%=pro.get(j).getMark()%></td>
                        <%} else {%>
                        <td></td>
                        <%}%>
                        <%if (pro.get(j).getMark() >= 5) {%>
                        <td>x</td>
                        <%} else {%>
                        <td></td>
                        <%}%>
                    </tr>
                    <%}%>
                </table>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"></script>
    </body>
</html>
