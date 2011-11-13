<%-- 
    Document   : jspMainNav
    Created on : 26-04-2011, 21:12:12
    Author     : ngloc_it
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <style media="all" type="text/css">
        #mainmenu a{
            font-family:Arial, Helvetica, sans-serif;
            font-size:12px;
            color:#ffffff;
            font-weight:bold;
            text-decoration:none;
            margin-left: 20px;
        }
        #mainmenu a:hover{
            color:red;
            font-style: italic;
        }
        #mainmenu li{
            list-style:none;
        }
        #mainmenu li a{
            line-height:35px;
        }
        #mainmenu li li{
            display:block;
            border: 1px solid #5F676D;
            background: url("../imgs/opaque_10.png") repeat-x scroll 0 0 transparent;
            text-align:left;
        }
        #mainmenu li li a{
            line-height:30px;
        }
        #mainmenu ul li ul{
            position:absolute;
            margin-left:10px;
            display:none;
            top: 322px;
            left: 378px;
        }
        #mainmenu li:hover>ul{
            display:block;
            width: 15%;
        }
    </style>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
    </head>
    <body>
        <div id="mainmenu">
            <ul>
                <li><a href="../ServHompage?action=view"> Trang chủ </a></li>
                <li> <a href="">Quản lý khoa</a>
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
                        <li><a href="../servViewRule?issetup=false">Quy định</a> </li>
                    </ul>
                </li>
                <li><a href=""> Giảng viên </a>
                    <ul>
                        <li><a href="../servUpdateScore?action=lecturer">Điểm kết thúc môn</a> </li>  
                    </ul>
                </li>
                <li><a href=""> Sinh viên </a>
                    <ul>
                        <li><a href="../servClassView?action=student">Danh sách lớp học</a> </li>
                        <li> <a href="../servRegistration?reg=view">Đăng Ký Môn Học</a> </li>
                        <li><a href="../servProgram">Xem Chương Trình Đào Tạo</a> </li>
                        <li> <a href="../servStudyResult?first=true">Kết quả học tập</a> </li>
                        <li> <a href="../servDetailSubject?action=view">Môn học tiên quyết</a> </li>
                        <li><a href="../servUpdateInfo?isupdate=false">Thông tin sinh viên</a> </li>
                        <li> <a href="../servChangePass?change=firts">Đổi mật khẩu</a> </li>
                        <li> <a href="../servSendComment?guest=notlogin">Gửi Yêu Cầu</a> </li>
                    </ul>
                </li>
                <li><a href="jspLienHe.jsp">Liên Hệ</a></li>
            </ul>
        </div>
    </body>
</html>
