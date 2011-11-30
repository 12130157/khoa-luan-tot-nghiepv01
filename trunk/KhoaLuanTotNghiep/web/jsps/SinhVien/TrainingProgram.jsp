<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>
<%@include file="MenuSV.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Xem chương trình đào tạo</title>
        <style media="all" type="text/css">
            table{
                margin-top: 10px;
                margin-left: 5px;
                margin-bottom: 120px;
                width: 99%;
            }

            table th{
                height: 15px;
                background-color: #00ff00;
            }

            table td{
                text-align: center;
                background-color: #5F5A59;
            }
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
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <br>
                <h2 align="center"><u>CHƯƠNG TRÌNH KHUNG</u></h2><br>
                <div>
                    <div id="topleft">
                        <b>Họ Tên: Nguyễn Trung Thành</b><br>
                        <b>MSSV: 07520319</b>
                    </div>
                    <div id="ropright">
                        <b>Lớp: Công nghệ phần mềm 02</b><br>
                        <b>Khoa: Kỹ thuật phần mềm</b>
                    </div>
                </div>
                <br><br>
               	<hr/><hr/>
                <p>
		    Tổng số môn đã hoàn thành: 120	<br/>
                    Tổng số tín chỉ đã tích lũy: 120    <br>
                    Tổng số tín chỉ cần tích lũy: 120<br>
                </p><br>
                <u>Chi tiết chương trình đào tạo:</u>
                <table>
                    <tr>
                        <th>STT</th><th>Học kỳ</th><th>Mã Môn</th><th>Tên môn</th><th>Số TC</th><th>LT</th><th>TH</th><th>Điểm</th><th>Đạt</th>
                    </tr>
                   
                </table>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
</html>
