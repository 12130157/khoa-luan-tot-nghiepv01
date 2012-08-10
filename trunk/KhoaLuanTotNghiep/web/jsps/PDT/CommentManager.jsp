<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.StringUtils"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Comment"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<Comment> listComment = (List<Comment>) session.getAttribute("commentList");
    Integer numpage = (Integer) session.getAttribute("numpage");
 %>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý comment</title>
        <style media="all" type="text/css">
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
                    Quản lý tin ý kiến
                </div>
                <br><h3>Danh sách các ý kiến:</h3>
                <br>
                 <hr/><hr/><br>
                <div id="NewsList">
                <form id="formdetail" name="formdetail">
                    <table id="Commentdetail" class="general-table" name="Commentdetail">
                        <tr>
                            <th>STT</th><th>Người gủi</th><th >Nội dung</th><th>Ngày gửi</th><th>Tình trạng</th><th>Xem</th><th>Xóa</th>
                        </tr>
                        <%
                        if (listComment != null) {
                           for (int i = 0; i < listComment.size(); i++) {
                            Comment n = listComment.get(i);
                            String content = n.getContent();
                            if (content != null && content.length()>200) {
                                content = content.substring(0, 200) + "...";
                            }
                            if (content != null && !content.isEmpty()) {
                                content = StringUtils.StripHTML(content);
                            }
                      %>
                      <tr>
                          <td><%=i + 1%></td>
                          <td><%=n.getAuthor() %></td>
                          <td><%=content%></td>
                          <td><%=n.getCreateDate()%></td>
                          <%if (n.getStatus()== 1) {%>
                            <td>Ðã xem</td>
                            <%} else {%>
                            <td>Chưa xem</td>
                            <%}%>
                            <td><a href="../../CommentController?action=detail&Id=<%=n.getId()%>">Xem</a> </td>
                            <td><a href="../../CommentController?action=delete&Id=<%=n.getId()%>">Xóa</a> </td>
                      </tr>
                      <%}
                    }%>
                    </table>
                    <div id="paggind">
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
    <script src="../../javascripts/AjaxUtil.js"> </script>
    <script src="../../javascripts/CommentManager.js"> </script>
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
               ajaxfunction("../../CommentController?action=Filter&curentPage="+currentpage);
             }
         }
    </script>
    </body>
</html>