<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.News"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    // List<News> listNews = (List<News>) session.getAttribute("newsList");

 %>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link rel="stylesheet" type="text/css" href="../../editor/editor.css">
        <meta http-equiv="Pragma" content="No-cache">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="Description" content="">
        <meta name="keywords" content="">
        <script type="text/javascript" src="../../editor/editor.js"></script>
        <title>Tạo tin mới</title>
        <style media="all" type="text/css">

            #formdetail table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;

            }
            #formdetail table th{
                height: 30px;
                border-color: black;
            }

            #formdetail table td{
                
            }
            
            #formdetail table textarea{
                width: 98%;
            }
        </style>
    </head>
    <body>
        <%--Div Wrapper--%>
        <div id="wrapper">            
            <%--Menu--%>
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Quản lý tin tức hệ thống
                </div>
                <br><h3>Đăng tin mới:</h3>
                <hr/><hr/>
                <form id="myform" name="myform" method="post" action="../../NewsController?action=insert" enctype="multipart/form-data">
                    <div id="Info">
                        <table style="margin-left: 28px;">
                            <tr>
                                <td>Tiêu đề:</td>
                                <td><input type="text"  name="NewsTiltle" id="NewsTiltle" size="100"></td>
                            </tr>
                            <tr>
                                <td>Loại:</td>
                                <td>
                                    <select id="Type" name="Type">
                                        <option value="1">Đăng ngay</option>
                                        <option value="0">Chưa đăng</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    
                    <table id="editor" width="90%" height="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                            <td colspan="2" align="center">
                                <span id="postArea"><br><br><br><br> Loading...</span>	
                                <script type="text/javascript">
                                    RTE=new Editor('RTE','postArea','',800, 350);
                                    RTE.display();
                                </script>
                                <script type="text/javascript" src="../../editor/avim.js"></script>
                            </td>
                        </tr>
                    </table>
                    <%-- File attached --%>
                    <u>File đính kèm</u>
                    <div class="clear" style="text-align: center;"> </div>
                    <input type="file" name="txtPath" id="txtPath" />
                    <div class="clear"> </div>
                    <br />
                    </div>
                    <input type="hidden" id="content" name="content" value="">
            </form>
                <%--
                 <br>
                <div id="NewsList">
                    <form id="formdetail" name="formdetail" action="../../NewsController?action=insert" method="post">
                    <table>
                        <tr>
                            <td>Tiêu đề:</td>
                            <td><input type="text"  name="NewsTiltle" id="NewsTiltle" size="100"></td>
                        </tr>
                        <tr>
                            <td>Loại:</td>
                            <td>
                                <select id="Type" name="Type">
                                    <option value="1">Đăng ngay</option>
                                    <option value="0">Chưa đăng</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>Nội dung:</td>
                            <td><textarea type="textarea" id="newscontent" name="newscontent" rows="12" cols="50"></textarea></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align="right"><input type="button" name="btnSend" onclick="addNews()" value="Đăng tin"></td>
                        </tr>
                    </table>
                </form>
               </div>      
                --%>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    <script type = "text/javascript">
         function addNews(){
             var content = document.formdetail.newscontent.value;
             var title = document.formdetail.NewsTiltle.value;
             if(title.length==0){
                 alert("Chưa có tiêu đề thông báo");
             } else if(content.length==0){
                 alert("Chưa có nội dung thông báo");
             }else{
                 document.forms["formdetail"].submit();
             }
         }
    </script>
    </body>
</html>