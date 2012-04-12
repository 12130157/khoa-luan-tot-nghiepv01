<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<%
Student student=(Student) session.getAttribute("student");
%>
<html>
  <head>	
    <link href="../../csss/comment.css" rel="stylesheet" type="text/css" media="screen">
    <meta http-equiv="Pragma" content="No-cache">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="Description" content="">
	<meta name="keywords" content="">
	   <title>Ý kiến</title>
	   <link rel="stylesheet" type="text/css" href="clientscript/editor.css">
	   <script type="text/javascript" src="clientscript/editor.js"></script>
   <style media="all" type="text/css">
      #editor{
         padding-left: 150px;
      }
      #Info{
         padding-left: 150px;
         width: 500px;
      }
      #title{
                text-align: center;
            }
            #viettype{
           background-color: aquamarine;       
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
        <br><hr/><hr/><br>
        <form id="myform" name="myform" method="post" action="../../StudentCommentController?action=complete">
        <div id="Info">
            <table>
                <tr>
                    <td width="70px">Họ Tên:</td>
                    <td><input type="text" readonly name="txtName" id="txtName" value="<%=student.getFullName()%>"></td>
                </tr>
                <tr>
                    <td>Email:</td>
                    <td><input type="text" readonly name="txtEmail" id="txtEmail" value="<%=student.getEmail()%>"></td>
                </tr>
            </table>
        </div>
       <table id="editor" width="50%" height="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
	<td colspan="2" align="center" bgcolor="#ffffff">
        <span id="postArea"><br><br><br><br> Loading...</span>	
    <script type="text/javascript">
	RTE=new Editor('RTE','postArea','',600, 350);
	RTE.display();
    </script>
     <form action="" name="vietType">
         <div id="viettype" class="Avimbar"> Kiểu gõ chữ Việt :  <span> <input id="him_auto" onclick="setMethod(0);" type="radio" name="type_method">AUTO <input id="him_telex" onclick="setMethod(1);" type="radio" name="type_method">TELEX <input id="him_vni" onclick="setMethod(2);" type="radio" name="type_method">VNI <input id="him_viqr" onclick="setMethod(3);" type="radio" name="type_method">VIQR <input id="him_viqr2" onclick="setMethod(4);" type="radio" name="type_method">VIQR* <input id="him_off" onclick="setMethod(-1);" type="radio" name="type_method">OFF </span></div>
    <hr width="80%" color="green" size="1">
  </form>
     <script type="text/javascript" src="clientscript/avim.js"></script>
     </td>
    </tr>
    </table>
                <input type="hidden" id="content" name="content" value="">
 </form> 
                
 </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
 </body>
 </html>