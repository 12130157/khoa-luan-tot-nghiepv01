<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.News"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    List<News> listNews = (List<News>) session.getAttribute("newsList");
     Integer numpage = (Integer) session.getAttribute("numpage");

 %>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý tin tức</title>
        <style media="all" type="text/css">

            #formdetail table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #formdetail table th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
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
                    <u><h3>Quản lý tin tức hệ thống</h3></u>
                </div>
                <br><h3>Danh sách các tin tức:</h3>
                   <a href="CreateNews.jsp">Đăng tin mới</a>
                 <hr/><hr/><br>
                <div id="NewsList">
                <form id="formdetail" name="formdetail">
                    <table id="Newsdetail" name="Newsdetail" border="2" bordercolor="yellow" >
                        <tr>
                            <th>STT</th><th>Ngày đăng</th><th >Tiêu đề</th><th>Tình trạng</th><th>Sửa</th><th>Xem</th><th>Xóa</th>
                        </tr>
                        <%
                        if (listNews != null) {
                           for (int i = 0; i < listNews.size(); i++) {
                            News n = listNews.get(i);
                      %>
                      <tr>
                          <td><%=i + 1%></td>
                          <td><%=n.getCreatedDate()%></td>
                          <td><%=n.getTitle() %></td>
                          <%if (listNews.get(i).getType() == 1) {%>
                            <td>Ðang đăng</td>
                            <%} else {%>
                            <td>Không đăng</td>
                            <%}%>
                            <%if (listNews.get(i).getType() == 0) {%>
                            <td><a href="../../NewsController?action=update&Id=<%=listNews.get(i).getId()%>">Đăng</a> </td>
                            <%} else {%>
                            <td><a href="../../NewsController?action=update&Id=<%=listNews.get(i).getId()%>">Gỡ bỏ</a> </td>
                            <%}%>
                            <td><a href="../../NewsController?Actor=PDT&action=detail&Id=<%=listNews.get(i).getId()%>">Xem</a> </td>
                            <td><a href="../../NewsController?action=delete&Id=<%=listNews.get(i).getId()%>">Xóa</a> </td>
                      </tr>
                      <%}
                    }%>
                    </table>
                    <div id="page">
                         <input type="button" value="|<<" onclick="FirstPage()"/>- 
                         <input type="button" value="<<" onclick="PrePage()"/>-
                         <input type="button" value=">>" onclick="NextPage()"/>-
                         <input type="button" value=">>|" onclick="EndPage()"/>
                         <input type="hidden" value="<%=numpage%>" id="numpage" />
                    </div>
                    <br/>
                </form>
               </div>      
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/NewsManage.js"></script>
     <script  type = "text/javascript" >
         var currentpage=1;
         var http = createRequestObject();
         numpage=document.getElementById("numpage").value;
         function FirstPage(){
             currentpage=1;
             SendRequest();
         }
         function PrePage(){
             currentpage=currentpage-1;
             if(currentpage<1) currentpage=1;
             SendRequest();
         }
         function NextPage(){
              currentpage=currentpage+1;
              if(currentpage>numpage)currentpage=numpage;
              SendRequest();
         }
         function EndPage(){
             currentpage=numpage;
             SendRequest();
         }
         function SendRequest(){
              if(http){
              ajaxfunction("../../NewsController?action=Filter&curentPage="+currentpage);
             }
         }
    </script>
</html>