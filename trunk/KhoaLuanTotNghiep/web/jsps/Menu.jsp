<%-- 
    Document   : jspmenu
    Created on : Apr 23, 2011, 3:50:32 PM
    Author     : ngloc_it
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../csss/menu.css" rel="stylesheet" type="text/css" media="screen">

    </head>
    <body>
    <MARQUEE onmouseover="this.stop();" onmouseout="this.start();" HEIGHT=25 BGCOLOR=transparent BEHAVIOR=scroll SCROLLAMOUNT="4">
        <h3>Đại Học Quốc Gia Tp. Hồ Chí Minh-Trường Đại Học Công Nghệ Thông Tin-Khoa Kỹ Thuật Phần Mềm.</h3>
    </MARQUEE>

    <div id="logo">
        <DIV id="flash" style="Z-INDEX: 1; padding-left: 10%; padding-top: 0px; WIDTH: 80%; POSITION: absolute; TOP: 0px; HEIGHT: 0px; margin-left: -150px; margin-top: 40px">
            <OBJECT height=200 width=1060>
                <embed src="../flash/flash.swf" width="1060" height="120"
                       quality="high" 
                       type="application/x-shockwave-flash" menu="false"
                       wmode="transparent">
                </embed>
            </OBJECT>
        </DIV>
    </div>
    <div id = "menu">
        <ul>
            <li><a href="../ServHompage?action=view"> Trang chủ |</a>
            </li>
            <li><a href="#">Quản Lý Khoa |</a>
                <ul>
                    <li><a href="../servLecturerManager?action=view">Quản lý giảng viên</a> </li>
                    <li><a href="../servStudentManager?action=view">Quản lý sinh viên</a> </li>
                    <li><a href="../servSubject?action=view">Quản lý môn học</a> </li>
                    <li><a href="../servCourse?action=view">Quản lý khóa học</a> </li>
                    <li><a href="../servProManage?action=view">Chương trình đào tạo</a> </li>
                    <li><a href="../servClassView?action=view">Danh sách lớp học</a> </li>
                    <li><a href="../servUpdateScore?action=view">Điểm kết thúc môn</a> </li> 
                    <li> <a href="../servDetailSubject?action=viewQL">Môn học tiên quyết</a> </li>
                    <li><a href="../servAccount?action=view">Quản lý Tài Khoản</a> </li>
                    <li><a href="../servCommentManager?action=view">Quản lý commnent</a> </li>
                    <li><a href="../ServNews?action=view">Quản lý tin túc</a> </li>
                    <li><a href="../servViewRule?issetup=false">Quy định</a> </li>
                </ul>
            </li>
            <li><a href="#">Giảng Viên |</a>
                <ul>
                    <li><a href="../servUpdateScore?action=lecturer">Điểm kết thúc môn</a> </li>  
                </ul>      
            </li>
            <li><a href="#">Sinh Viên |</a>
                <ul>
                    <li><a href="../servClassView?action=student">Danh sách lớp học</a> </li>
                    <li><a href="../ServScore?action=view">Điểm cuối kỳ</a> </li>
                    <li> <a href="../servRegistration?reg=view">Đăng Ký Môn Học</a> </li>
                    <li><a href="../servProgram">Xem Chương Trình Đào Tạo</a> </li>
                    <li> <a href="../servStudyResult?first=true">Xem kết quả học tập</a> </li>
                    <li> <a href="../servDetailSubject?action=view">Môn học tiên quyết</a> </li>
                    <li><a href="../servUpdateInfo?isupdate=false">Thông tin sinh viên</a> </li>
                    <li> <a href="../servChangePass?change=firts">Đổi mật khẩu</a> </li>
                    <li> <a href="../servSendComment?guest=notlogin">Gửi Yêu Cầu</a> </li>
                </ul>
            </li>
            <li><a href="../ViewSchedule">Thời khóa biểu |</a></li>
            <%
                HttpSession sesstion = request.getSession();
                String username = (String) session.getAttribute("username");
                if (username == null) {
            %>
            <li><a href="jspLogin.jsp">Đăng nhập |</a></li>
            <%} else {
            %>
            <li><a href="../servLogin?login=false">Đăng xuất |</a></li>
            <%}%>

            <li><a href="jspLienHe.jsp">Liên Hệ</a></li>
        </ul>
    </div>       
</body>
</html>