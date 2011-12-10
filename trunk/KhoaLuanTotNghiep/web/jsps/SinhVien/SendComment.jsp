<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<%
Student student=(Student) session.getAttribute("student");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gửi yêu cầu</title>
        <style media="all" type="text/css">
           #title{
                text-align: center;
            }
           table{
                margin-left: 20px;
                margin-top: 50px;
                margin-bottom: 120px;
                width: 650px;
                border: 2px solid;
                background-color: #495C28;
            }
            #txt-info{
                width:250px;
                background-color: #028347;
            }
            table a{
                color: #FEFAB9;
            }
            table textarea{
                width: 98%;
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
               <div id="title">
                    <u><h3>Hãy gửi ý kiến của bạn</h3></u>
                </div>
                <br><hr/><hr/>
                <div>
                    <form id="myform" name="myform" method="post" action="../../StudentCommentController?action=complete">
                    <table>
                        <tr>
                            <td><a>Họ Tên</a></td>
                            <td><input type="text" readonly name="txtName" id="txt-info" value="<%=student.getFullName()%>"></td>
                        </tr>
                        <tr>
                            <td><a>Email</a></td>
                            <td><input type="text" readonly name="txtEmail" id="txtEmail" value="<%=student.getEmail()%>"></td>
                        </tr>
                        <tr>
                            <td><a>Nội dung:</a></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><textarea type="textarea"  name="txtContent" rows="12" cols="50"></textarea></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align="right"><input type="button" name="btnSend" onclick="sendcomment()" value="Gửi ý kiến"></td>
                        </tr>
                    </table>
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
        function sendcomment(){
            var content = document.myform.txtContent.value;
            var name = document.myform.txtName.value;
            var email = document.myform.txtEmail.value;
            if(content.length==0){
                alert("Bạn chưa nhập nội dung");
            }
            else{
                document.forms["myform"].submit();
            }
        }
    </script>
  </html>