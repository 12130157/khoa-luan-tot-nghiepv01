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
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">

    </head>
    <body>
    <MARQUEE onmouseover="this.stop();" onmouseout="this.start();" HEIGHT=25 BGCOLOR=transparent BEHAVIOR=scroll SCROLLAMOUNT="4">
        <h3>Đại Học Quốc Gia Tp. Hồ Chí Minh - Trường Đại Học Công Nghệ Thông Tin.</h3>
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
             <li><a href="../../HomepageController?actor=PDT"> Trang chủ </a></li>
             <li><a href=""> Quản lý con người </a>
                 <ul>
                     <li><a href="../../ManageStudentController?function=liststudent"> Quản lý sinh viên</a></li>
                     <li><a href="../../ManageLecturerController?function=listlecturer"> Quản lý giảng viên</a></li>
                 </ul>
             </li>
             <li><a href="../../AccountController?action=manager"> Quản lý tài khoản </a></li>
             <li><a href="#"> Quản lý học vụ </a>
                 <ul>
                     <li><a href="../../ManageSubjectController?function=list_subject&ajax=false"> Quản lý môn học</a></li>
                     <li><a href="../../ManageClassController?action=default"> Quản lý lớp học</a></li>
                     <li><a href="../../ReportController?action=default"> Thống kê đăng ký</a></li>
                     <li><a href="../../TimeController?action=default"> Thời gian đăng ký</a></li>
                </ul>
             </li>
             <li><a href=""> Đào tạo </a>
                 <ul>
                     <li><a href="#"> Khóa học</a></li>
                     <li><a href="#"> Quản lý khoa</a></li>
                     <li><a href="#"> Chương trình đào tạo</a></li>
                     <li><a href="#"> Quản lý lớp</a></li>
                 </ul>
             </li>
             <li><a href="../../ManageScoreController?function=manage"> Điểm cuối kỳ |</a></li>
             <li><a href=""> Quy định </a>
                 <ul>
                     <li><a href="../../PreSubjectController?action=manage"> Môn học tiên quyết</a></li>
                     <li><a href="../../ManageRuleController?action=default"> Quy định</a></li>
                 </ul>
             </li>
             <li><a href=""> Giao tiếp </a>
                 <ul>
                     <li><a href="../../NewsController?action=manager"> Quản lý tin tức</a></li>
                     <li><a href="../../CommentController?action=manager"> Quản lý yêu cầu</a></li>
                 </ul>
             </li>
             <li><a href="../../Login?function=logout"> Đăng xuất</a></li>
         </ul>
    </div>       
</body>
</html>