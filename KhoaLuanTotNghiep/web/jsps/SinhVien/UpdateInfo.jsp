<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
Student student=(Student) session.getAttribute("student");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thông tin cá nhân</title>
        <style media="all" type="text/css">

            #formdetail table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #formdetail table th{
                background-color: #5F676D;
                height: 30px;
            }

            #formdetail table td{
                text-align: center;
                background-color: #5F676D;
            }
            #title{
                text-align: center;
            }
            #page{
                text-align: center;
            }
            a {
                 color: violet;
            }
            #button{
                text-align: center;
            }
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
                <div id="title">
                    <u><h3><%=student.getFullName()%></h3></u>
                    <h3>Mã số sinh viên: <%=student.getId()%></h3>
                </div>
                <h3>Cập nhật thông tin cá nhan:</h3>
                   <hr/><hr/><br>
                <div id="NewsList">
                    <form id="formdetail" name="formdetail" action="../../AccountController?action=update" method="post">
                    <table>
                        <tr>
                            <td><i>Ngày sinh</i> </td>
                            <th>
                                <input type="text" id="birthday" name="birthday" readonly="readonly" value="<%=student.getBirthday()%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('birthday','YYMMMDD')" />
                            </th>
                        </tr>  
                        <tr>
                            <td><i>Giới tính</i> </td>
                            <th>
                                <select name="gender" style="width:100px">
                                    <%
                                    if(student.getGender().equalsIgnoreCase("Nam")){
                                    %>
                                    <option value="Nam">  Nam  </option>
                                    <option value="Nữ">  Nữ  </option>
                                    <%}else{%>
                                    <option value="Nữ">  Nữ  </option>
                                    <option value="Nam">  Nam  </option>
                                    <%}%>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <td><i>Ngày nhập học</i> </td>
                            <th>
                                <input type="text" id="startdate" name="startdate" readonly="readonly" value="<%=student.getDateStart()%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('startdate','YYMMMDD')" />
                            </th>
                        </tr>
                        <tr>
                            <td><i>CMND </i></td>
                            <th><input type="text" name="IdentityCard" id="IdentityCard" onkeypress="return checknumber(event)" value="<%=student.getIdentityNumber()%>"/></th>
                        </tr>
                        <tr>
                            <td><i>Quê quán</i> </td>
                            <th><input type="text" name="home" id="home" value="<%=student.getHomeAddr()%>" size="100"/></th>
                        </tr>
                        <tr>
                            <td><i>Đại chỉ liên lạc</i> </td>
                            <th><input type="text" name="address" id="address" value="<%=student.getAddress()%>" size="100"/></th>
                        </tr>
                        <tr>
                            <td><i>Điện thoại</i> </td>
                            <th><input type="text" name="phone" id="phone" onkeypress="return checknumber(event)" value="<%=student.getPhone()%>"/></th>
                        </tr>
                     </table>
                        <div id="button">
                            <input type="button" name="OK" id="OK" onclick="Change()" value="       OK       "/>
                            <input type="button" name="Can" id="Can" onclick="Cancel();" value="     Đóng     "/>
                        </div>
                </form>
              <form id="reset" name="reset" action="../../AccountController?action=Info" method="post">
                  
              </form>
               
               </div>      
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   <script  type = "text/javascript" >
        function Cancel(){
             document.forms["reset"].submit();
        }
        function Change(){
            var identityCard=document.getElementById("IdentityCard").value;
            var home=document.getElementById("home").value;
            var address=document.getElementById("address").value;
            var phone=document.getElementById("phone").value;
           if(identityCard.length==0){
                alert("Hãy nhập CMND");
            }
            else if(home.length==0){
                alert("Hãy nhập đại chỉ thường trú");
            }
            else if(address.length==0){
                alert("Hãy nhập địa chỉ liên lạc");
            }
            else if(phone.length==0){
                alert("Hãy nhập điện thoại");
            }
           else{
                document.forms["formdetail"].submit();
            }
        }
        function checknumber(evt){
           var e = event || evt; // for trans-browser compatibility
	var charCode = e.which || e.keyCode;

	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

	return true;
        }
    </script>
</html>