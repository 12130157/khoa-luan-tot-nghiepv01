<%-- 
    Document   : EditLecturer
    Created on : 24-12-2011, 12:40:49
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@include file="MenuPDT.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    Lecturer lecturer = (Lecturer) session.getAttribute("lecturer");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý giang viên</title>
        <style media="all" type="text/css">
            #tablelistlecturer{
                margin-left: 10px;
                margin-top: 20px;
                margin-bottom: 20px;
                width: 740px;
                border: 3px solid #73726E;
            }
            #tablelistlecturer th{
                height: 32px;
                font-weight: bold;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                text-align: center;
            }
            #tablelistlecturer td{
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                padding: 2px 5px 2px 5px;
                text-align: left;
            }
            #formsearch{
                margin-top: 10px;
                margin-left: 20px;
                padding: 5px 10px 5px 10px;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                border: 3px solid #73726E;
                width: 320px;
            }
            #red{
                margin-left: 32px;
                margin-top: 15px;
                background-color: #e4e4e3;
                width: 250px;
                height: 32px;
            }
            #red:hover {
                border: 2px solid #ff092d;
            }
            #sidebar {
                height:400px;
                overflow:auto;
            }
        </style>
    </head>
    <body onload="">
        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div style="text-align: center;">
                    <u> <h3> Trang chỉnh sửa thông tin Giảng Viên </h3></u>
                </div>
                <p id="error">

                </p>

                <% if (lecturer != null) { %>
               <table>
                   <tr> <td style="width: 150px">  Mã GV  </td> <td style="width: 320px" > <%= lecturer.getId() %> </td> </tr>
                   <tr> <td> Họ và tên </td> <td> <input type="text" value="<%= lecturer.getFullName() %>"> </td> </tr>
                   <tr> <td> Khoa </td> <td> <input type="text" value="<%= lecturer.getFacultyCode() %>"> </td> </tr>
                   <tr> <td> Ngày sinh </td> <td> <input type="text" value="<%= lecturer.getBirthday() %>"> </td> </tr>
                   <tr> <td> Quê quán </td> <td> <input type="text" value="<%= lecturer.getAddress() %>"> </td> </tr>
                   <tr> <td> CMND </td> <td> <input type="text" value=" <%= lecturer.getIdentityCard() %>"> </td> </tr>
                   <tr> <td> Điện thoại </td> <td> <input type="text" value="<%= lecturer.getPhone() %>"> </td> </tr>
                   <tr> <td> Email </td> <td> <input type="text" value=" <%= lecturer.getEmail() %>"> </td> </tr>
                   <tr> <td> Giới tính </td> <td> <input type="text" value="<%= lecturer.getGender() %>"> </td> </tr>
                   <tr> <td> Học Hàm </td> <td> <input type="text" value="<%= lecturer.getHocHam() %>"> </td> </tr>
                   <tr> <td> Học Vị </td> <td> <input type="text" value="<%= lecturer.getHocVi() %>"> </td> </tr>
                   <tr> <td> Ghi chú </td> <td> <input type="text" value="<%= lecturer.getNote() %>"> </td> </tr>
                   <tr style="margin-top: 25px;">
                       <td>  </td>
                       <td> 
                           <input type="button" onclick="" value="Hoàn thành">
                           <input type="button" onclick="" value="Hủy">
                       </td>
                   </tr>
               </table>
               <% } %>
                
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <SCRIPT language = "javascript">
        function createRequestObject(){
            var req;
            if(window.XMLHttpRequest){
                req = new XMLHttpRequest();
            } else if(window.ActiveXObject){
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } else{
                alert('Functions does not support you Brower');
            }
            return req;
        }
 
        function handleResponse(){
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("error");
                detail.innerHTML = http.responseText;
            }
        }
        
    </SCRIPT>
</html>
